package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.CreateCompanyRequest;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * CompanyController menaxhon operacionet CRUD të kompanive.
 * Përfshin krijimin, listimin, marrjen dhe përditësimin e kompanive,
 * si dhe verifikimin e ekzistencës së një kompanie në bazë të domain-it.
 *
 * Endpoint-et kryesore:
 * - POST /api/companies
 * - GET /api/companies
 * - GET /api/companies/{id}
 * - GET /api/companies/domain/{domain}
 * - PUT /api/companies/{id}
 */
@RestController
@RequestMapping("/api/companies")
@Tag(name = "Companies", description = "Operations related to company management")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * Krijon një kompani të re duke përdorur të dhënat nga `CreateCompanyRequest`.
     *
     * @param dto objekti DTO që përmban emrin, domain-in dhe të dhëna të tjera të kompanisë.
     * @return kompania e sapo krijuar.
     */
    @Operation(summary = "Create a new company")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company created successfully")
    })
    @PostMapping
    public ResponseEntity<Company> create(@RequestBody CreateCompanyRequest dto) {
        return ResponseEntity.ok(companyService.createCompany(dto));
    }

    /**
     * Kthen listën e të gjitha kompanive të regjistruara.
     *
     * @return lista e kompanive.
     */
    @Operation(summary = "Get all companies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of companies returned")
    })
    @GetMapping
    public ResponseEntity<List<Company>> getAll() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    /**
     * Gjen një kompani në bazë të ID-së së saj.
     *
     * @param id identifikuesi i kompanisë.
     * @return kompania nëse ekziston, përndryshe statusi 404 Not Found.
     */
    @Operation(summary = "Get a company by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company found and returned"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Integer id) {
        return companyService.getCompanyById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Kontrollon nëse ekziston një kompani me domain-in e dhënë.
     *
     * @param domain domain-i i kompanisë që do të kontrollohet.
     * @return statusi 200 OK nëse ekziston, ose 404 Not Found nëse jo.
     */
    @Operation(summary = "Check if a company exists by domain")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company with given domain exists"),
            @ApiResponse(responseCode = "404", description = "Company with given domain not found")
    })
    @GetMapping("/domain/{domain}")
    public ResponseEntity<Void> checkCompanyDomain(@PathVariable String domain) {
        Optional<Company> optional = companyService.getCompanyByDomain(domain);
        return optional.isPresent() ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Përditëson të dhënat e një kompanie ekzistuese.
     *
     * @param id  ID-ja e kompanisë që do të përditësohet.
     * @param dto të dhënat e reja për përditësim.
     * @return kompania e përditësuar.
     */
    @Operation(summary = "Update an existing company by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company updated successfully"),
            @ApiResponse(responseCode = "500", description = "Company not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable Integer id, @RequestBody CreateCompanyRequest dto) {
        return ResponseEntity.ok(companyService.updateCompany(id, dto));
    }
}
