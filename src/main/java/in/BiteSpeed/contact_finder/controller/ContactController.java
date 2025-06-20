package in.BiteSpeed.contact_finder.controller;

import in.BiteSpeed.contact_finder.dtos.IdentifyRequestDTO;
import in.BiteSpeed.contact_finder.dtos.IdentifyResponseDTO;
import in.BiteSpeed.contact_finder.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identify")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<IdentifyResponseDTO> identify(@RequestBody IdentifyRequestDTO request) {
        if (request.getEmail() == null && request.getPhoneNumber() == null) {
            return ResponseEntity.badRequest().build();
        }

        IdentifyResponseDTO response = contactService.identify(
                request.getEmail(), request.getPhoneNumber());
        return ResponseEntity.ok(response);
    }
}
