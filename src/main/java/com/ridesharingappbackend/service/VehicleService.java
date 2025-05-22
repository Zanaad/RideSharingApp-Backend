package com.ridesharingappbackend.service;

import com.ridesharingappbackend.dto.VehicleDTO;
import com.ridesharingappbackend.mappers.VehicleMapper;
import com.ridesharingappbackend.models.Vehicle;
import com.ridesharingappbackend.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Shërbim që menaxhon logjikën e biznesit për entitetin {@link Vehicle} në aplikacionin e ndarjes së udhëtimeve.
 *
 * Ky shërbim ofron metoda për:
 * <ul>
 *   <li>Marrjen e të gjitha automjeteve që i përkasin një kompanie specifike</li>
 *   <li>Gjetjen e një automjeti sipas ID-së dhe kompanisë</li>
 *   <li>Gjetjen e automjetit për një shofer të caktuar brenda kompanisë</li>
 *   <li>Krijimin e një automjeti të ri</li>
 *   <li>Përditësimin e të dhënave të një automjeti ekzistues</li>
 *   <li>Fshirjen e një automjeti</li>
 * </ul>
 *
 * Përdor {@link VehicleRepository} për ndërveprim me shtresën e të dhënave dhe
 * {@link VehicleMapper} për transformim midis entiteteve dhe DTO-ve.
 *
 * Të gjitha operacionet janë të kufizuara brenda kontekstit të një kompanie përmes kontrollit të {@code companyId}.
 *
 * @author [Emri juaj]
 * @version 1.0
 * @since 2025-05-22
 */

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
