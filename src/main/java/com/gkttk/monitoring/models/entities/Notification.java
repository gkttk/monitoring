package com.gkttk.monitoring.models.entities;


import com.gkttk.monitoring.models.enums.Severity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "component_id")
    private SystemComponent component;

    private String description;

    @Enumerated(EnumType.STRING)
    private Severity severity;


}
