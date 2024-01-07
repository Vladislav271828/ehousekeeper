package org.F105540.Owner;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Apartment.DtoApartment;

import java.util.List;

@Data

@NoArgsConstructor
public class DtoOwner {

    private Integer id;

    private List<DtoApartment> apartments;

    private String name = null;

    @Builder
    public DtoOwner(String name) {
        this.name = name;
    }
}
