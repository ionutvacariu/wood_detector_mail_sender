package kafka.registration.plate;

public interface IKafkaConstants {
    // public static String KAFKA_BROKERS = "172.16.233.98:9092";

    String KAFKA_BROKERS = "localhost:9092";
    Integer MESSAGE_COUNT = 1000;
    String CLIENT_ID = "client1";
    String RECOGNIZED_REGISTRATION_TOPIC_NAME = "registration_plate";
    String VALIDATED_REGISTRATION_TOPIC_NAME = "validated_registration_plate";

    String GROUP_ID_CONFIG = "consumerGroup1";
    String GROUP_ID_CONFIG_FOR_EMAIL_SENDER = "email_sender_consumer";

    Integer MAX_NO_MESSAGE_FOUND_COUNT = 1000000;
    String OFFSET_RESET_LATEST = "latest";
    String OFFSET_RESET_EARLIER = "earliest";
    Integer MAX_POLL_RECORDS = 3;
}