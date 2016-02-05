package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "employee")
public class Location {
    @Id
    private Integer id;
    private String name;
    private String address;

    public Location(Integer id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    @Column(name="id")
    public Integer getId() {
        return id;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    @Column(name="address")
    public String getAddress() {
        return address;
    }
}
