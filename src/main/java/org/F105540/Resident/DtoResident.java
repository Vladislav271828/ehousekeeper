package org.F105540.Resident;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.DtoApartment;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DtoResident {

    private Integer id;
    private List<DtoApartment> apartments;
    private String name = null;
    private Integer age = null;
    private Boolean usesElevator = null;



    @Override
    public String toString() {
        return "DtoResident{" +
                "id=" + id +
                //", apartments=" + apartments +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", usesElevator=" + usesElevator +
                '}';
    }
}
