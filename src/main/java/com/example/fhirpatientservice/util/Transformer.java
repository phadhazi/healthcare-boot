package com.example.fhirpatientservice.util;

import com.example.fhirpatientservice.model.Organization;
import com.example.fhirpatientservice.model.Patient;
import org.hl7.fhir.r4.model.*;

public class Transformer {

    public static org.hl7.fhir.r4.model.Patient toFhirPatient(Patient patient) {
        org.hl7.fhir.r4.model.Patient fhirPatient = new org.hl7.fhir.r4.model.Patient();

        fhirPatient.setId(new IdType("Patient", patient.getId().toString()));

        HumanName name = new HumanName();
        name.setFamily(patient.getFamilyName());
        name.addGiven(patient.getGivenName());
        fhirPatient.addName(name);

        fhirPatient.setGender(patient.getGender() != null
                ? Enumerations.AdministrativeGender.fromCode(patient.getGender().toString().toLowerCase())
                : null);

        if (patient.getBirthDate() != null) {
            fhirPatient.setBirthDate(DateUtil.toDate(patient.getBirthDate()));
        }

        return fhirPatient;
    }

    public static Patient fromFhirPatient(org.hl7.fhir.r4.model.Patient fhirPatient) {
        Patient patient = new Patient();

        if (fhirPatient.hasName()) {
            HumanName name = fhirPatient.getNameFirstRep();
            patient.setFamilyName(name.getFamily());
            if (!name.getGiven().isEmpty()) {
                patient.setGivenName(name.getGiven().get(0).getValue());
            }
        }

        if (fhirPatient.hasGender()) {
            patient.setGender(Patient.Gender.valueOf(
                    fhirPatient.getGender().toCode().toUpperCase()
            ));
        }

        if (fhirPatient.hasBirthDate()) {
            patient.setBirthDate(DateUtil.toLocalDate(fhirPatient.getBirthDate()));
        }

        return patient;
    }

    public static org.hl7.fhir.r4.model.Organization toFhirOrganization(com.example.fhirpatientservice.model.Organization org) {
        org.hl7.fhir.r4.model.Organization fhirOrg = new org.hl7.fhir.r4.model.Organization();
        fhirOrg.setId(new IdType("Organization", org.getId().toString()));
        fhirOrg.setName(org.getName());
        return fhirOrg;
    }

    public static com.example.fhirpatientservice.model.Organization fromFhirOrganization(org.hl7.fhir.r4.model.Organization fhirOrg) {
        com.example.fhirpatientservice.model.Organization org = new com.example.fhirpatientservice.model.Organization();
        org.setName(fhirOrg.getName());
        return org;
    }
}
