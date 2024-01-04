package com.gkttk.monitoring.components;

import com.gkttk.monitoring.models.dtos.EmailNotificationDto;
import com.gkttk.monitoring.services.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailSender {

    private final EmailService emailService;

  @JmsListener(destination = "${messaging.queue.name}", containerFactory = "myFactory")
  public void listenMessage(EmailNotificationDto emailNotification) {

        emailService.sendEmail(emailNotification);
    }

}
