package app.ccb.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

@Entity(name = "branches")
public class Branch extends BaseEntity {

    private String name;
    private List<Employee> employees;



    @Column(name = "name",nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
