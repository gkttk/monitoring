package com.gkttk.monitoring.models.dtos;

import com.gkttk.monitoring.models.enums.Severity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationFilter {

  private Long componentId;
  private Severity severity;
}
