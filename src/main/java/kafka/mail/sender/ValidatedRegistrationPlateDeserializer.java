package kafka.mail.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ValidatedRegistrationPlateDeserializer implements Deserializer<ValidatedRegistrationPlate> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public ValidatedRegistrationPlate deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        ValidatedRegistrationPlate object = null;
        try {
            object = mapper.readValue(data, ValidatedRegistrationPlate.class);
        } catch (Exception exception) {
            System.out.println("Error in deserializing bytes " + exception);
        }
        return object;
    }

    @Override
    public void close() {
    }
}