package app.ccb.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {

    private String name;

    public Branch() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
