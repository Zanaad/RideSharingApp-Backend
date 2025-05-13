package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.CreateCompanyRequest;
import com.ridesharingappbackend.mappers.CompanyMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCompany_SavesAndReturnsCompany() {
        CreateCompanyRequest dto = new CreateCompanyRequest();
        Company company = new Company(); company.setId(1);

        when(companyMapper.fromCreateRequest(dto)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);

        Company result = companyService.createCompany(dto);

        assertEquals(1, result.getId());
    }

    @Test
    void getAllCompanies_ReturnsList() {
        Company company = new Company(); company.setId(1);

        when(companyRepository.findAll()).thenReturn(List.of(company));

        List<Company> result = companyService.getAllCompanies();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getCompanyById_WhenExists_ReturnsCompany() {
        Company company = new Company(); company.setId(1);

        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        Optional<Company> result = companyService.getCompanyById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void getCompanyById_WhenNotExists_ReturnsEmpty() {
        when(companyRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Company> result = companyService.getCompanyById(99);

        assertFalse(result.isPresent());
    }

    @Test
    void updateCompany_WhenExists_UpdatesAndReturnsCompany() {
        CreateCompanyRequest dto = new CreateCompanyRequest();
        Company existing = new Company(); existing.setId(1);

        when(companyRepository.findById(1)).thenReturn(Optional.of(existing));
        doNothing().when(companyMapper).updateCompanyFromRequest(dto, existing);
        when(companyRepository.save(existing)).thenReturn(existing);

        Company result = companyService.updateCompany(1, dto);

        assertEquals(1, result.getId());
        verify(companyMapper).updateCompanyFromRequest(dto, existing);
    }

    @Test
    void updateCompany_WhenNotExists_ThrowsException() {
        CreateCompanyRequest dto = new CreateCompanyRequest();

        when(companyRepository.findById(123)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> companyService.updateCompany(123, dto));

        assertEquals("Company not found", ex.getMessage());
    }
}
