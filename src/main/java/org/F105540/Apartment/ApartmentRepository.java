package org.F105540.Apartment;

import jakarta.transaction.Transactional;
import org.F105540.Building.Building;
import org.F105540.Resident.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Integer> {

    @Query("SELECT a FROM Apartment a " +
            "JOIN a.building b " +
            "WHERE b.id = :buildingId")
    List<Apartment> findListOfApartmentsInBuilding(@Param("buildingId") int buildingId);

}
