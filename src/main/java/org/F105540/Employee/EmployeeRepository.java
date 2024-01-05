package org.F105540.Employee;

import org.F105540.Resident.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN FETCH e.buildings b " +
            "WHERE e.company.id = :companyId " +
            "GROUP BY e.id " +
            "ORDER BY COUNT(b) ASC " +
            "LIMIT 1")
    Employee findEmployeeWithLeastBuildingsBelongingToCompany(
            @Param("companyId") int companyId
    );

    @Query("SELECT e FROM Employee e " +
            "LEFT JOIN FETCH e.buildings b " +
            "WHERE e.company.id = :companyId " +
            "AND e.id <> :excludedEmployeeId " +
            "GROUP BY e.id " +
            "ORDER BY COUNT(b) ASC " +
            "LIMIT 1")
    Employee findEmployeeWithLeastBuildingsBelongingToCompany(
            @Param("companyId") int companyId,
            @Param("excludedEmployeeId") int excludedEmployeeId
    );
}
