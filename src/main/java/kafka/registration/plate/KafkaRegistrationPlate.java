package kafka.registration.plate;

import forestInspector.CallForestInspector;
import kafka.mail.sender.VerifiedRegistrationPlateProducer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaRegistrationPlate {
    public static final String UNIDENTIFIED = "unidentified";
    Logger l = Logger.getLogger(KafkaRegistrationPlate.class);

    @Autowired
    private CallForestInspector forestInspector;


    @Autowired
    private VerifiedRegistrationPlateProducer verifiedRegistrationPlateProducer;


    public void readKafkaRegistrationPlates(Consumer<String, RetrievedRegPlate> consumer) {
        int noMessageFound = 0;
        while (true) {
            ConsumerRecords<String, RetrievedRegPlate> consumerRecords = consumer.poll(1000);
            // 1000 is the time in milliseconds consumer will wait if no record is found at broker.
            if (consumerRecords.count() == 0) {
                noMessageFound++;
                if (noMessageFound > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                    // If no message found count is reached to threshold exit loop.
                    break;
                else
                    continue;
            }
            //print each record.

            Map<RetrievedRegPlate, Boolean> stringBooleanMap = manageRegistrationPlate(consumerRecords);
            verifiedRegistrationPlateProducer.send(stringBooleanMap);
            // commits the offset of record to broker.
            consumer.commitAsync(); // sent ack to kafka ( to remove message from kafka )
            stringBooleanMap.forEach((s, aBoolean) -> System.out.println(s + " " + aBoolean));
        }
        consumer.close();
    }

    private Map<RetrievedRegPlate, Boolean> manageRegistrationPlate(ConsumerRecords<String, RetrievedRegPlate> consumerRecords) {
        Map<RetrievedRegPlate, Boolean> verifiedPlates = new HashMap<>();
        for (ConsumerRecord<String, RetrievedRegPlate> record : consumerRecords) {/*
            System.out.println(consumer.toString());
            System.out.println(Thread.currentThread());*/
            System.out.println("Retrieved registration plate : " + record.value().getRegPlate());
            if (record.value() == null) {
                continue;
            }
            RetrievedRegPlate value = record.value();
            String registrationPlate = record.value().getRegPlate();
            /* System.out.println("Record registrationPlate " + registrationPlate);*/
            boolean isNoticePresent;
            if (UNIDENTIFIED.equals(record.value().getRegPlate())) {
                isNoticePresent = false;
            } else {
                isNoticePresent = forestInspector.callForestInspector(registrationPlate);
                limitRequests();
            }
            verifiedPlates.put(value, isNoticePresent);
        }
        return verifiedPlates;
    }

    private void limitRequests() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }
}