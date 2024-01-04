package com.gkttk.monitoring.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gkttk.monitoring.components.mappers.NotificationMapper;
import com.gkttk.monitoring.dao.repositories.NotificationRepository;
import com.gkttk.monitoring.dao.specifications.NotificationEqualSpecification;
import com.gkttk.monitoring.models.dtos.NotificationDto;
import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.models.entities.Notification;
import com.gkttk.monitoring.models.entities.SystemComponent;
import com.gkttk.monitoring.models.enums.Severity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

  private static final Long NOTIFICATION_ID = 1L;
  private static final LocalDateTime NOTIFICATION_TIMESTAMP = LocalDateTime.now();
  private static final String NOTIFICATION_DESCRIPTION = "Notification Description";
  private static final Severity NOTIFICATION_SEVERITY = Severity.WARNING;
  private static final Long COMPONENT_ID = 2L;
  private static final String COMPONENT_NAME = "Component name";
  private static final String COMPONENT_DESCRIPTION = "Component description";
  private static Notification notification;
  private static NotificationDto notificationDto;
  @Mock private NotificationRepository repositoryMock;
  @Mock private NotificationMapper mapperMock;
  @InjectMocks private NotificationServiceImpl service;

  @BeforeAll
  static void beforeAll() {
    SystemComponent component = new SystemComponent();
    component.setId(COMPONENT_ID);
    component.setName(COMPONENT_NAME);
    component.setDescription(COMPONENT_DESCRIPTION);

    notification = new Notification();
    notification.setId(NOTIFICATION_ID);
    notification.setDescription(NOTIFICATION_DESCRIPTION);
    notification.setComponent(component);
    notification.setTimestamp(NOTIFICATION_TIMESTAMP);
    notification.setSeverity(NOTIFICATION_SEVERITY);

    notificationDto = new NotificationDto();
    notificationDto.setId(NOTIFICATION_ID);
    notificationDto.setDescription(NOTIFICATION_DESCRIPTION);
    notificationDto.setComponentName(COMPONENT_NAME);
    notificationDto.setTimestamp(NOTIFICATION_TIMESTAMP);
    notificationDto.setSeverity(NOTIFICATION_SEVERITY);
  }

  @Test
  public void testGetByIdShouldReturnOptionalWithDtoIfItIsPresentedInTheDB() {
    // given
    when(repositoryMock.findById(NOTIFICATION_ID)).thenReturn(Optional.of(notification));
    when(mapperMock.mapToDto(notification)).thenReturn(notificationDto);
    // when
    Optional<NotificationDto> result = service.getById(NOTIFICATION_ID);
    // then
    assertTrue(result.isPresent());
    result.ifPresent(
        dto -> {
          assertEquals(NOTIFICATION_ID, dto.getId());
          assertEquals(NOTIFICATION_DESCRIPTION, dto.getDescription());
          assertEquals(COMPONENT_NAME, dto.getComponentName());
          assertEquals(NOTIFICATION_TIMESTAMP, dto.getTimestamp());
          assertEquals(NOTIFICATION_SEVERITY, dto.getSeverity());
        });

    verify(repositoryMock).findById(NOTIFICATION_ID);
    verify(mapperMock).mapToDto(notification);
  }

  @Test
  public void testGetByIdShouldReturnAnEmptyOptionalIfItIsNotPresentedInTheDB() {
    // given
    when(repositoryMock.findById(NOTIFICATION_ID)).thenReturn(Optional.empty());
    // when
    Optional<NotificationDto> result = service.getById(NOTIFICATION_ID);
    // then
    assertFalse(result.isPresent());
    verify(repositoryMock).findById(NOTIFICATION_ID);
  }

  @Test
  @SuppressWarnings("ConstantConditions")
  public void testGetAllCountShouldRequestAllValuesIfTheProvidedFilterIsNull() {
    // given
    NotificationFilter filter = null;
    List<Notification> expectedList = List.of(notification, notification);
    when(repositoryMock.findAll(null)).thenReturn(expectedList);
    when(mapperMock.mapToDto(notification)).thenReturn(notificationDto);
    // when
    List<NotificationDto> result = service.getAll(filter);
    // then
    assertEquals(expectedList.size(), result.size());
    verify(repositoryMock).findAll(null);
    verify(mapperMock, times(2)).mapToDto(notification);
  }

  @Test
  public void testGetAllCountShouldRequestValuesByProvidedFilterIfTheFilterIsNotNull() {
    // given
    NotificationFilter filter =
        NotificationFilter.builder()
            .severity(NOTIFICATION_SEVERITY)
            .componentId(NOTIFICATION_ID)
            .build();
    List<Notification> expectedList = List.of(notification);
    when(repositoryMock.findAll(any(NotificationEqualSpecification.class)))
        .thenReturn(expectedList);
    when(mapperMock.mapToDto(notification)).thenReturn(notificationDto);
    // when
    List<NotificationDto> result = service.getAll(filter);
    // then
    assertEquals(expectedList.size(), result.size());
    verify(repositoryMock).findAll(any(NotificationEqualSpecification.class));
    verify(mapperMock).mapToDto(notification);
  }

  @Test
  public void testCreateShouldRequestCreationOfEntity() {
    // given
    when(mapperMock.mapToEntity(notificationDto)).thenReturn(notification);
    when(repositoryMock.save(any(Notification.class))).thenReturn(notification);
    when(mapperMock.mapToDto(notification)).thenReturn(notificationDto);
    // when
    service.create(notificationDto);
    // then
    verify(mapperMock).mapToEntity(notificationDto);
    verify(repositoryMock).save(any(Notification.class));
    verify(mapperMock).mapToDto(notification);
  }

  @Test
  public void testDeleteByIdShouldRequestDeletionOfEntity() {
    // given
    doNothing().when(repositoryMock).deleteById(NOTIFICATION_ID);
    // when
    service.deleteById(NOTIFICATION_ID);
    // then
    verify(repositoryMock).deleteById(NOTIFICATION_ID);
  }
}
