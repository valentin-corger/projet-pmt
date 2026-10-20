package com.pmt.iscod.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendTaskAssigned(String toEmail, String taskName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail);
            message.setSubject("Tâche assignée : " + taskName);
            message.setText("Bonjour,\n\nVous avez été assigné à la tâche \"" + taskName + "\".\n\nBonne journée,\nL'équipe PMT");
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println("Échec envoi email à " + toEmail + " : " + e.getMessage());
        }
    }
}
