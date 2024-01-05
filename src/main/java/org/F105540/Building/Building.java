package org.F105540.Building;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.Apartment;
import org.F105540.Employee.Employee;
import org.F105540.company.Company;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Apartment> apartments;




    @Column(name = "address")
    private String address;
    @Column(name = "num_of_floors")
    private int numberOfFloors;
    @Column(name = "num_of_apartments")
    private int numberOfApartments;
    @Column(name = "area")
    private int area;
    @Column(name = "expenses")
    private double expenses;
    @Column(name = "expenses_paid")
    private double expensesPaid;
    @Column(name = "tax_per_area")
    private double taxPerArea;
    @Column(name = "tax_per_elevator_person")
    private double taxPerElevatorPerson;
    @Column(name = "tax_for_pet")
    private double taxForPet;
    @Column(name = "common_parts")
    private String commonParts;

}
