package org.F105540.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Building.DtoBuilding;
import org.F105540.Employee.DtoEmployee;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoCompany {

    private Integer id;

    private String name;

    private List<DtoBuilding> buildings;

    private List<DtoEmployee> employees;

    private int income;

    private int expenses;

    private int expensesPaid;

}
