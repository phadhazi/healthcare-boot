package com.example.fhirpatientservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String givenName;
    private String familyName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization managingOrganization;

    public enum Gender {
        MALE,
        FEMALE,
        OTHER,
        UNKNOWN
    }
}
