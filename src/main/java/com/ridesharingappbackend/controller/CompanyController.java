package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.CreateCompanyRequest;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> create(@RequestBody CreateCompanyRequest dto) {
        return ResponseEntity.ok(companyService.createCompany(dto));
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAll() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Integer id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable Integer id, @RequestBody CreateCompanyRequest dto) {
        return ResponseEntity.ok(companyService.updateCompany(id, dto));
    }
}
