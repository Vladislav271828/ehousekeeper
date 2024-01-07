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
    private Integer number = null;
    private Integer floor = null;
    private Double area = null;
    private Boolean taxIsPaid = null;
    private Boolean hasPet = null;


    @Override
    public String toString() {
        return "DtoApartment{" +
                "id=" + id +
                ", building=" + building +
                ", owner=" + owner +
                ", number=" + number +
                ", floor=" + floor +
                ", area=" + area +
                ", taxIsPaid=" + taxIsPaid +
                ", hasPet=" + hasPet +
                '}';
    }

}
