package org.F105540;

import org.F105540.Apartment.ApartmentService;
import org.F105540.Apartment.DtoApartment;
import org.F105540.Building.BuildingService;
import org.F105540.Building.DtoBuilding;
import org.F105540.Employee.DtoEmployee;
import org.F105540.Employee.EmployeeService;
import org.F105540.Owner.DtoOwner;
import org.F105540.Owner.OwnerService;
import org.F105540.Resident.DtoResident;
import org.F105540.Resident.ResidentRepository;
import org.F105540.Resident.ResidentService;
import org.F105540.company.CompanyService;
import org.F105540.company.DtoCompany;
import org.F105540.queries.QueryService;
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
    public CommandLineRunner demo(ApartmentService apartmentService, BuildingService buildingService, OwnerService ownerService, ResidentService residentService, CompanyService companyService, EmployeeService employeeService, QueryService queryService) {
        return args -> {

            System.out.println("Създаване на служители");

            DtoEmployee employee = DtoEmployee.builder().name("Dragan").build();
            DtoEmployee employee2 = DtoEmployee.builder().name("Petkan").build();
            DtoEmployee employee3 = DtoEmployee.builder().name("Ivan").company(DtoCompany.builder().name("Kompaniq 2 BABYYY").build()).build();

            employee = employeeService.createEmployee(employee);
            employee2 = employeeService.createEmployee(employee2);
            employee3 = employeeService.createEmployee(employee3);

            System.out.println("Създаване на компания");

            DtoCompany company = DtoCompany.builder().name("Kompaniq").build();
            company = companyService.createCompany(company);
            DtoCompany company2 = DtoCompany.builder().name("Kompaniq dwe").build();
            company2 = companyService.createCompany(company);
            employee = employeeService.addEmployeeToCompany(employee.getId(), company.getId());
            employee2 = employeeService.addEmployeeToCompany(employee2.getId(), company.getId());
            employee3 = employeeService.addEmployeeToCompany(employee3.getId(), company.getId());

            System.out.println("Създаване на сгради, апартаменти и собственик");

            DtoBuilding building = buildingService.createBuilding(DtoBuilding.builder().address("ulitsa nomer edno").numberOfFloors(21).taxForPet(10.0).taxPerArea(1.0).taxPerElevatorPerson(100.0).build());
            DtoBuilding building2 = buildingService.createBuilding(DtoBuilding.builder().address("ulitsa nomer dwe").numberOfFloors(99).taxForPet(2.0).taxPerArea(3.0).taxPerElevatorPerson(5.0).build());
            DtoApartment apartment = apartmentService.createApartmentInBuilding(DtoApartment.builder().area(1000.0).hasPet(true).number(1).build(), building.getId());
            DtoApartment apartment2 = apartmentService.createApartmentInBuilding(DtoApartment.builder().area(10000.0).hasPet(false).number(2).build(), building.getId());
            DtoApartment apartment3 = apartmentService.createApartmentInBuilding(DtoApartment.builder().area(100000.0).hasPet(false).number(1).build(), building2.getId());

            DtoOwner owner = ownerService.createOwner(DtoOwner.builder().name("Maria").build());
            DtoOwner owner2 = ownerService.createOwner(DtoOwner.builder().name("Maggy").build());

            apartment = apartmentService.addOwnerToApartment(owner.getId(), apartment.getId());
            apartment2 = apartmentService.addOwnerToApartment(owner.getId(), apartment2.getId());
            apartment3 = apartmentService.addOwnerToApartment(owner2.getId(), apartment3.getId());

            System.out.println("Добавяне на сградите към компанията");

            companyService.addBuildingToCompany(building.getId(), company.getId());
            companyService.addBuildingToCompany(building2.getId(), company.getId());

            System.out.println("Създаване на жители и добавяне към апартаментите");

            DtoResident resident = new DtoResident(null, null, "Johan", 21, true);
            DtoResident resident2 = new DtoResident(null, null, "Johny", 2, true);
            DtoResident resident3 = new DtoResident(null, null, "Jonathan", 31, false);
            DtoResident resident4 = new DtoResident(null, null, "Joseph", 60, true);
            resident = residentService.createResident(resident);
            resident2 = residentService.createResident(resident2);
            resident3 = residentService.createResident(resident3);
            resident4 = residentService.createResident(resident4);

            apartment = apartmentService.addResidentToApartment(resident.getId(), apartment.getId());
            apartment = apartmentService.addResidentToApartment(resident2.getId(), apartment.getId());
            apartment2 = apartmentService.addResidentToApartment(resident3.getId(), apartment2.getId());
            apartment3 = apartmentService.addResidentToApartment(resident3.getId(), apartment3.getId());
            apartment3 = apartmentService.addResidentToApartment(resident4.getId(), apartment3.getId());

            System.out.println("редактиране на компания, служител, сграда, апартамент, живущ и собственик (точки 1, 2, 3, 4)");
            System.out.println("Също така въвеждане на такса в сградата (нищо че сме го направили по време на инициализация) (точка 6)");

            company = companyService.editCompany(company.getId(), DtoCompany.builder().name("Novo ime na kompaniq").build());
            employee = employeeService.editEmployee(employee.getId(), DtoEmployee.builder().name("Novo ime na slujitel").build());
            building = buildingService.editBuilding(building.getId(), DtoBuilding.builder().address("Nov adress").taxPerArea(1.0).taxForPet(1.0).taxPerElevatorPerson(1.0).build());
            apartment = apartmentService.editApartment(apartment.getId(), DtoApartment.builder().number(33).build());
            resident = residentService.editResident(resident.getId(), DtoResident.builder().name("Novo ime na jitel").build());
            owner = ownerService.editOwner(owner.getId(), DtoOwner.builder().name("Novo ime na sobstvenik").build());

            System.out.println("смяна на служител на сградата (точка 5)");

            building = companyService.assignBuildingToEmployee(building.getId(), employee2.getId());

            System.out.println("плащане на таксите на всички апартаменти (точка 7, 10)");

            apartment = apartmentService.payTax(apartment.getId());
            apartment2 = apartmentService.payTax(apartment2.getId());
            apartment3 = apartmentService.payTax(apartment3.getId());

            System.out.println("филтриране и сортиране на компании, служители, жители в сграда(точка 8)");

            companyService.getAllByOrderByIncomeDesc();

            System.out.println(employeeService.getAllByName("Ivan", company.getId()));
            System.out.println(employeeService.getEmployeesWithBuildingsMoreOrLessThanOfCompany(true, 1, company.getId()));

            System.out.println(residentService.getResidentsByNameAndBuilding("Johny", building.getId()));
            System.out.println(residentService.getResidentsOlderOrYoungerThanInBuilding(true, 0, 0));
            //THIS DOESNT WORK!?!??!?!qqw
//            System.out.println("обобщени и подробни справки (точка 9)");
//
//            System.out.println(queryService.getNumberOfBuildingsOfEmployee(employee2.getId()));
//            System.out.println(queryService.getListOfBuildingsOfEmployee(employee2.getId()));
//
//            System.out.println(queryService.getNumberOfApartmentsInBuilding(building.getId()));
//            System.out.println(queryService.getListOfApartmentsInBuilding(building.getId()));
//
//            System.out.println(queryService.getNumberOfResidentsInBuilding(building.getId()));
//            System.out.println(queryService.getListOfResidentsInBuilding(building.getId()));


        };
    }

}