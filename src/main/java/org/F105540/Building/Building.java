package org.F105540.Building;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.Apartment;
import org.F105540.Employee.Employee;

import java.math.BigDecimal;
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


    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private List<Apartment> apartments;


    @Column(name = "address")
    private String address;
    @Column(name = "num_of_floors")
    @Min(value = 0, message = "Number of floors cannot be negative")
    private int numberOfFloors;
    @Column(name = "num_of_apartments")
    @Min(value = 0, message = "Number of apartments cannot be negative")
    private int numberOfApartments;
    @Column(name = "area")
    @Min(value = 0, message = "Area cannot be negative")
    private double area;
    @Column(name = "expenses")
    private BigDecimal expenses;
    @Column(name = "expenses_paid")
    private BigDecimal expensesPaid;
    @Column(name = "tax_per_area")
    @Min(value = 0, message = "Tax per area cannot be negative")
    private BigDecimal taxPerArea;
    @Column(name = "tax_per_elevator_person")
    @Min(value = 0, message = "Tax per elevator person cannot be negative")
    private BigDecimal taxPerElevatorPerson;
    @Column(name = "tax_for_pet")
    @Min(value = 0, message = "Tax per pet cannot be negative")
    private BigDecimal taxForPet;
    @Column(name = "common_parts")
    private String commonParts;

}
