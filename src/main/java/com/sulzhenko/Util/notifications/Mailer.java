package com.sulzhenko.Util.notifications;

import com.sulzhenko.model.services.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
public class Mailer{
    private static final String FROM = "newemailforfp@gmail.com";
    private static final String PASSWORD = "jspimzkggethafoa";
    private static final Logger logger = LogManager.getLogger(Mailer.class);
    private Mailer() {
    }
    public static void send(String to, String sub, String msg){
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, PASSWORD);
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);
            //send message
            Transport.send(message);
            logger.info("message sent successfully");
        } catch (MessagingException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e);
        }
    }
}
