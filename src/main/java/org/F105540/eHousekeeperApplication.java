package org.F105540;

import org.F105540.Apartment.Apartment;
import org.F105540.Apartment.ApartmentService;
import org.F105540.Apartment.DtoApartment;
import org.F105540.Building.Building;
import org.F105540.Building.BuildingService;
import org.F105540.Building.DtoBuilding;
import org.F105540.Employee.DtoEmployee;
import org.F105540.Employee.Employee;
import org.F105540.Employee.EmployeeService;
import org.F105540.Owner.DtoOwner;
import org.F105540.Owner.Owner;
import org.F105540.Owner.OwnerService;
import org.F105540.Resident.DtoResident;
import org.F105540.Resident.Resident;
import org.F105540.Resident.ResidentService;
import org.F105540.company.CompanyService;
import org.F105540.company.DtoCompany;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class eHousekeeperApplication {
    public static void main(String[] args) {
        SpringApplication.run(eHousekeeperApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(ApartmentService apartmentService, BuildingService buildingService, OwnerService ownerService, ResidentService residentService, CompanyService companyService, EmployeeService employeeService) {
        return args -> {

            DtoBuilding building = DtoBuilding.builder().numberOfFloors(10).address("adr").numberOfFloors(21).build();
            building = buildingService.createBuilding(building);
            DtoApartment apartment = buildingService.addApartmentToBuilding(DtoApartment.builder().area(12).build(), building.getId(), 2);
            DtoOwner owner = ownerService.createOwner(DtoOwner.builder().name("Ivan").build());
            apartment = apartmentService.addOwnerToApartment(owner.getId(), apartment.getId());
            DtoResident resident = new DtoResident("Ivan", 21, false);
            resident = residentService.createResident(resident);
            apartment = apartmentService.addResidentToApartment(resident.getId(), apartment.getId());
            //apartment = apartmentService.removeResidentFromApartment(resident.getId(), apartment.getId());
            DtoEmployee employee = DtoEmployee.builder().name("Dragan").build();
            DtoEmployee employee2 = DtoEmployee.builder().name("Petkan").build();
            employee = employeeService.createEmployee(employee);
            employee2 = employeeService.createEmployee(employee2);
            DtoCompany company = DtoCompany.builder().name("Kompaniq").build();
            company = companyService.createCompany(company);
            company = companyService.addEmployeeToCompany(employee.getId(), company.getId());
            company = companyService.addEmployeeToCompany(employee2.getId(), company.getId());
            //company = companyService.removeEmployeeFromCompany(employee.getId(), company.getId());
            company = companyService.addBuildingToCompany(building.getId(), company.getId());
            employee = employeeService.createEmployee(employee);
            employee2 = employeeService.createEmployee(employee2);
            System.out.println(employee.getBuildings());
            System.out.println(employee2.getBuildings());
//            System.out.println(company.getBuildings().get(0));
//            //companyService.removeBuildingFromCompany(building.getId(), company.getId());
//            //building = companyService.revokeBuildingFromEmployee(building.getId(), employee.getId());
//            System.out.println(company.getBuildings().get(0));
//            System.out.println(employee2.getBuildings());
//            company = companyService.removeEmployeeFromCompany(employee2.getId(), company.getId());
//            System.out.println(company.getBuildings().get(0));








//            // Create a new apartment
//            DtoApartment newApartment = DtoApartment.builder()
//                    .hasPet(true)
//                    .number(33)
//                    // Set apartment properties here
//                    .build();
//            //DtoApartment newApartment = new DtoApartment(null, null, null, null, null, null, null, null);
//            DtoApartment newApartment2 = new DtoApartment(null, null, null, null, 1, 1, 1, null);
//            // Save the new apartment
//            ////////DtoApartment savedApartment = apartmentService.createApartment(newApartment);
//            //System.out.println("Saved Apartment: " + savedApartment);
//
//            //savedApartment = apartmentService.editApartment(1, newApartment);
//            //savedApartment = apartmentService.editApartment(1, newApartment);
//            ////////savedApartment = apartmentService.editApartment(1, newApartment2);
//            //System.out.println("Saved Apartment: " + savedApartment);
//            System.out.println(apartmentService.getApartmentById(1));
//            System.out.println(apartmentService.getAllApartments());
//            //apartmentService.deleteApartment(22);
//            apartmentService.deleteApartment(1);



        };
    }

}