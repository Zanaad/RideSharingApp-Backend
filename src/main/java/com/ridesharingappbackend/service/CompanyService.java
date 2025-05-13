package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.CreateCompanyRequest;
import com.ridesharingappbackend.mappers.CompanyMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public Company createCompany(CreateCompanyRequest dto) {
        Company company = companyMapper.fromCreateRequest(dto);
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Integer id) {
        return companyRepository.findById(id);
    }

    public Company updateCompany(Integer id, CreateCompanyRequest dto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        companyMapper.updateCompanyFromRequest(dto, company);
        return companyRepository.save(company);
    }
}
