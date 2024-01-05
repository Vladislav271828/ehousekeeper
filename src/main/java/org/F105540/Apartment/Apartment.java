package org.F105540.Apartment;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.F105540.Building.Building;
import org.F105540.Employee.Employee;
import org.F105540.Owner.Owner;
import org.F105540.Resident.Resident;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apartment")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "apartment_resident",
            joinColumns = @JoinColumn(name = "apartment_id"),
            inverseJoinColumns = @JoinColumn(name = "resident_id"))
    private List<Resident> residents;

    private Integer number;
    private Integer floor;
    private int area;
    @Column(name = "has_pet")
    private boolean hasPet;


    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", building id=" + building.getId() +
                ", owner id=" + owner.getId() +
                ", residents=" + residents +
                ", number=" + number +
                ", floor=" + floor +
                ", area=" + area +
                ", hasPet=" + hasPet +
                '}';
    }
}



