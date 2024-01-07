package org.F105540.queries;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.F105540.Apartment.ApartmentRepository;
import org.F105540.Building.Building;
import org.F105540.Building.BuildingRepository;
import org.F105540.Building.DtoBuilding;
import org.F105540.Employee.Employee;
import org.F105540.Employee.EmployeeRepository;
import org.F105540.Resident.DtoResident;
import org.F105540.Resident.ResidentRepository;
import org.F105540.company.CompanyRepository;
import org.F105540.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryService {

    private BuildingRepository buildingRepository;
    private EmployeeRepository employeeRepository;
    private ApartmentRepository apartmentRepository;
    private ResidentRepository residentRepository;
    private CompanyRepository companyRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    @Transactional
    public int getNumberOfBuildingsOfEmployee(int employeeId){

        return getListOfBuildingsOfEmployee(employeeId).size();
    }

    @Transactional
    public List<DtoBuilding> getListOfBuildingsOfEmployee(int employeeId){
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

        return buildingRepository.findBuildingsByEmployeeId(employeeId).stream()
                .map(building -> modelMapper.map(building, DtoBuilding.class))
                .toList();
    }

    @Transactional
    public int getNumberOfApartmentsInBuilding(int buildingId){

        return getListOfApartmentsInBuilding(buildingId).size();
    }

    @Transactional
    public List<DtoBuilding> getListOfApartmentsInBuilding(int buildingId){
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));

        return apartmentRepository.findListOfApartmentsInBuilding(buildingId).stream()
                .map(ap -> modelMapper.map(ap, DtoBuilding.class))
                .toList();
    }

    @Transactional
    public int getNumberOfResidentsInBuilding(int buildingId){

        return getListOfResidentsInBuilding(buildingId).size();
    }

    @Transactional
    public List<DtoResident> getListOfResidentsInBuilding(int buildingId){
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));

        return residentRepository.findResidentsInBuilding(buildingId).stream()
                .map(res -> modelMapper.map(res, DtoResident.class))
                .toList();
    }







}
