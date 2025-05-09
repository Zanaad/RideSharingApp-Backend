package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.CreateCompanyRequest;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company createCompany(CreateCompanyRequest dto) {
        Company company = new Company();
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setContactEmail(dto.getContactEmail());
        company.setContactPhone(dto.getContactPhone());
        company.setDomain(dto.getDomain());
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
        company.setName(dto.getName());
        company.setDomain(dto.getDomain());
        return companyRepository.save(company);
    }
}
