package com.gricko.telegram.mail;

import com.gricko.telegram.model.User;
import com.gricko.telegram.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class NotificationService {

    private final UserService userService;
    private final JavaMailSender mailSender;

    public NotificationService(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Value("${bot.email.subject}")
    private String emailSubject;

    @Value("${bot.email.from}")
    private String emailFrom;

    @Value("${bot.email.to}")
    private String emailTo;

    @Scheduled(fixedRate = 10000)
    public void sendNewApplications(){
        List<User> users = userService.findNewUsers();
        if (users.size() == 0)
            return;

        StringBuilder sb = new StringBuilder();

        users.forEach(user ->
                sb.append("Phone: ")
                    .append(user.getPhone())
                    .append("\r\n")
                    .append("Email: ")
                    .append(user.getEmail())
                    .append("\r\n\r\n")
        );

        sendEmail(sb.toString());
    }

    private void sendEmail(String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailTo);
        message.setFrom(emailFrom);
        message.setSubject(emailSubject);
        message.setText(text);

        mailSender.send(message);
    }
}
