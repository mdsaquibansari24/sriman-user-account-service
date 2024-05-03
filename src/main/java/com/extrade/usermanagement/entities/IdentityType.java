package com.extrade.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "identity_types")
@Data
public class IdentityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identity_type_id")
    private int identityTypeId;
    @Column(name = "identity_type")
    private String identityType;
    @Column(name = "display_nm")
    private String displayName;
    @Column(name = "last_modified_dt")
    private LocalDateTime lastModifiedDate;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}
