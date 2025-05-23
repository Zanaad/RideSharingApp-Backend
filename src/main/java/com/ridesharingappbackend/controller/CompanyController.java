package com.ridesharingappbackend.controller;

import com.ridesharingappbackend.dto.CreateCompanyRequest;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.service.CompanyService;
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
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * Krijon një kompani të re duke përdorur të dhënat nga `CreateCompanyRequest`.
     *
     * @param dto objekti DTO që përmban emrin, domain-in dhe të dhëna të tjera të kompanisë.
     * @return kompania e sapo krijuar.
     */
    @PostMapping
    public ResponseEntity<Company> create(@RequestBody CreateCompanyRequest dto) {
        return ResponseEntity.ok(companyService.createCompany(dto));
    }

    /**
     * Kthen listën e të gjitha kompanive të regjistruara.
     *
     * @return lista e kompanive.
     */
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
    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable Integer id, @RequestBody CreateCompanyRequest dto) {
        return ResponseEntity.ok(companyService.updateCompany(id, dto));
    }
}
