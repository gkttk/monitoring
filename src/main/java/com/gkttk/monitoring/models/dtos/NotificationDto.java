package com.gkttk.monitoring.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gkttk.monitoring.models.enums.Severity;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificationDto {

  private Long id;
  private LocalDateTime timestamp;

  @JsonProperty(value = "component_name")
  private String componentName;

  private String description;
  private Severity severity;
}
