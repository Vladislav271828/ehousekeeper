package org.F105540.Employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Building.Building;
import org.F105540.company.Company;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "id", cascade = CascadeType.PERSIST)
    private List<Building> buildings;


    @Column(name = "salary_to_be_paid")
    private int salaryToBePaid;

    @Column(name = "salary_paid")
    private int salaryPaid;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId=" + company.getId() +
                ", buildings=" + buildings +
                ", salaryToBePaid=" + salaryToBePaid +
                ", salaryPaid=" + salaryPaid +
                '}';
    }
}
