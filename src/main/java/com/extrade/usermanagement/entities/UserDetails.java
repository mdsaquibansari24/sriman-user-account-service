package com.extrade.usermanagement.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="user_details")
@GenericGenerator(name = "user_account_foreign_gen", strategy = "foreign",
        parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "userAccount")})
public class UserDetails {
    @Id
    @Column(name="user_account_id")
    @GeneratedValue(generator = "user_account_foreign_gen")
    private int userAccountId;
    private int experience;
    @Column(name = "highest_degree")
    private String highestDegree;
    @Column(name = "position_type")
    private String positionType;
    @Column(name = "hired_dt")
    private LocalDate hiredDate;
    @Column(name="terminated_dt")
    private LocalDate terminatedDate;
    @Column(name = "reason_for_termination")
    private String reasonForTermination;

    @OneToOne
    @PrimaryKeyJoinColumn
    private UserAccount userAccount;

    @Column(name = "last_modified_dt")
    private LocalDateTime lastModifiedDate;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
}
