package org.F105540.Employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Building.Building;
import org.F105540.company.Company;

import java.math.BigDecimal;
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
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.PERSIST)
    private List<Building> buildings;


    @Column(name = "salary_to_be_paid")
    private BigDecimal salaryToBePaid;

    @Column(name = "expenses_paid")
    private BigDecimal salaryPaid;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId=" + company.getId() +
                ", buildings=" + buildings.size() +
                ", salaryToBePaid=" + salaryToBePaid +
                ", salaryPaid=" + salaryPaid +
                '}';
    }
}
