package app.ccb.domain.dtos;

import app.ccb.domain.entities.Client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExportEmployee {

    private String fullName;
    private BigDecimal salary;
    private LocalDate startedOn;
    private List<Client> clients;


    public ExportEmployee() {

        this.clients = new ArrayList<>();

    }

    public ExportEmployee(String fullName, BigDecimal salary, LocalDate startedOn, List<Client> clients) {
        this.fullName = fullName;
        this.salary = salary;
        this.startedOn = startedOn;
        this.clients = clients;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(LocalDate startedOn) {
        this.startedOn = startedOn;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Full Name: %s",this.getFullName())).append(System.lineSeparator());
        sb.append(String.format("Salary: %s",this.getSalary())).append(System.lineSeparator());
        sb.append(String.format("Started On: %s",this.getStartedOn())).append(System.lineSeparator());



        return sb.toString().trim();

    }
}
