package com.gkttk.monitoring.components;

import static org.mockito.Mockito.*;

import com.gkttk.monitoring.models.dtos.EmailNotificationDto;
import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.models.entities.SystemConfiguration;
import com.gkttk.monitoring.models.enums.Severity;
import com.gkttk.monitoring.services.NotificationService;
import com.gkttk.monitoring.services.SystemConfigurationService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith({MockitoExtension.class})
public class SeverityNotificationCheckSchedulerTest {

  private static final String EMAIL_CONFIG_KEY = "notification_email";
  private static final String EMAIL_CONFIG_VALUE = "email@email.com";
  private static final String THRESHOLD_CONFIG_KEY = "severity_email_threshold";
  private static final String THRESHOLD_CONFIG_VALUE = "4";
  private static final String SEVERITY_LEVEL_CONFIG_KEY = "severity_email_level";
  private static final String SEVERITY_LEVEL_CONFIG_VALUE = "WARNING";
  private static final List<String> KEYS =
      Arrays.asList(EMAIL_CONFIG_KEY, THRESHOLD_CONFIG_KEY, SEVERITY_LEVEL_CONFIG_KEY);
  private static Map<String, SystemConfiguration> expectedSystemConfigurationMap;
  private static NotificationFilter expectedNotificationFilter;

  @Value("${messaging.queue.name}")
  private String queueName;

  @Mock private SystemConfigurationService configurationServiceMock;
  @Mock private NotificationService notificationServiceMock;
  @Mock private JmsTemplate jmsTemplateMock;
  @InjectMocks private SeverityNotificationCheckScheduler scheduler;

  @BeforeAll
  static void beforeAll() {

    SystemConfiguration systemConfiguration1 = new SystemConfiguration();
    systemConfiguration1.setId(1L);
    systemConfiguration1.setKey(EMAIL_CONFIG_KEY);
    systemConfiguration1.setValue(EMAIL_CONFIG_VALUE);

    SystemConfiguration systemConfiguration2 = new SystemConfiguration();
    systemConfiguration2.setId(2L);
    systemConfiguration2.setKey(SEVERITY_LEVEL_CONFIG_KEY);
    systemConfiguration2.setValue(SEVERITY_LEVEL_CONFIG_VALUE);

    SystemConfiguration systemConfiguration3 = new SystemConfiguration();
    systemConfiguration3.setId(3L);
    systemConfiguration3.setKey(THRESHOLD_CONFIG_KEY);
    systemConfiguration3.setValue(THRESHOLD_CONFIG_VALUE);

    expectedSystemConfigurationMap =
        Stream.of(systemConfiguration1, systemConfiguration2, systemConfiguration3)
            .collect(Collectors.toMap(SystemConfiguration::getKey, Function.identity()));

    expectedNotificationFilter =
        NotificationFilter.builder()
            .severity(Severity.fromString(SEVERITY_LEVEL_CONFIG_VALUE))
            .build();
  }

  @BeforeEach
  void beforeEach() {
    ReflectionTestUtils.setField(scheduler, "queueName", queueName);
  }

  @Test
  public void testIfSchedulerSendAMessageWhenThresholdIsReached() {
    // given
    when(configurationServiceMock.getByKeys(KEYS)).thenReturn(expectedSystemConfigurationMap);
    when(notificationServiceMock.getAllCount(expectedNotificationFilter))
        .thenReturn(Long.parseLong(THRESHOLD_CONFIG_VALUE));
    doNothing()
        .when(jmsTemplateMock)
        .convertAndSend(eq(queueName), any(EmailNotificationDto.class));
    // when
    scheduler.checkForErrorsSeverity();
    // then
    verify(configurationServiceMock).getByKeys(KEYS);
    verify(notificationServiceMock).getAllCount(expectedNotificationFilter);
    verify(jmsTemplateMock).convertAndSend(eq(queueName), any(EmailNotificationDto.class));
  }

  @Test
  public void testIfSchedulerDoesNotSendAMessageWhenThresholdIsNotReached() {
    // given
    long expectedNumberOfNotifications = 2;
    when(configurationServiceMock.getByKeys(KEYS)).thenReturn(expectedSystemConfigurationMap);
    when(notificationServiceMock.getAllCount(expectedNotificationFilter))
        .thenReturn(expectedNumberOfNotifications);

    // when
    scheduler.checkForErrorsSeverity();
    // then
    verify(configurationServiceMock).getByKeys(KEYS);
    verify(notificationServiceMock).getAllCount(expectedNotificationFilter);
    verify(jmsTemplateMock, never()).convertAndSend(eq(queueName), any(EmailNotificationDto.class));
  }
}
