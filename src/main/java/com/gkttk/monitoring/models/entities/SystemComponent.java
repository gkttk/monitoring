package com.gkttk.monitoring.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "components")
@Data
@NoArgsConstructor
public class SystemComponent {

    public SystemComponent(Long id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

}
