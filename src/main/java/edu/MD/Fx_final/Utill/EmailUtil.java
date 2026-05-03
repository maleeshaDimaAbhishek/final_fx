package edu.MD.Fx_final.Utill;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String FALLBACK_FROM_EMAIL = "maleeshadimal20@gmail.com";
    private static final String FALLBACK_PASSWORD = "oiys xvdl rukr rewy";

    public static boolean sendEmail(String toEmail, String subject, String messageText) {
        if (toEmail == null || toEmail.isBlank()) {
            return false;
        }

        final String fromEmail = System.getenv().getOrDefault("BOOK_BORROW_EMAIL", FALLBACK_FROM_EMAIL);
        final String password = System.getenv().getOrDefault("BOOK_BORROW_EMAIL_PASSWORD", FALLBACK_PASSWORD);

        // SMTP properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // Auth session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageText);

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}
