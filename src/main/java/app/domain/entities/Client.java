package app.ccb.domain.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "clients")
public class Client extends BaseEntity {

    private String fullName;
    private int age;
    private BankAccount bankAccount;
    private List<Employee> employees;


    public Client() {

        this.employees = new ArrayList<>();
    }

    @ManyToMany(targetEntity = Employee.class)
    @JoinTable(name = "employees_clients",joinColumns = @JoinColumn(name = "client_id")
            ,inverseJoinColumns = @JoinColumn(name = "employee_id",referencedColumnName = "id"))
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Column(name = "full_name",nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @OneToOne(targetEntity = BankAccount.class,mappedBy = "client")
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }


}
