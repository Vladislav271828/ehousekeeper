package org.F105540.Resident;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.Apartment;
import org.F105540.Apartment.DtoApartment;

@Data

@NoArgsConstructor
public class DtoResident {

    private int id;
    private DtoApartment apartment;
    private String name;
    @Min(value = 0, message = "Age cannot be negative")
    private int age;
    private boolean usesElevator;
    @Builder
    public DtoResident(String name, int age, boolean usesElevator) {
        this.name = name;
        this.age = age;
        this.usesElevator = usesElevator;
    }
}
