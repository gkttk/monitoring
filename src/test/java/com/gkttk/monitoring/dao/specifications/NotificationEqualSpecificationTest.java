package com.gkttk.monitoring.dao.specifications;

import static org.mockito.Mockito.*;

import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.models.entities.Notification;
import com.gkttk.monitoring.models.enums.Severity;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationEqualSpecificationTest {

  private static final long TEST_COMPONENT_ID = 1L;
  private static final Severity TEST_SEVERITY = Severity.ERROR;
  private static final String COMPONENT_ID_FIELD_NAME = "id";
  private static final String NOTIFICATION_COMPONENT_FIELD_NAME = "component";
  private static final String NOTIFICATION_SEVERITY_FIELD_NAME = "severity";

  @Mock private Root<Notification> rootMock;
  @Mock private CriteriaQuery<?> queryMock;
  @Mock private CriteriaBuilder criteriaBuilderMock;
  @Mock private Join joinMock;
  @Mock private Path pathMock;
  @Mock private Predicate predicateMock;

  @Test
  @SuppressWarnings("unchecked")
  public void testToPredicateShouldCreatePredicateWithComponentIdIfThisIsProvidedInFilter() {
    // given
    when(rootMock.join(NOTIFICATION_COMPONENT_FIELD_NAME)).thenReturn(joinMock);
    when(joinMock.get(COMPONENT_ID_FIELD_NAME)).thenReturn(pathMock);
    when(criteriaBuilderMock.equal(pathMock, TEST_COMPONENT_ID)).thenReturn(predicateMock);
    when(criteriaBuilderMock.and(predicateMock)).thenReturn(predicateMock);

    NotificationFilter filter = NotificationFilter.builder().componentId(TEST_COMPONENT_ID).build();
    NotificationEqualSpecification specification = new NotificationEqualSpecification(filter);
    // when
    specification.toPredicate(rootMock, queryMock, criteriaBuilderMock);
    // then
    verify(rootMock).join(NOTIFICATION_COMPONENT_FIELD_NAME);
    verify(joinMock).get(COMPONENT_ID_FIELD_NAME);
    verify(criteriaBuilderMock).equal(pathMock, TEST_COMPONENT_ID);
    verify(criteriaBuilderMock).and(predicateMock);
    verify(rootMock, never()).join(NOTIFICATION_SEVERITY_FIELD_NAME);
    verify(joinMock, never()).get(NOTIFICATION_SEVERITY_FIELD_NAME);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testToPredicateShouldCreatePredicateWithSeverityIfThisIsProvidedInFilter() {
    // given
    when(rootMock.get(NOTIFICATION_SEVERITY_FIELD_NAME)).thenReturn(pathMock);
    when(criteriaBuilderMock.equal(pathMock, TEST_SEVERITY)).thenReturn(predicateMock);
    when(criteriaBuilderMock.and(predicateMock)).thenReturn(predicateMock);

    NotificationFilter filter = NotificationFilter.builder().severity(Severity.ERROR).build();
    NotificationEqualSpecification specification = new NotificationEqualSpecification(filter);
    // when
    specification.toPredicate(rootMock, queryMock, criteriaBuilderMock);
    // then
    verify(rootMock).get(NOTIFICATION_SEVERITY_FIELD_NAME);
    verify(criteriaBuilderMock).equal(pathMock, TEST_SEVERITY);
    verify(criteriaBuilderMock).and(predicateMock);
    verify(rootMock, never()).join(NOTIFICATION_COMPONENT_FIELD_NAME);
    verify(joinMock, never()).get(NOTIFICATION_COMPONENT_FIELD_NAME);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void
      testToPredicateShouldCreatePredicateWithSeverityAndComponentIdIfTheyAreProvidedInFilter() {
    // given
    when(rootMock.join(NOTIFICATION_COMPONENT_FIELD_NAME)).thenReturn(joinMock);
    when(joinMock.get(COMPONENT_ID_FIELD_NAME)).thenReturn(pathMock);
    when(criteriaBuilderMock.equal(pathMock, TEST_COMPONENT_ID)).thenReturn(predicateMock);
    when(rootMock.get(NOTIFICATION_SEVERITY_FIELD_NAME)).thenReturn(pathMock);
    when(criteriaBuilderMock.equal(pathMock, TEST_SEVERITY)).thenReturn(predicateMock);

    NotificationFilter filter =
        NotificationFilter.builder()
            .componentId(TEST_COMPONENT_ID)
            .severity(Severity.ERROR)
            .build();
    NotificationEqualSpecification specification = new NotificationEqualSpecification(filter);
    // when
    specification.toPredicate(rootMock, queryMock, criteriaBuilderMock);
    // then
    verify(rootMock).join(NOTIFICATION_COMPONENT_FIELD_NAME);
    verify(joinMock).get(COMPONENT_ID_FIELD_NAME);
    verify(criteriaBuilderMock).equal(pathMock, TEST_COMPONENT_ID);

    verify(rootMock).get(NOTIFICATION_SEVERITY_FIELD_NAME);
    verify(criteriaBuilderMock).equal(pathMock, TEST_SEVERITY);
    verify(criteriaBuilderMock).and(new Predicate[] {predicateMock, predicateMock});
  }
}
