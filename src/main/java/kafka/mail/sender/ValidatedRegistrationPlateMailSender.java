package kafka.mail.sender;

import kafka.registration.plate.IKafkaConstants;
import mail.EmailService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ValidatedRegistrationPlateMailSender {

    private final static String path_to_image = "/Users/ionutvacariu/PycharmProjects/woodDetectorPython/darknetW/detectedPlates/img";
    public static final String UNIDENTIFIED = "unidentified";

    @Autowired
    private EmailService emailService;

    public void sendMail(String registrationPlate) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();
        emailService.sendSimpleMessage("ionut.vacariu123@gmail.com",
                "Notice Warning",
                "The following car seems to not have a notice " + registrationPlate + "\n"
                        + "Current date " + localDate);
    }

    public void sendMail(String path_to_image, String path_to_large_image, String text) {
        try {
            emailService.sendMessageWithAttachment("ionut.vacariu123@gmail.com",
                    "Notice Warning",
                    text, path_to_image, path_to_large_image);
        } catch (MessagingException e) {
            System.out.println("!!!!±±±±±±±±±±± NU AM PUTUT TRIMITE EMAIL !!!!!!!±±±±±±±±±#########@@@@@@@@@@!!!!!!!");

        }
    }


    public void ReadValidatedRegAndSendMail(Consumer<String, ValidatedRegistrationPlate> consumer) {
        int noMessageFound = 0;
        while (true) {
            Map<String, Boolean> stringBooleanMap;
            ConsumerRecords<String, ValidatedRegistrationPlate> consumerRecords = consumer.poll(1000);
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
            consumerRecords.forEach(record ->
            {
                LocalDateTime localDateTime = LocalDateTime.now();
                LocalDate localDate = localDateTime.toLocalDate();
                ValidatedRegistrationPlate value = record.value();
                if (value != null && !value.getIsValid()) {
                    if (UNIDENTIFIED.equals(value.getRegistrationPlate().getRegPlate())) {
                        String path_to_attach = value.getRegistrationPlate().getImgPath();
                        String text = "The following car cannot be identified" + "\n"
                                + "Please visit  http://www.inspectorulpadurii.ro to check it";
                        sendMail(
                                "noResizedImage", getCompletePathToLargeFile(path_to_attach), text);
                    } else {
                        sendIdentifiedMail(value, localDate);
                    }
                }
            });
            consumer.commitAsync(); // sent ack to kafka ( to remove message from kafka )
        }
        consumer.close();
    }

    private void sendIdentifiedMail(ValidatedRegistrationPlate value, LocalDate localDate) {
        String path_to_attach = value.getRegistrationPlate().getImgPath();
        sendMail(
                getCompletePath(path_to_attach), getCompletePathToLargeFile(path_to_attach), "The following car seems to not have a notice " + value.getRegistrationPlate().getRegPlate() + "\n"
                        + "Current date " + localDate);
    }

    private String getCompletePath(String s) {
        String[] split = s.split("/");
        String s1 = split[split.length - 1];
        return path_to_image + "/" + s1;
    }

    private String getCompletePathToLargeFile(String s) {
        String[] split = s.split("/");
        String s1 = split[split.length - 1];

        int lastDot = s1.lastIndexOf(".");

        String file_name = s1.substring(0, lastDot);
        String file_extension = s1.substring(lastDot, s1.length());

        String large = file_name.concat("_large");
        String large_file_name = large.concat(file_extension);


        return path_to_image + "/" + large_file_name;
    }


}
