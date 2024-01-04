package com.gkttk.monitoring.services;

import com.gkttk.monitoring.models.dtos.EmailNotificationDto;

public interface EmailService {

  void sendEmail(EmailNotificationDto emailNotification);
}
