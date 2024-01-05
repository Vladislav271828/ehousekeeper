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

import java.util.List;

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
    return modelMapper.map(companyRepository.save(modelMapper.map(company, Company.class)), DtoCompany.class);
  }

  @Transactional
  public DtoCompany editCompany(int id, Company company) {
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    Company companyToEdit = companyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Company", id));

    modelMapper.map(company, companyToEdit);

    Company updatedCompany = companyRepository.save(companyToEdit);

    return modelMapper.map(updatedCompany, DtoCompany.class);
  }

  @Transactional
  public DtoCompany addEmployeeToCompany(Integer employeeId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

    if (employee.getCompany() != null && employee.getCompany().getId() != company.getId()) throw new RuntimeException("Employee already works for another company. Please remove them from the company first");
    if ((employee.getCompany() != null ? employee.getCompany().getId() : 0) == company.getId()) throw new RuntimeException("Employee already works for this company");

    employee.setCompany(company);
    employeeRepository.save(employee);

    return modelMapper.map(companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId)), DtoCompany.class);
  }

  @Transactional
  public DtoCompany removeEmployeeFromCompany(Integer employeeId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

    if (employee.getCompany() == null || employee.getCompany().getId() != company.getId()) throw new InvalidInputException("Employee does not work for this company");

    //employee.setCompany(null);
    //employeeRepository.save(employee);
    System.out.println(employee + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

    employee.getBuildings().forEach(building -> {

      Employee newEmployee = employeeRepository.findEmployeeWithLeastBuildingsBelongingToCompany(companyId, employeeId);
      if (newEmployee == null) throw new RuntimeException("The company doesn't have any free employees :(");
      System.out.println(newEmployee + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

      building.setCompany(company);
      building.setEmployee(newEmployee);
      employee.getBuildings().add(building);
      employeeRepository.save(employee);
      buildingRepository.save(building);
    });

    return modelMapper.map( companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId)), DtoCompany.class);
  }

  @Transactional
  public DtoCompany addBuildingToCompany(Integer buildingId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));

    if (building.getCompany() != null && building.getCompany().getId() != company.getId()) throw new InvalidInputException("Building already belongs to another company. Please remove it from the company first");
    if ((building.getCompany() != null ? building.getCompany().getId() : 0) == company.getId()) throw new InvalidInputException("Building already belongs to this company");

    Employee employee = employeeRepository.findEmployeeWithLeastBuildingsBelongingToCompany(companyId);
    if (employee == null) throw new RuntimeException("The company doesn't have any free employees :(");

    building.setCompany(company);
    building.setEmployee(employee);
    employee.getBuildings().add(building);
    employeeRepository.save(employee);
    buildingRepository.save(building);

    return modelMapper.map(companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId)), DtoCompany.class);

  }


  @Transactional
  public DtoCompany removeBuildingFromCompany(Integer buildingId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));

    if (building.getCompany() == null || building.getCompany().getId() != company.getId()) throw new InvalidInputException("Building is not assigned to this company");

    building.setCompany(null);
    building.setEmployee(null);
    buildingRepository.save(building);

    return modelMapper.map(companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId)), DtoCompany.class);

  }

  @Transactional
  public DtoBuilding assignBuildingToEmployee(Integer buildingId, Integer employeeId){
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));
    if (building.getCompany() == null || building.getCompany().getId() != employee.getCompany().getId()) throw new InvalidInputException("Building is not assigned to this company");
    if (building.getEmployee().getId() == employee.getId()) throw new InvalidInputException("This building is already assigned to this employee");

    building.setEmployee(employee);

    return modelMapper.map(buildingRepository.save(building), DtoBuilding.class);
  }

  @Transactional
  public DtoBuilding revokeBuildingFromEmployee(Integer buildingId, Integer employeeId){
    Building building = buildingRepository.findById(buildingId)
            .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

    if (building.getCompany() == null || building.getCompany().getId() != employee.getCompany().getId()) throw new InvalidInputException("Building is not assigned to this company");
    if(building.getEmployee().getId() != employee.getId()) throw new InvalidInputException("This building is assigned to employee with id " + building.getEmployee().getId());
    building.setEmployee(null);

    return modelMapper.map(buildingRepository.save(building), DtoBuilding.class);
  }

  @Transactional
  public void deleteCompany(int id) {
    companyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Company", id));
    companyRepository.deleteById(id);
  }
}
