package com.smart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender; // Spring iska bean khud banayega

    public boolean sendEmail(String message, String subject, String to) {
        try {
            SimpleMailMessage m = new SimpleMailMessage();
            m.setFrom("ssp123yyhhhgvv@gmail.com");
            m.setTo(to);
            m.setSubject(subject);
            m.setText(message);

            mailSender.send(m);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}