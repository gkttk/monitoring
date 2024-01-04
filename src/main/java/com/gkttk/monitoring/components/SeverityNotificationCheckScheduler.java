package com.gkttk.monitoring.components;

import com.gkttk.monitoring.models.dtos.EmailNotificationDto;
import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.models.entities.SystemConfiguration;
import com.gkttk.monitoring.models.enums.Severity;
import com.gkttk.monitoring.services.NotificationService;
import com.gkttk.monitoring.services.SystemConfigurationService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SeverityNotificationCheckScheduler {

  private static final String EMAIL_CONFIG_KEY = "notification_email";
  private static final String THRESHOLD_CONFIG_KEY = "severity_email_threshold";
  private static final String SEVERITY_LEVEL_CONFIG_KEY = "severity_email_level";
  private static final List<String> KEYS =
      Arrays.asList(EMAIL_CONFIG_KEY, THRESHOLD_CONFIG_KEY, SEVERITY_LEVEL_CONFIG_KEY);
  private final SystemConfigurationService configurationService;
  private final NotificationService notificationService;
  private final JmsTemplate jmsTemplate;

  @Value("${messaging.queue.name}")
  private String queueName;

  @Scheduled(fixedDelayString = "${scheduler.configuration.fixed-delay}")
  public void checkForErrorsSeverity() {

    Map<String, SystemConfiguration> configMap = configurationService.getByKeys(KEYS);

    // get by severity level
    Severity severity = getSeverityEnumValue(configMap);
    NotificationFilter filter = NotificationFilter.builder().severity(severity).build();
    Long entitiesCount = notificationService.getAllCount(filter);

    // get threshold
    Integer threshold = getThreshold(configMap);

    if (threshold <= entitiesCount) {
      // send a message to queue
      String emailToSend = getEmailToSend(configMap);
      EmailNotificationDto notification =
          EmailNotificationDto.builder()
              .severity(severity)
              .emailToSend(emailToSend)
              .numberOfNotifications(entitiesCount)
              .date(new Date())
              .build();
      jmsTemplate.convertAndSend(queueName, notification);
    }
  }

  private Severity getSeverityEnumValue(Map<String, SystemConfiguration> configMap) {
    String severityString = configMap.get(SEVERITY_LEVEL_CONFIG_KEY).getValue();
    return Severity.valueOf(severityString.toUpperCase());
  }

  private Integer getThreshold(Map<String, SystemConfiguration> configMap) {
    String threshold = configMap.get(THRESHOLD_CONFIG_KEY).getValue();
    return Integer.parseInt(threshold);
  }

  private String getEmailToSend(Map<String, SystemConfiguration> configMap) {
    return configMap.get(EMAIL_CONFIG_KEY).getValue();
  }
}
