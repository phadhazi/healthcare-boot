package com.example.fhirpatientservice.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.example.fhirpatientservice.provider.OrganizationProvider;
import com.example.fhirpatientservice.provider.PatientProvider;
import jakarta.servlet.ServletException;

public class FhirServerConfiguration extends RestfulServer {

    private final PatientProvider patientProvider;
    private final OrganizationProvider organizationProvider;

    public FhirServerConfiguration(PatientProvider patientProvider, OrganizationProvider organizationProvider) {
        this.patientProvider = patientProvider;
        this.organizationProvider = organizationProvider;
    }

    @Override
    protected void initialize() throws ServletException {
        setFhirContext(FhirContext.forR4());
        registerProvider(patientProvider);
        registerProvider(organizationProvider);
        setDefaultPrettyPrint(true);
    }
}
