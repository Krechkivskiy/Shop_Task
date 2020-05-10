package com.productshop.productshop.service.impl;

import com.productshop.productshop.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Value("${server.url}")
    private String url;

    public MailServiceImpl() {
    }

    @Override
    public void send(String messageToSend, String token) {
        try {
            final String username = "test123test178@gmail.com";
            final String password = "Svistylin123";
            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true"); //TLS
            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("test123test178@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(messageToSend));
            message.setSubject("Testing Gmail TLS");
            message.setText(messageToSend + token);
            Transport.send(message);
        } catch (MessagingException e) {
        }
    }
}

