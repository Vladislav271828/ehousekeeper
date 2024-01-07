package org.F105540.Building;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
    private List<DtoApartment> apartments;
    private String address = null;

    private Integer numberOfFloors = null;

    private Integer numberOfApartments = null;

    private Double area = null;
    private Double expenses = null;
    private Double expensesPaid = null;
    private Double taxPerArea = null;
    private Double taxPerElevatorPerson = null;
    private Double taxForPet = null;
    private String commonParts = null;

    @Override
    public String toString() {
        return "DtoBuilding{" +
                "id=" + id +
                ", employee=" + employee+
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
