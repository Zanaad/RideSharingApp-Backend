package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.CreateCompanyRequest;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_success() {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setDomain("company.com");

        Company company = new Company();
        company.setId(1);
        company.setDomain("company.com");

        when(companyService.createCompany(request)).thenReturn(company);

        ResponseEntity<Company> response = companyController.create(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(company, response.getBody());
    }

    @Test
    void getAll_returnsListOfCompanies() {
        Company c1 = new Company();
        c1.setId(1);
        c1.setDomain("company1.com");

        Company c2 = new Company();
        c2.setId(2);
        c2.setDomain("company2.com");

        List<Company> companies = Arrays.asList(c1, c2);

        when(companyService.getAllCompanies()).thenReturn(companies);

        ResponseEntity<List<Company>> response = companyController.getAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(companies, response.getBody());
    }

    @Test
    void getById_existingId_returnsCompany() {
        Company company = new Company();
        company.setId(1);
        company.setDomain("company.com");

        when(companyService.getCompanyById(1)).thenReturn(Optional.of(company));

        ResponseEntity<Company> response = companyController.getById(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(company, response.getBody());
    }

    @Test
    void getById_nonExistingId_returnsNotFound() {
        when(companyService.getCompanyById(99)).thenReturn(Optional.empty());

        ResponseEntity<Company> response = companyController.getById(99);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void checkCompanyDomain_existingDomain_returnsOk() {
        Company company = new Company();
        company.setId(1);
        company.setDomain("company.com");

        when(companyService.getCompanyByDomain("company.com")).thenReturn(Optional.of(company));

        ResponseEntity<Void> response = companyController.checkCompanyDomain("company.com");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void checkCompanyDomain_nonExistingDomain_returnsNotFound() {
        when(companyService.getCompanyByDomain("unknown.com")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = companyController.checkCompanyDomain("unknown.com");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void update_existingCompany_returnsUpdatedCompany() {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setDomain("updated.com");

        Company updatedCompany = new Company();
        updatedCompany.setId(1);
        updatedCompany.setDomain("updated.com");

        when(companyService.updateCompany(1, request)).thenReturn(updatedCompany);

        ResponseEntity<Company> response = companyController.update(1, request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedCompany, response.getBody());
    }
}
