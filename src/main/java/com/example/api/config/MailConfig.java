package com.example.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.user}")
    private String user;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.transport.protocol}")
    private String protocol;
    @Value("${spring.mail.smtp.auth}")
    private String auth;
    @Value("${spring.mail.smtp.starttls.enable}")
    private String starttlsEnable;
    @Value("${spring.mail.smtp.starttls.required}")
    private String starttlsRequired;

    @Bean
    public JavaMailSender JavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setJavaMailProperties(configureMailSender());
        return mailSender;
    }

    private Properties configureMailSender() {
        Properties properties = new Properties();
        properties.put("mail.user", user);
        properties.put("mail.password", password);
        properties.put("mail.host", host);
        properties.put("mail.port", port);
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.starttls.required", starttlsRequired);
        return properties;
    }
}
