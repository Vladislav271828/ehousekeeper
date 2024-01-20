package org.F105540.company;

import jakarta.transaction.Transactional;
import org.F105540.Building.Building;
import org.F105540.Building.BuildingRepository;
import org.F105540.Building.DtoBuilding;
import org.F105540.Employee.Employee;
import org.F105540.Employee.EmployeeRepository;
import org.F105540.exceptions.EntityNotFoundException;
import org.F105540.exceptions.InvalidInputException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

@Service
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final EmployeeRepository employeeRepository;
  private final BuildingRepository buildingRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository, BuildingRepository buildingRepository) {
    this.companyRepository = companyRepository;
    this.employeeRepository = employeeRepository;
    this.buildingRepository = buildingRepository;
  }

  @Transactional
  public List<DtoCompany> getAllCompanies() {
    return companyRepository.findAll().stream()
            .map(company -> modelMapper.map(company, DtoCompany.class))
            .toList();
  }

  @Transactional
  public DtoCompany getCompanyById(int id) {
    return modelMapper.map(companyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Company", id)), DtoCompany.class);
  }

  @Transactional
  public DtoCompany createCompany(DtoCompany company) {
    company.setEmployees(emptyList());
    if(company.getIncome() == null) company.setIncome(BigDecimal.ZERO);
    return modelMapper.map(companyRepository.save(modelMapper.map(company, Company.class)), DtoCompany.class);
  }

  @Transactional
  public DtoCompany editCompany(int id, DtoCompany company) {
    company.setEmployees(null);
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    Company companyToEdit = companyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Company", id));

    modelMapper.map(company, companyToEdit);

    return modelMapper.map(companyRepository.save(companyToEdit), DtoCompany.class);
  }

  @Transactional
  public DtoBuilding addBuildingToCompany(Integer buildingId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));

    if (building.getEmployee() == null) {

      Employee employee = employeeRepository.findEmployeeWithLeastBuildingsBelongingToCompany(companyId);
      if (employee == null) throw new RuntimeException("The company doesn't have any free employees :(");

      building.setEmployee(employee);

      return modelMapper.map(buildingRepository.save(building), DtoBuilding.class);
    }

    else {
      if (!building.getEmployee().getCompany().getId().equals(company.getId())) throw new InvalidInputException("Building already belongs to another company. Please remove it from the company first");
      else throw new InvalidInputException("Building already belongs to this company");
    }
  }


  @Transactional
  public DtoCompany removeBuildingFromCompany(Integer buildingId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));
    if (building.getEmployee() == null || building.getEmployee().getCompany() == null || !Objects.equals(building.getEmployee().getCompany().getId(), company.getId())) throw new InvalidInputException("Building is not assigned to this company");

    building.setEmployee(null);


    return modelMapper.map(buildingRepository.save(building), DtoCompany.class);

  }

  @Transactional
  public DtoBuilding assignBuildingToEmployee(Integer buildingId, Integer employeeId){
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));
    if (building.getEmployee() == null || building.getEmployee().getCompany() == null || !Objects.equals(building.getEmployee().getCompany().getId(), employee.getCompany().getId())) throw new InvalidInputException("Building is not assigned to this company");
    if (Objects.equals(building.getEmployee().getId(), employee.getId())) throw new InvalidInputException("This building is already assigned to this employee");

    building.setEmployee(employee);
    employee.getBuildings().add(building);
    buildingRepository.save(building);
    employeeRepository.save(employee);

    return modelMapper.map(buildingRepository.save(building), DtoBuilding.class);
  }

//  @Transactional
//  public DtoBuilding revokeBuildingFromEmployee(Integer buildingId, Integer employeeId){
//    Building building = buildingRepository.findById(buildingId)
//            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));
//    Employee employee = employeeRepository.findById(employeeId)
//            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));
//
//    if (building.getEmployee().getCompany() == null || !building.getEmployee().getCompany().getId().equals(employee.getCompany().getId())) throw new InvalidInputException("Building is not assigned to this company");
//    if(!building.getEmployee().getId().equals(employee.getId())) throw new InvalidInputException("This building is assigned to employee with id " + building.getEmployee().getId());
//    building.setEmployee(null);
//    employee.getBuildings().remove(building);
//    buildingRepository.save(building);
//    employeeRepository.save(employee);
//
//    return modelMapper.map(buildingRepository.save(building), DtoBuilding.class);
//  }

  @Transactional
  public List<DtoCompany> getAllByOrderByIncomeDesc(){
    return companyRepository.findAllByOrderByIncomeDesc().stream()
            .map(company -> modelMapper.map(company, DtoCompany.class))
            .toList();
  }



  @Transactional
  public void deleteCompany(int id) {
    companyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Company", id));
    companyRepository.deleteById(id);
  }
}
