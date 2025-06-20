package in.BiteSpeed.contact_finder.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifyResponseDTO {
    private ContactData contact;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContactData {
        private Long primaryContatctId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Long> secondaryContactIds;
    }
}
