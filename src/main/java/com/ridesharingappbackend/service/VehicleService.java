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

    public List<VehicleDTO> getAllVehicles(Integer companyId) {
        if (companyId != null) {
            return getVehiclesByCompanyId(companyId);
        }
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleDTO> getVehiclesByCompanyId(Integer companyId) {
        return vehicleRepository.findByCompanyId(companyId).stream()
                .map(vehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<VehicleDTO> getVehicleById(Integer id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toDTO);
    }

    public Optional<VehicleDTO> getVehicleByIdAndCompanyId(Integer id, Integer companyId) {
        return vehicleRepository.findByIdAndCompanyId(id, companyId)
                .map(vehicleMapper::toDTO);
    }

    public VehicleDTO getVehicleByDriverId(Integer driverId) {
        Vehicle vehicle = vehicleRepository.findByDriverId(driverId);
        return vehicleMapper.toDTO(vehicle);
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    public VehicleDTO updateVehicle(Integer id, VehicleDTO updatedDto) {
        return vehicleRepository.findById(id)
                .map(existing -> {
                    vehicleMapper.updateVehicleFromDto(updatedDto, existing);
                    return vehicleMapper.toDTO(vehicleRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public boolean deleteVehicle(Integer id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByIdAndCompanyId(Integer id, Integer companyId) {
        return vehicleRepository.existsByIdAndCompanyId(id, companyId);
    }
}