package org.F105540.Employee;

import jakarta.transaction.Transactional;
import org.F105540.Building.BuildingRepository;
import org.F105540.company.Company;
import org.F105540.company.CompanyRepository;
import org.F105540.exceptions.EntityNotFoundException;
import org.F105540.exceptions.InvalidInputException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final CompanyRepository companyRepository;
  private final BuildingRepository buildingRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository, BuildingRepository buildingRepository) {
    this.employeeRepository = employeeRepository;
    this.companyRepository = companyRepository;
    this.buildingRepository = buildingRepository;
  }

  @Transactional
  public List<DtoEmployee> getAllEmployees() {
    return employeeRepository.findAll().stream()
            .map(employee -> modelMapper.map(employee, DtoEmployee.class))
            .toList();
  }

  @Transactional
  public DtoEmployee getEmployeeById(int id) {
    return modelMapper.map(employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee", id)), DtoEmployee.class);
  }

  @Transactional
  public DtoEmployee createEmployee(DtoEmployee employee) {
    employee.setCompany(null);
    employee.setBuildings(emptyList());
    if (employee.getSalaryPaid() == null) employee.setSalaryPaid(BigDecimal.ZERO);
    if (employee.getSalaryToBePaid() == null) employee.setSalaryToBePaid(BigDecimal.ZERO);
    return modelMapper.map(employeeRepository.save(modelMapper.map(employee, Employee.class)), DtoEmployee.class);
  }

  @Transactional
  public DtoEmployee editEmployee(int id, DtoEmployee employee) {
    employee.setCompany(null);
    employee.setBuildings(null);
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    Employee employeeToEdit = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee", id));

    modelMapper.map(employee, employeeToEdit);

    Employee updatedEmployee = employeeRepository.save(employeeToEdit);
//    if (employee.getCompany() != null){
//      employee.getCompany().get
//    }

    return modelMapper.map(updatedEmployee, DtoEmployee.class);
  }

  @Transactional
  public DtoEmployee addEmployeeToCompany(Integer employeeId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

    if (employee.getCompany() != null && !employee.getCompany().getId().equals(company.getId())) throw new RuntimeException("Employee already works for another company. Please remove them from the company first");
    if ((employee.getCompany() != null ? employee.getCompany().getId() : 0) == company.getId()) throw new RuntimeException("Employee already works for this company");

    employee.setCompany(company);

    return modelMapper.map(employeeRepository.save(employee), DtoEmployee.class);
  }

  @Transactional
  public DtoEmployee removeEmployeeFromCompany(Integer employeeId, Integer companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee", employeeId));

    if (employee.getCompany() == null || !Objects.equals(employee.getCompany().getId(), company.getId())) throw new InvalidInputException("Employee does not work for this company");

    employee.getBuildings().forEach(building -> {
      Employee newEmployee = employeeRepository.findEmployeeWithLeastBuildingsBelongingToCompany(companyId, employeeId);
      if (newEmployee == null) throw new RuntimeException("The company doesn't have any free employees :(");
      building.setEmployee(newEmployee);
      employee.setCompany(null);

      buildingRepository.save(building);
    });

    return modelMapper.map(employeeRepository.save(employee), DtoEmployee.class);
  }

  @Transactional
  public List<DtoEmployee> getAllByName(String name, int companyId){
    Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new EntityNotFoundException("Company", companyId));

    return employeeRepository.findAllByNameAndCompany(name, company).stream()
            .map(employee -> modelMapper.map(employee, DtoEmployee.class))
            .toList();
  }

  @Transactional
  public List<DtoEmployee> getEmployeesWithBuildingsMoreOrLessThanOfCompany(boolean moreThen, int buildings, int companyId){
    if(moreThen) return employeeRepository.findEmployeesWithMoreBuildingsThan(buildings, companyId).stream()
            .map(employee -> modelMapper.map(employee, DtoEmployee.class))
            .toList();
    else return employeeRepository.findEmployeesWithLessBuildingsThan(buildings, companyId).stream()
            .map(employee -> modelMapper.map(employee, DtoEmployee.class))
            .toList();
  }

  @Transactional
  public void deleteEmployee(int id) {
    Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee", id));
    if (employee.getCompany() != null)  removeEmployeeFromCompany(id, employee.getCompany().getId());
    employeeRepository.deleteById(id);
  }

}
