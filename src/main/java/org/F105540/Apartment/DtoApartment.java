package org.F105540.Apartment;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Building.Building;
import org.F105540.Building.DtoBuilding;
import org.F105540.Owner.DtoOwner;
import org.F105540.Owner.Owner;
import org.F105540.Resident.DtoResident;
import org.F105540.Resident.Resident;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoApartment {

    private Integer id;
    private DtoBuilding building;
    private DtoOwner owner;
    private List<DtoResident> residents;
    @Min(value = 0, message = "Apartment number cannot be negative")
    private Integer number;
    private Integer floor;
    @Min(value = 0, message = "Area cannot be negative")
    private double area;
    private boolean taxIsPaid;
    private boolean hasPet;


    @Override
    public String toString() {
        return "DtoApartment{" +
                "id=" + id +
                ", building id=" + building.getId() +
                ", owner id=" + owner.getId() +
                ", residents=" + residents +
                ", number=" + number +
                ", floor=" + floor +
                ", area=" + area +
                ", taxIsPaid=" + taxIsPaid +
                ", hasPet=" + hasPet +
                '}';
    }

}
