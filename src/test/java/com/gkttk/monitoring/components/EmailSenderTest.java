package com.gkttk.monitoring.components;

import com.gkttk.monitoring.models.dtos.EmailNotificationDto;
import com.gkttk.monitoring.models.enums.Severity;
import com.gkttk.monitoring.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailSenderTest {

  @Mock private EmailService emailServiceMock;

  @InjectMocks private EmailSender sender;

  @Test
  public void testIfSenderSendMessages() {
    // given
    EmailNotificationDto emailNotificationDto =
        EmailNotificationDto.builder()
            .severity(Severity.ERROR)
            .emailToSend("email@test.com")
            .numberOfNotifications(4)
            .build();
    // when
    sender.listenMessage(emailNotificationDto);
    // then
    Mockito.verify(emailServiceMock).sendEmail(emailNotificationDto);
  }
}
