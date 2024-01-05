package org.F105540.Building;

import jakarta.transaction.Transactional;
import org.F105540.Apartment.Apartment;
import org.F105540.Apartment.ApartmentRepository;
import org.F105540.Apartment.DtoApartment;
import org.F105540.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {

  private final BuildingRepository buildingRepository;
  private final ApartmentRepository apartmentRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  public BuildingService(BuildingRepository buildingRepository, ApartmentRepository apartmentRepository) {
    this.buildingRepository = buildingRepository;
    this.apartmentRepository = apartmentRepository;
  }

  @Transactional
  public List<DtoBuilding> getAllBuildings() {
    return buildingRepository.findAll().stream()
            .map(building -> modelMapper.map(building, DtoBuilding.class))
            .toList();
  }

  @Transactional
  public DtoBuilding getBuildingById(int id) {
    return modelMapper.map(buildingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Building", id)), DtoBuilding.class);
  }

  @Transactional
  public DtoBuilding createBuilding(DtoBuilding building) {
    return  modelMapper.map(buildingRepository.save(modelMapper.map(building, Building.class)), DtoBuilding.class);
  }

  @Transactional
  public DtoBuilding editBuilding(Integer buildingId, DtoBuilding building){
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    Building existingBuilding = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));

    modelMapper.map(building, existingBuilding);

    Building updatedBuilding = buildingRepository.save(existingBuilding);

    return modelMapper.map(updatedBuilding, DtoBuilding.class);
  }

  @Transactional
  public DtoApartment addApartmentToBuilding(DtoApartment apartment, Integer buildingId, Integer floor) {
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));
    if (floor > building.getNumberOfFloors()) {
        throw new IllegalArgumentException("floor number can't be bigger than " + building.getNumberOfFloors());
    }
    Apartment newApartment = modelMapper.map(apartment, Apartment.class);
    newApartment.setFloor(floor);
    newApartment.setBuilding(building);
    apartmentRepository.save(newApartment);
    Apartment savedApartment = building.getApartments().stream()
            .filter(ap -> ap.getId().equals(newApartment.getId()))
            .findFirst()
            .orElse(null);
    return modelMapper.map(savedApartment, DtoApartment.class);
  }

  @Transactional
  public void deleteBuilding(int id) {
    buildingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Building", id));
    buildingRepository.deleteById(id);
  }


}
