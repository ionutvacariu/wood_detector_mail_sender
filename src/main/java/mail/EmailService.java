package mail;

import javax.mail.MessagingException;

public interface EmailService {

    public void sendSimpleMessage(String to, String subject, String text);

    public void sendMessageWithAttachment(
            String to, String subject, String text, String pathToAttachment, String large_file) throws MessagingException;
}