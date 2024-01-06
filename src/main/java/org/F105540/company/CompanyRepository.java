package org.F105540.company;

import org.F105540.Employee.Employee;
import org.F105540.Resident.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    @Query("SELECT c FROM Company c ORDER BY c.income DESC")
    List<Company> findAllByOrderByIncomeDesc();



}
