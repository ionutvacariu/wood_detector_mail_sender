package app;

import forestInspector.CallForestInspector;
import kafka.mail.sender.VerifiedRegistrationPlateProducer;
import kafka.registration.plate.ConsumerCreator;
import kafka.registration.plate.IKafkaConstants;
import kafka.registration.plate.KafkaRegistrationPlate;
import kafka.mail.sender.ValidatedRegistrationPlateMailSender;
import mail.EmailService;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {VerifiedRegistrationPlateProducer.class, CallForestInspector.class, KafkaRegistrationPlate.class, EmailService.class})
public class App extends SpringBootServletInitializer {

    @Autowired
    private KafkaRegistrationPlate runConsumer;

    @Autowired
    private ValidatedRegistrationPlateMailSender validatedRegistrationPlateMailSender;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }

    @Bean
    public void configureLog() {
        BasicConfigurator.configure();
    }

    @Bean
    public void startConsumer() {
        new Thread(() -> runConsumer.readKafkaRegistrationPlates(ConsumerCreator.createConsumer(IKafkaConstants.GROUP_ID_CONFIG))).start();
        System.out.println("blea o mers imediat");
    }

    @Bean
    public void startMailSenderKafkaConsumer() {
        new Thread(() -> {
            validatedRegistrationPlateMailSender.ReadValidatedRegAndSendMail(ConsumerCreator.createConsumerForEmailSender(IKafkaConstants.GROUP_ID_CONFIG));
        }).start();
        System.out.println("blea o mers imediat");

    }

    @Bean
    public JavaMailSender getJavaMailSender() {


        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("inspectorforest@gmail.com");
        mailSender.setPassword("Qwerty@123");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}