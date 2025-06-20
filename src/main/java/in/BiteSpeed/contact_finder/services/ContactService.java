package in.BiteSpeed.contact_finder.services;

import in.BiteSpeed.contact_finder.dtos.IdentifyResponseDTO;
import in.BiteSpeed.contact_finder.entities.Contact;
import in.BiteSpeed.contact_finder.enums.LinkPrecedence;
import in.BiteSpeed.contact_finder.repositories.ContactRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public IdentifyResponseDTO identify(String email, String phoneNumber) {

        List<Contact> matched = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        // If no contacts found, create a new primary contact
        if (matched.isEmpty()) {
            Contact newContact = saveContact(email, phoneNumber, null,LinkPrecedence.primary);
            return getIdentifyResponseDTO(newContact, Collections.singletonList(email), Collections.singletonList(phoneNumber), new ArrayList<>());
        }

        // Find all linked contacts (recursively fetch all related)
        Set<Contact> allLinked = new HashSet<>();
        Queue<Contact> queue = new LinkedList<>(matched);
        while (!queue.isEmpty()) {
            Contact c = queue.poll();

            if (allLinked.add(c)) {
                // if it's a secondary
                if (c.getLinkedId() != null) {
                    contactRepository.findById(c.getLinkedId()).ifPresent(parent -> {
                        if (allLinked.add(parent)) {
                            queue.add(parent);
                        }
                    });
                }

                //children
                List<Contact> children = contactRepository.findByLinkedId(c.getId());
                for (Contact child : children) {
                    if (allLinked.add(child)) {
                        queue.add(child);
                    }
                }
            }
        }

        // Find all unique primary contacts among the group
        List<Contact> primaryContacts = allLinked.stream()
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.primary)
                .sorted(Comparator.comparing(Contact::getCreatedAt))
                .toList();

        Contact truePrimary = primaryContacts.get(0);

        // Demote other primaries (if any) to secondary
        for (int i = 1; i < primaryContacts.size(); i++) {
            Contact toDemote = primaryContacts.get(i);
            toDemote.setLinkPrecedence(LinkPrecedence.secondary);
            toDemote.setLinkedId(truePrimary.getId());
            toDemote.setUpdatedAt(LocalDateTime.now());
            contactRepository.save(toDemote);
        }

        // Determine if a new contact needs to be created
        boolean alreadyExists = allLinked.stream().anyMatch(c ->
                Objects.equals(c.getEmail(), email) && Objects.equals(c.getPhoneNumber(), phoneNumber));

        boolean newInfo = false;
        if (email != null) {
            newInfo = allLinked.stream().noneMatch(c -> email.equals(c.getEmail()));
        }
        if (!newInfo && phoneNumber != null) {
            newInfo = allLinked.stream().noneMatch(c -> phoneNumber.equals(c.getPhoneNumber()));
        }

        if (!alreadyExists && newInfo) {
            Contact newSecondary = saveContact(email, phoneNumber, truePrimary.getId(),LinkPrecedence.secondary);
            allLinked.add(newSecondary);
        }

        // Prepare final response
        return getFinalResponseDTO(allLinked, truePrimary);
    }

    private Contact saveContact(String email, String phoneNumber, Long primaryId, LinkPrecedence precedence) {
        Contact newContact = new Contact();
        newContact.setEmail(email);
        newContact.setPhoneNumber(phoneNumber);
        newContact.setLinkedId(primaryId);
        newContact.setLinkPrecedence(precedence);
        newContact.setCreatedAt(LocalDateTime.now());
        newContact.setUpdatedAt(LocalDateTime.now());
        return contactRepository.save(newContact);

    }

    private IdentifyResponseDTO getFinalResponseDTO(Set<Contact> allLinked, Contact truePrimary) {
        List<String> emails = allLinked.stream()
                .map(Contact::getEmail)
                .filter(Objects::nonNull)
                .distinct()
                .sorted((a, b) -> a.equals(truePrimary.getEmail()) ? -1 : 1)
                .collect(Collectors.toList());

        List<String> phoneNumbers = allLinked.stream()
                .map(Contact::getPhoneNumber)
                .filter(Objects::nonNull)
                .distinct()
                .sorted((a, b) -> a.equals(truePrimary.getPhoneNumber()) ? -1 : 1)
                .collect(Collectors.toList());

        List<Long> secondaryIds = allLinked.stream()
                .map(Contact::getId)
                .filter(id -> !id.equals(truePrimary.getId()))
                .sorted()
                .collect(Collectors.toList());

        return getIdentifyResponseDTO(truePrimary, emails, phoneNumbers, secondaryIds);
    }

    private IdentifyResponseDTO getIdentifyResponseDTO(Contact truePrimary, List<String> emails, List<String> phoneNumbers, List<Long> secondaryIds) {
        return new IdentifyResponseDTO(new IdentifyResponseDTO.ContactData(
                truePrimary.getId(),
                emails,
                phoneNumbers,
                secondaryIds
        ));
    }

}
