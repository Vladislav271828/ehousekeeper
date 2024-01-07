package org.F105540.Resident;

import jakarta.transaction.Transactional;
import org.F105540.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Integer> {

    @Transactional
    @Query("SELECT COUNT(r) FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.id = :apartmentId " +
            "AND r.age > 7 " +
            "AND r.usesElevator = true")
    int findNumberOfResidentsInApartmentOlderThanSevenAndUsingElevator(
            @Param("apartmentId") Integer apartmentId
    );

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.building.id = :buildingId " +
            "AND r.name = :residentName")
    List<Resident> findResidentsInBuildingIdAndName(
            @Param("buildingId") int buildingId,
            @Param("residentName") String residentName);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.building.id = :buildingId")
    List<Resident> findResidentsInBuilding(
            @Param("buildingId") int buildingId);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.building.id = :buildingId " +
            "AND r.age > :ageThreshold")
    List<Resident> findResidentsInBuildingOlderThan(
            @Param("buildingId") int buildingId,
            @Param("ageThreshold") int ageThreshold);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.building.id = :buildingId " +
            "AND r.age < :ageThreshold")
    List<Resident> findResidentsInBuildingYoungerThan(
            @Param("buildingId") int buildingId,
            @Param("ageThreshold") int ageThreshold);


}
