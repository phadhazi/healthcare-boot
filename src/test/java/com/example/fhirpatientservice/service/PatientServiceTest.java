package com.example.fhirpatientservice.service;

import com.example.fhirpatientservice.model.Patient;
import com.example.fhirpatientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setFamilyName("Smith");
        testPatient.setGivenName("John");
        testPatient.setBirthDate(LocalDate.of(1980, 5, 20));
    }

    @Test
    void testGetPatientById_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        Optional<Patient> result = patientService.getPatientById(1L);

        assertTrue(result.isPresent());
        assertEquals("Smith", result.get().getFamilyName());
    }

    @Test
    void testGetAllPatients() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(testPatient));

        List<Patient> result = patientService.getAllPatients();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getGivenName());
    }

    @Test
    void testSavePatient() {
        when(patientRepository.save(testPatient)).thenReturn(testPatient);

        Patient saved = patientService.savePatient(testPatient);

        assertNotNull(saved);
        verify(patientRepository, times(1)).save(testPatient);
    }

    @Test
    void testSearchPatientsByFamilyName_Valid() {
        when(patientRepository.findByFamilyNameIgnoreCaseContaining("smi"))
                .thenReturn(List.of(testPatient));

        List<Patient> results = patientService.searchPatientsByFamilyName("smi");

        assertEquals(1, results.size());
        assertEquals("Smith", results.get(0).getFamilyName());
    }

    @Test
    void testSearchPatientsByFamilyName_TooShort() {
        List<Patient> results = patientService.searchPatientsByFamilyName("ab");

        assertTrue(results.isEmpty());
        verify(patientRepository, never()).findByFamilyNameIgnoreCaseContaining(anyString());
    }
}
