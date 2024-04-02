package com.example.carrentingapp.email.sender;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.message_history.EmailMessageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender javaMailSender;
    private final EmailMessageRepository emailMessageRepository;

    @Override
    @Async
    public void send(String to, String from, String subject, String email, EmailMessage.EmailMessageType type) {
        EmailMessage emailMessage = new EmailMessage(from, to, subject, email, type);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            javaMailSender.send(message);

            emailMessage.setStatus(EmailMessage.EmailMessageStatus.EMAIL_SENT);
            emailMessage.setSentAt(LocalDateTime.now());
        } catch (MessagingException e) {
            emailMessage.setStatus(EmailMessage.EmailMessageStatus.EMAIL_ERROR);
            emailMessage.setSentAt(null);

            LOGGER.error("Failed to send email, error details: ", e);
//            throw new IllegalStateException("Failed to send email");
        } finally {
            emailMessageRepository.save(emailMessage);
        }
    }
}
