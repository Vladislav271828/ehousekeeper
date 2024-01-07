package org.F105540.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Building.DtoBuilding;
import org.F105540.company.DtoCompany;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoEmployee {

    private Integer id;

    private String name = null;

    private DtoCompany company;

    private List<DtoBuilding> buildings;

    private Integer salaryToBePaid = null;
    
    private Integer salaryPaid = null;



    @Override
    public String toString() {
        return "DtoEmployee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", company=" + company +
                ", salaryToBePaid=" + salaryToBePaid +
                ", salaryPaid=" + salaryPaid +
                '}';
    }
}