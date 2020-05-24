package kafka.mail.sender;

import kafka.registration.plate.IKafkaConstants;
import kafka.registration.plate.RetrievedRegPlate;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
public class VerifiedRegistrationPlateProducer {


    private static KafkaProducer<String, ValidatedRegistrationPlate> producer = new KafkaProducer<>(getKafkaProperties());

    public void send(Map<RetrievedRegPlate, Boolean> verifiedPlates) {
        sendRequests(producer, verifiedPlates);
    }

    private void sendRequests(KafkaProducer<String, ValidatedRegistrationPlate> producer, Map<RetrievedRegPlate, Boolean> verifiedPlates) {
        verifiedPlates.forEach((key, value) -> {
                    ValidatedRegistrationPlate validatedRegistrationPlate = new ValidatedRegistrationPlate(key, value);
                    final ProducerRecord<String, ValidatedRegistrationPlate> record =
                            new ProducerRecord<>(IKafkaConstants.VALIDATED_REGISTRATION_TOPIC_NAME,
                                    key.getRegPlate(), validatedRegistrationPlate);
                    producer.send(record, new DemoProducerCallback());
                }
        );
        producer.flush();
    }

    private static Properties getKafkaProperties() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ValidatedRegistrationPlateSerializer.class);
        String schemaRegURL = "http://localhost:8081/";
        //props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegURL);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}

class DemoProducerCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        System.out.println("Published record: " + recordMetadata);
    }
}