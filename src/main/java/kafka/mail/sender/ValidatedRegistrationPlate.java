package kafka.mail.sender;

import kafka.registration.plate.RetrievedRegPlate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ValidatedRegistrationPlate {

    private RetrievedRegPlate registrationPlate;
    private Boolean isValid;


}
