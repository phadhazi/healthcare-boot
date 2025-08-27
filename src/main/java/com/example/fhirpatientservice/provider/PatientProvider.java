package com.example.fhirpatientservice.provider;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.example.fhirpatientservice.service.PatientService;
import com.example.fhirpatientservice.util.Transformer;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientProvider implements IResourceProvider {

    private final PatientService patientService;

    public PatientProvider(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public Class<Patient> getResourceType() {
        return Patient.class;
    }

    @Read
    public Patient getPatientById(@IdParam IdType id) {
        try {
        Long patientId = Long.valueOf(id.getIdPart());

        return patientService.getPatientById(patientId)
                .map(Transformer::toFhirPatient)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));
        }
        catch (ResourceNotFoundException e) {
            throw e;
        }
        catch(Exception e) {
            throw new InternalErrorException(e.getMessage(), e);
        }
    }

    @Search
    public List<Patient> searchPatientsByFamilyName(@OptionalParam(name = "family") StringParam familyName) {
        try {
            String family = familyName != null ? familyName.getValue() : "";
            return patientService.searchPatientsByFamilyName(family).stream()
                    .map(Transformer::toFhirPatient)
                    .collect(Collectors.toList());
        }
        catch(Exception e) {
            throw new InternalErrorException(e.getMessage(), e);
        }

    }

    @Create
    public MethodOutcome createPatient(RequestDetails requestDetails, @ResourceParam Patient fhirPatient) {
        try {
            com.example.fhirpatientservice.model.Patient patient = Transformer.fromFhirPatient(fhirPatient);
            com.example.fhirpatientservice.model.Patient saved = patientService.savePatient(patient);

            MethodOutcome outcome = new MethodOutcome();
            outcome.setId(new IdType("Patient", saved.getId()));
            outcome.setResource(Transformer.toFhirPatient(saved));
            outcome.setCreated(true);
            return outcome;
        }
        catch(Exception e) {
            throw new InternalErrorException(e.getMessage(), e);
        }
    }
}
