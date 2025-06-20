package in.BiteSpeed.contact_finder.repositories;

import in.BiteSpeed.contact_finder.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("SELECT c FROM Contact c WHERE c.email = :email OR c.phoneNumber = :phoneNumber")
    List<Contact> findByEmailOrPhoneNumber(@Param("email") String email, @Param("phoneNumber") String phoneNumber);

    List<Contact> findByLinkedId(Long linkedId);
    Optional<Contact> findById(Long id);
}
