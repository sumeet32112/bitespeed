package in.BiteSpeed.contact_finder.dtos;

import lombok.Data;

@Data
public class IdentifyRequestDTO {
    private String email;
    private String phoneNumber;
}
