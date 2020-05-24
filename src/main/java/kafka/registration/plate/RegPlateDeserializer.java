package kafka.registration.plate;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.mail.sender.ValidatedRegistrationPlate;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;


public class RegPlateDeserializer implements Deserializer<RetrievedRegPlate> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public RetrievedRegPlate deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        RetrievedRegPlate object = null;
        try {
            object = mapper.readValue(data, RetrievedRegPlate.class);
        } catch (Exception exception) {
            System.out.println("Error in deserializing bytes " + exception);
        }
        return object;
    }

    @Override
    public void close() {
    }
}