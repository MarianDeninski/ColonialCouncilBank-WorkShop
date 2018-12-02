package app.ccb.repositories;

import app.ccb.domain.dtos.ExportEmployee;
import app.ccb.domain.entities.Client;
import app.ccb.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {


    @Query("" +
            "SELECT e " +
            "FROM app.ccb.domain.entities.Employee e " +
            "WHERE concat(e.firstName, ' ', e.lastName) = :fullName")
    Employee findByFullName(@Param("fullName") String fullName);



   // @Query("SELECT new app.ccb.domain.dtos.ExportEmployee(concat(e.firstName,' ',e.lastName)," +
   //         "e.salary,e.startedOn, e.clients  )" +
   //         " FROM app.ccb.domain.entities.Employee e " +
   //         " WHERE size(e.clients)>0"+
   //         " ORDER BY size(e.clients) DESC,e.id ASC ")
   // List<ExportEmployee> getAllEmployees();

    @Query("SELECT e" +
             " FROM app.ccb.domain.entities.Employee e " +
             " WHERE size(e.clients)>0"+
             " ORDER BY size(e.clients) DESC,e.id ASC ")
     List<Employee> getAllEmployees();

}
