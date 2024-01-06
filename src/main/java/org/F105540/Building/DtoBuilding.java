package org.F105540.Building;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.Apartment;
import org.F105540.Apartment.DtoApartment;
import org.F105540.Employee.DtoEmployee;
import org.F105540.Employee.Employee;
import org.F105540.company.Company;
import org.F105540.company.DtoCompany;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoBuilding {

    Integer id;
    private DtoEmployee employee;
    private DtoCompany company;
    private List<DtoApartment> apartments;
    private String address;
    @Min(value = 0, message = "Number of floors cannot be negative")
    private int numberOfFloors;
    @Min(value = 0, message = "Number of apartments cannot be negative")
    private int numberOfApartments;
    @Min(value = 0, message = "Area cannot be negative")
    private double area;
    private double expenses;
    private double expensesPaid;
    @Min(value = 0, message = "Tax per area cannot be negative")
    private double taxPerArea;
    @Min(value = 0, message = "Tax per elevator person cannot be negative")
    private double taxPerElevatorPerson;
    @Min(value = 0, message = "Tax per pet cannot be negative")
    private double taxForPet;
    private String commonParts;

    @Override
    public String toString() {
        return "DtoBuilding{" +
                "id=" + id +
                ", employee=" + employee+
                ", apartments=" + apartments +
                ", address='" + address + '\'' +
                ", numberOfFloors=" + numberOfFloors +
                ", numberOfApartments=" + numberOfApartments +
                ", area=" + area +
                ", expenses=" + expenses +
                ", expensesPaid=" + expensesPaid +
                ", taxPerArea=" + taxPerArea +
                ", taxPerElevatorPerson=" + taxPerElevatorPerson +
                ", taxForPet=" + taxForPet +
                ", commonParts='" + commonParts + '\'' +
                '}';
    }
}
