package com.example.fhirpatientservice.provider;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.example.fhirpatientservice.service.OrganizationService;
import com.example.fhirpatientservice.util.Transformer;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrganizationProvider implements IResourceProvider {

    private final OrganizationService organizationService;

    public OrganizationProvider(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }

    @Read
    public Organization getOrganizationById(@IdParam IdType id) {
        try {
            Long orgId = Long.valueOf(id.getIdPart());

            return organizationService.getOrganizationById(orgId)
                    .map(Transformer::toFhirOrganization)
                    .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id " + orgId));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException("Unexpected error while retrieving Organization with ID " + id.getIdPart(), e);
        }
    }

    @Search
    public List<Organization> getAllOrganizations() {
        try {
            return organizationService.getAllOrganizations().stream()
                    .map(Transformer::toFhirOrganization)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new InternalErrorException("Unexpected error while retrieving all Organizations", e);
        }
    }

    @Create
    public MethodOutcome createOrganization(RequestDetails requestDetails, @ResourceParam Organization fhirOrg) {
        try {
            com.example.fhirpatientservice.model.Organization organization = Transformer.fromFhirOrganization(fhirOrg);
            com.example.fhirpatientservice.model.Organization saved = organizationService.saveOrganization(organization);

            MethodOutcome outcome = new MethodOutcome();
            outcome.setId(new IdType("Organization", saved.getId()));
            outcome.setResource(Transformer.toFhirOrganization(saved));
            outcome.setCreated(true);
            return outcome;
        } catch (Exception e) {
            throw new InternalErrorException("Unexpected error while creating Organization", e);
        }
    }
}
