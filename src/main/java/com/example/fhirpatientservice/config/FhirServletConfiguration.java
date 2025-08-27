package com.example.fhirpatientservice.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;
import com.example.fhirpatientservice.provider.OrganizationProvider;
import com.example.fhirpatientservice.provider.PatientProvider;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirServletConfiguration {

    @Bean
    public ServletRegistrationBean<RestfulServer> fhirServletRegistration(
            PatientProvider patientProvider,
            OrganizationProvider organizationProvider
    ) {
        return new ServletRegistrationBean<>(new RestfulServer(FhirContext.forR4()) {{
            registerProvider(patientProvider);
            registerProvider(organizationProvider);
            setDefaultPrettyPrint(true);
        }}, "/fhir/r4/*");
    }
}
