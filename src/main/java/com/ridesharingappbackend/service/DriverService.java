package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.DriverDTO;
import com.ridesharingappbackend.mappers.DriverMapper;
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


    public Optional<DriverDTO> getDriverById(Integer id) {
        return driverRepository.findById(id).map(driverMapper::toDTO);
    }

    public DriverDTO createDriver(DriverDTO dto) {
        Driver driver = driverMapper.toEntity(dto);
        return driverMapper.toDTO(driverRepository.save(driver));
    }

    public DriverDTO updateDriver(Integer id, DriverDTO updatedDto) {
        return driverRepository.findById(id)
                .map(existing -> {
                    driverMapper.updateDriverFromDto(updatedDto, existing);
                    return driverMapper.toDTO(driverRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Driver not found"));
    }

    public void deleteDriver(Integer id) {
        driverRepository.deleteById(id);
    }
}