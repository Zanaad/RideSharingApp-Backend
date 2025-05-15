package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.mappers.DriverMapper;
import com.ridesharingappbackend.models.Company;
import com.ridesharingappbackend.models.Driver;
import com.ridesharingappbackend.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverService(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    public List<DriverDTO> getAllDriversByCompany(Integer companyId) {
        return driverRepository.findByCompanyId(companyId).stream()
                .map(driverMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<DriverDTO> getDriverById(Integer id, Integer companyId) {
        return driverRepository.findByIdAndCompanyId(id, companyId)
                .map(driverMapper::toDTO);
    }

    public DriverDTO createDriver(DriverDTO dto, Integer companyId) {
        Driver driver = driverMapper.toEntity(dto);
        Company company = new Company();
        company.setId(companyId);
        driver.setCompany(company);
        return driverMapper.toDTO(driverRepository.save(driver));
    }


    public DriverDTO updateDriver(Integer id, DriverDTO updatedDto, Integer companyId) {
        return driverRepository.findByIdAndCompanyId(id, companyId)
                .map(existing -> {
                    driverMapper.updateDriverFromDto(updatedDto, existing);
                    return driverMapper.toDTO(driverRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Driver not found or not part of this company"));
    }

    public void deleteDriver(Integer id, Integer companyId) {
        Driver driver = driverRepository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new RuntimeException("Driver not found or not part of this company"));
        driverRepository.delete(driver);
    }
}
