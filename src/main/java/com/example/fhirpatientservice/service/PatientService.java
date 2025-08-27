package com.example.fhirpatientservice.service;

import com.example.fhirpatientservice.model.Patient;
import com.example.fhirpatientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> searchPatientsByFamilyName(String familyName) {
        if (familyName == null || familyName.length() < 3) {
            return Collections.emptyList();
        }
        return patientRepository.findByFamilyNameIgnoreCaseContaining(familyName);
    }
}
