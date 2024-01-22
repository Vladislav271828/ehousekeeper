package org.F105540.Building;

import jakarta.transaction.Transactional;
import org.F105540.Apartment.ApartmentRepository;
import org.F105540.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;

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
    building.setApartments(emptyList());
    building.setEmployee(null);
    if (building.getExpenses() == null) building.setExpenses(BigDecimal.ZERO);
    if (building.getExpensesPaid() == null) building.setExpensesPaid(BigDecimal.ZERO);
    return  modelMapper.map(buildingRepository.save(modelMapper.map(building, Building.class)), DtoBuilding.class);
  }

  @Transactional
  public DtoBuilding editBuilding(Integer buildingId, DtoBuilding building){
    building.setApartments(null);
    building.setEmployee(null);
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    Building existingBuilding = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));

    modelMapper.map(building, existingBuilding);

    return modelMapper.map(buildingRepository.save(existingBuilding), DtoBuilding.class);
  }


  @Transactional
  public void deleteBuilding(int id) {
    buildingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Building", id));
    buildingRepository.deleteById(id);
  }


}
