package org.F105540.Owner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.Apartment;
import org.F105540.Apartment.DtoApartment;

import java.util.List;

@Data


@NoArgsConstructor
public class DtoOwner {

    private Integer id;

    private List<DtoApartment> apartments;

    private String name;

    @Builder
    public DtoOwner(String name) {
        this.name = name;
    }
}
