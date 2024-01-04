package com.gkttk.monitoring.dao.specifications;

import com.gkttk.monitoring.models.dtos.NotificationFilter;
import com.gkttk.monitoring.models.entities.Notification;
import com.gkttk.monitoring.models.enums.Severity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class NotificationEqualSpecification implements Specification<Notification> {

  private static final String COMPONENT_ID_FIELD_NAME = "id";
  private static final String NOTIFICATION_COMPONENT_FIELD_NAME = "component";
  private static final String NOTIFICATION_SEVERITY_FIELD_NAME = "severity";
  private final NotificationFilter filter;

  @Override
  public Predicate toPredicate(
      Root<Notification> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

    List<Predicate> predicates = new ArrayList<>();
    Long componentId = filter.getComponentId();
    if (componentId != null) {
      Predicate predicate =
          criteriaBuilder.equal(
              root.join(NOTIFICATION_COMPONENT_FIELD_NAME).get(COMPONENT_ID_FIELD_NAME),
              componentId);
      predicates.add(predicate);
    }

    Severity severity = filter.getSeverity();
    if (severity != null) {
      Predicate predicate =
          criteriaBuilder.equal(root.get(NOTIFICATION_SEVERITY_FIELD_NAME), severity);
      predicates.add(predicate);
    }

    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
  }
}
