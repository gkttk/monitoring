package com.gkttk.monitoring.services.impl;

import com.gkttk.monitoring.models.dtos.EmailNotificationDto;
import com.gkttk.monitoring.services.EmailService;
import java.text.SimpleDateFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
  private static final String DEFAULT_SUBJECT = "Monitoring information";
  private final JavaMailSender emailSender;

  @Override
  public void sendEmail(EmailNotificationDto notification) {

    String body = createBody(notification);
    String emailToSend = notification.emailToSend();

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailToSend);
    message.setSubject(DEFAULT_SUBJECT);
    message.setText(body);
    emailSender.send(message);
    log.info("An email was sent to {}", emailToSend);
  }

  private String createBody(EmailNotificationDto notification) {
    String formattedDate = new SimpleDateFormat("dd-MM-yyyy, hh:mm:ss").format(notification.date());
    return String.format(
        "%d %s were spotted on %s",
        notification.numberOfNotifications(), notification.severity(), formattedDate);
  }
}
