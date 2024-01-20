package org.F105540.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Building.DtoBuilding;
import org.F105540.Employee.DtoEmployee;

import java.math.BigDecimal;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoCompany {

    private Integer id;

    private String name = null;

    private List<DtoEmployee> employees;

    private BigDecimal income = null;

    private BigDecimal expenses = null;

    private BigDecimal expensesPaid = null;

    @Override
    public String toString() {
        return "DtoCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", income=" + income +
                ", expenses=" + expenses +
                ", expensesPaid=" + expensesPaid +
                '}';
    }
}
