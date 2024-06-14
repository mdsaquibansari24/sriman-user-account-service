package com.extrade.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "role_cd")
    private String roleCode;
    private String description;
    @Column(name = "last_modified_dt")
    private LocalDateTime lastModifiedDate;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

}
