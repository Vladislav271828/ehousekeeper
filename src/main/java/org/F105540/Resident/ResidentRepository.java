package org.F105540.Resident;

import jakarta.transaction.Transactional;
import org.F105540.Apartment.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Integer> {

    @Transactional
    @Query("SELECT COUNT(c) FROM Resident c " +
            "WHERE c.apartment.id = :apartmentId " +
            "AND (c.age > 7) " +
            "AND (c.usesElevator = true)")
    int findNumberOfResidentsOlderThanSevenAndUsingElevator(
            @Param("apartmentId") Integer Id
    );

}
