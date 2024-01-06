package org.F105540.Building;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Integer> {

    @Query("SELECT b FROM Building b " +
            "JOIN b.employee e " +
            "WHERE e.id = :employeeId")
    List<Building> findBuildingsByEmployeeId(@Param("employeeId") int employeeId);
}

