package org.F105540.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.Apartment;
import org.F105540.Building.Building;
import org.F105540.Employee.Employee;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @OneToMany(mappedBy = "company", cascade = CascadeType.PERSIST)
    private List<Building> buildings;


    @Column(name = "name")
    private String name;

    @Column(name = "income")
    private int income;

    @Column(name = "expenses_to_pay")
    private int expenses;

    @Column(name = "expenses_paid")
    private int expensesPaid;

}
