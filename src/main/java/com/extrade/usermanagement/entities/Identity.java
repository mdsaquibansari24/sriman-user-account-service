package com.extrade.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "identity")
@Data
public class Identity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_identity_id")
    private int userIdentityId;
    @Column(name = "identity_number")
    private String identityNo;
    @Column(name = "issued_authority")
    private String issuedAuthority;
    @Column(name = "expiry_year")
    private int expiryYear;
    @Column(name = "expiry_month")
    private int expiryMonth;
    @Column(name = "last_modified_dt")
    private LocalDateTime lastModifiedDate;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    @Column(name = "identity_file_access_uri")
    private String identityFileUri;

    @ManyToOne
    @JoinColumn(name = "identity_type_id")
    private IdentityType identityType;
}
