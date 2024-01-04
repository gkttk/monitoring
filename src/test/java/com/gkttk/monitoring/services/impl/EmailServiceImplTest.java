package com.gkttk.monitoring.services.impl;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;

import com.gkttk.monitoring.models.dtos.EmailNotificationDto;
import com.gkttk.monitoring.models.enums.Severity;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {
  private static final String DEFAULT_SUBJECT = "Monitoring information";
  private static final long NUMBER_OF_NOTIFICATIONS = 4;
  private static final String EMAIL_TO_SEND = "test@mail.com";
  private static final Date DATE_OF_NOTIFICATION = new Date();
  private static final Severity SEVERITY = Severity.WARNING;
  @Mock private JavaMailSender emailSender;

  @InjectMocks private EmailServiceImpl emailService;

  @Test
  public void testSendEmailShouldTriggerEmailSender() {
    // given
    EmailNotificationDto dto =
        EmailNotificationDto.builder()
            .emailToSend(EMAIL_TO_SEND)
            .numberOfNotifications(NUMBER_OF_NOTIFICATIONS)
            .date(DATE_OF_NOTIFICATION)
            .severity(SEVERITY)
            .build();

    SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
    expectedMailMessage.setTo(EMAIL_TO_SEND);
    expectedMailMessage.setSubject(DEFAULT_SUBJECT);
    // when
    emailService.sendEmail(dto);
    // then
    verify(emailSender).send(refEq(expectedMailMessage, "sentDate", "text"));
  }
}
