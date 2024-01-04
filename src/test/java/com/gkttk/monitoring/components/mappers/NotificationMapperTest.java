package com.gkttk.monitoring.components.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.gkttk.monitoring.dao.repositories.SystemComponentRepository;
import com.gkttk.monitoring.models.dtos.NotificationDto;
import com.gkttk.monitoring.models.entities.Notification;
import com.gkttk.monitoring.models.entities.SystemComponent;
import com.gkttk.monitoring.models.enums.Severity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
public class NotificationMapperTest {

  private static final Long NOTIFICATION_ID = 1L;
  private static final LocalDateTime NOTIFICATION_TIMESTAMP = LocalDateTime.now();
  private static final String NOTIFICATION_DESCRIPTION = "Notification Description";
  private static final Severity NOTIFICATION_SEVERITY = Severity.WARNING;
  private static final Long COMPONENT_ID = 2L;
  private static final String COMPONENT_NAME = "Component name";
  private static final String COMPONENT_DESCRIPTION = "Component description";
  private static SystemComponent component;

  private final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);
  @Mock private SystemComponentRepository componentRepositoryMock;

  @BeforeAll
  static void beforeAll() {
    component = new SystemComponent();
    component.setId(COMPONENT_ID);
    component.setName(COMPONENT_NAME);
    component.setDescription(COMPONENT_DESCRIPTION);
  }

  @BeforeEach
  void beforeEach() {
    ReflectionTestUtils.setField(mapper, "componentRepository", componentRepositoryMock);
  }

  @Test
  public void testMapperTurnEntityIntoDto() {
    // given
    Notification notification = new Notification();
    notification.setId(NOTIFICATION_ID);
    notification.setDescription(NOTIFICATION_DESCRIPTION);
    notification.setComponent(component);
    notification.setTimestamp(NOTIFICATION_TIMESTAMP);
    notification.setSeverity(NOTIFICATION_SEVERITY);
    // when
    NotificationDto result = mapper.mapToDto(notification);
    // then
    assertEquals(notification.getId(), result.getId());
    assertEquals(notification.getDescription(), result.getDescription());
    assertEquals(notification.getComponent().getName(), result.getComponentName());
    assertEquals(notification.getSeverity(), result.getSeverity());
    assertEquals(notification.getTimestamp(), result.getTimestamp());
  }

  @Test
  public void testMapperTurnDtoIntoEntity() {
    // given
    when(componentRepositoryMock.findByName(anyString())).thenReturn(Optional.of(component));
    NotificationDto dto = new NotificationDto();
    dto.setId(NOTIFICATION_ID);
    dto.setDescription(NOTIFICATION_DESCRIPTION);
    dto.setComponentName(COMPONENT_NAME);
    dto.setTimestamp(NOTIFICATION_TIMESTAMP);
    dto.setSeverity(NOTIFICATION_SEVERITY);
    // when
    Notification result = mapper.mapToEntity(dto);
    // then
    assertEquals(dto.getId(), result.getId());
    assertEquals(dto.getDescription(), result.getDescription());
    assertEquals(component, result.getComponent());
    assertEquals(dto.getSeverity(), result.getSeverity());
    assertEquals(dto.getTimestamp(), result.getTimestamp());
  }
}
