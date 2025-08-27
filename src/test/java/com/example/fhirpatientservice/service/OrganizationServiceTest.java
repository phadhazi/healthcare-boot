package com.example.fhirpatientservice.service;

import com.example.fhirpatientservice.model.Organization;
import com.example.fhirpatientservice.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService organizationService;

    private Organization testOrg;

    @BeforeEach
    void setUp() {
        testOrg = new Organization();
        testOrg.setId(1L);
        testOrg.setName("Test Organization");
    }

    @Test
    void testGetOrganizationById_Found() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(testOrg));

        Optional<Organization> result = organizationService.getOrganizationById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Organization", result.get().getName());
    }

    @Test
    void testGetAllOrganizations() {
        when(organizationRepository.findAll()).thenReturn(Arrays.asList(testOrg));

        var result = organizationService.getAllOrganizations();

        assertEquals(1, result.size());
    }

    @Test
    void testSaveOrganization() {
        when(organizationRepository.save(testOrg)).thenReturn(testOrg);

        var saved = organizationService.saveOrganization(testOrg);

        assertEquals("Test Organization", saved.getName());
        verify(organizationRepository, times(1)).save(testOrg);
    }
}
