package kafka.registration.plate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.kafka.common.serialization.StringSerializer;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class RetrievedRegPlate {
    private String regPlate;
    private String imgPath;
}
