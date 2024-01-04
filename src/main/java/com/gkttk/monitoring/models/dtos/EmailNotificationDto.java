package com.gkttk.monitoring.models.dtos;

import com.gkttk.monitoring.models.enums.Severity;
import lombok.Builder;

import java.util.Date;

@Builder
public record EmailNotificationDto(Severity severity, String emailToSend, long numberOfNotifications, Date date) {

}
