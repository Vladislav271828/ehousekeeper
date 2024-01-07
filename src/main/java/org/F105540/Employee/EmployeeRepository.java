package org.F105540.Employee;

import org.F105540.Resident.Resident;
import org.F105540.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN FETCH e.buildings b " +
            "WHERE e.company.id = :companyId " +
            "GROUP BY e.id, b.id " +
            "ORDER BY COUNT(b) ASC " +
            "LIMIT 1")
    Employee findEmployeeWithLeastBuildingsBelongingToCompany(
            @Param("companyId") int companyId
    );

    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN FETCH e.buildings b " +
            "WHERE e.company.id = :companyId " +
            "AND e.id <> :excludedEmployeeId " +
            "GROUP BY e.id, e, b " +
            "ORDER BY COUNT(b) ASC " +
            "LIMIT 1")
    Employee findEmployeeWithLeastBuildingsBelongingToCompany(
            @Param("companyId") int companyId,
            @Param("excludedEmployeeId") int excludedEmployeeId
    );

    List<Employee> findAllByNameAndCompany(String name, Company company);

    @Query("SELECT e FROM Employee e " +
            "WHERE SIZE(e.buildings) > :threshold " +
            "AND e.company.id = :companyId " +
            "ORDER BY SIZE(e.buildings) DESC")
    List<Employee> findEmployeesWithMoreBuildingsThan(
            @Param("threshold") int threshold,
            @Param("companyId") int companyId);

    @Query("SELECT e FROM Employee e " +
            "WHERE SIZE(e.buildings) < :threshold " +
            "AND e.company.id = :companyId " +
            "ORDER BY SIZE(e.buildings) DESC")
    List<Employee> findEmployeesWithLessBuildingsThan(
            @Param("threshold") int threshold,
            @Param("companyId") int companyId);
}


