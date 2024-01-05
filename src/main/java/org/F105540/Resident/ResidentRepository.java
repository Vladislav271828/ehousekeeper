package org.F105540.Resident;

import jakarta.transaction.Transactional;
import org.F105540.Apartment.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Integer> {

    @Transactional
    @Query("SELECT COUNT(r) FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.id = :apartmentId " +
            "AND (r.age > 7) " +
            "AND (r.usesElevator = true)")
    int findNumberOfResidentsInApartmentOlderThanSevenAndUsingElevator(
            @Param("apartmentId") Integer apartmentId
    );

}
