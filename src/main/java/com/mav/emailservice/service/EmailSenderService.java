package com.mav.emailservice.service;

import com.mav.emailservice.exceptions.EmailSendingException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {


    private final JavaMailSender mailSender;

    // Constructor to inject JavaMailSender
    public EmailSenderService(JavaMailSender mailSender) throws RuntimeException{
        this.mailSender = mailSender;
    }

    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates that the text is HTML

            mailSender.send(message);
        } catch (MessagingException e) {
        throw new EmailSendingException("Failed to send email", e);
    }
    }

}