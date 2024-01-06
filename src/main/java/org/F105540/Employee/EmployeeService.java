package org.F105540.Employee;

import jakarta.transaction.Transactional;
import org.F105540.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final ModelMapper modelMapper = new ModelMapper();

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
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
    return modelMapper.map(employeeRepository.save(modelMapper.map(employee, Employee.class)), DtoEmployee.class);
  }

  @Transactional
  public DtoEmployee editEmployee(int id, Employee employee) {
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    Employee employeeToEdit = employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee", id));

    modelMapper.map(employee, employeeToEdit);

    Employee updatedEmployee = employeeRepository.save(employeeToEdit);
//    if (employee.getCompany() != null){
//      employee.getCompany().get
//    }
    //TODO CHECK IF THE EMPLOYEE GETS UPDATED IN THE COMPANY OR I NEED TO SAVE IT EVERYWHERE

    return modelMapper.map(updatedEmployee, DtoEmployee.class);
  }

  @Transactional
  public List<DtoEmployee> getAllByName(String name){
    return employeeRepository.findAllByName(name).stream()
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
    employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee", id));
    employeeRepository.deleteById(id);
  }

}
