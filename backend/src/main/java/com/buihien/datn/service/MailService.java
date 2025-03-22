package com.buihien.datn.service;


import com.buihien.datn.exception.InvalidDataException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String emailFrom;
    @Value("${spring.mail.name}")
    private String name;
    @Value("${endpoint.confirmUser}")
    private String apiConfirmUser;

    public void sendConfirmLink(String emailTo, String username, String resetToken) throws MessagingException, UnsupportedEncodingException {
        if (emailTo == null || username == null) {
            throw new InvalidDataException("Invalid input for sending email");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();

        String linkConfirm = apiConfirmUser + "?resetToken=" + resetToken;

        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        properties.put("username", username);
        context.setVariables(properties);

        helper.setFrom(emailFrom, name);
        helper.setTo(emailTo);
        helper.setSubject("Rest password confirmation");
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);

        mailSender.send(message);
    }
}