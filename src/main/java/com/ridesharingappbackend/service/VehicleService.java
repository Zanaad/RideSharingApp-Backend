package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.mappers.VehicleMapper;
import com.ridesharingappbackend.models.Vehicle;
import com.ridesharingappbackend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public List<VehicleDTO> getAllVehiclesByCompany(Integer companyId) {
        return vehicleRepository.findByCompanyId(companyId).stream()
                .map(vehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<VehicleDTO> getVehicleByIdAndCompanyId(Integer id, Integer companyId) {
        return vehicleRepository.findByIdAndCompanyId(id, companyId)
                .map(vehicleMapper::toDTO);
    }

    public Optional<VehicleDTO> getVehicleByDriverIdAndCompanyId(Integer driverId, Integer companyId) {
        return Optional.ofNullable(vehicleRepository.findByDriverId(driverId))
                .filter(vehicle -> vehicle.getCompany().getId().equals(companyId))
                .map(vehicleMapper::toDTO);
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    public VehicleDTO updateVehicle(Integer id, Integer companyId, VehicleDTO updatedDto) {
        return vehicleRepository.findByIdAndCompanyId(id, companyId)
                .map(existing -> {
                    vehicleMapper.updateVehicleFromDto(updatedDto, existing);
                    return vehicleMapper.toDTO(vehicleRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Vehicle not found or does not belong to the company"));
    }

    public boolean deleteVehicle(Integer id, Integer companyId) {
        return vehicleRepository.findByIdAndCompanyId(id, companyId)
                .map(vehicle -> {
                    vehicleRepository.delete(vehicle);
                    return true;
                })
                .orElse(false);
    }
}
