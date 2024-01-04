package com.gkttk.monitoring.configurations;

import com.gkttk.monitoring.properties.MailProperties;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {

  private final MailProperties properties;

  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(properties.host());
    mailSender.setPort(properties.port());
    mailSender.setUsername(properties.username());
    mailSender.setPassword(properties.password());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", properties.protocol());
    props.put("mail.smtp.auth", properties.auth());
    props.put("mail.smtp.starttls.enable", properties.starttlsEnabled());
    props.put("mail.debug", properties.debug());

    return mailSender;
  }
}
