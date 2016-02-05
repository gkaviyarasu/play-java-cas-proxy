package models;

import javax.persistence.*;

@Entity(name = "employee")
@UniqueConstraint(columnNames = {"email_id"})
public class Employee {
    @Id
    private final String   id;
    private final Integer   employeeId;
    private final String   firstName;
    private final String   lastName;
    private final String   fullName;
    private final String   nickName;
    private final String   emailId;
    private final String   jobTitle;
    private final String   deskLocation;
    private final Employee manager;
    private final String   address;
    private final String   city;
    private final String   country;
    private final String   mobile;

    public Employee(String id, Integer employeeId, String firstName, String lastName, String fullName, String nickName, String emailId, String jobTitle, String deskLocation, Employee manager,
                    String address, String city, String country, String mobile) {
        this.employeeId = employeeId;
        this.address = address;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.nickName = nickName;
        this.emailId = emailId;
        this.jobTitle = jobTitle;
        this.deskLocation = deskLocation;
        this.manager = manager;
        this.city = city;
        this.country = country;
        this.mobile = mobile;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    @Column(name = "email_id")
    public String getEmailId() {
        return emailId;
    }

    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    @Column(name = "id")
    public String getId() {
        return id;
    }

    @Column(name = "desk_location")
    public String getDeskLocation() {
        return deskLocation;
    }

    @Column(name = "job_title")
    public String getJobTitle() {
        return jobTitle;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @ManyToOne(targetEntity = Employee.class, fetch = FetchType.LAZY, optional = true)
    @Column(name = "manager_id")
    public Employee getManager() {
        return manager;
    }

    @Column(name = "mobile", nullable = false)
    public String getMobile() {
        return mobile;
    }

    @Column(name = "nick_name")
    public String getNickName() {
        return nickName;
    }

    @Column(name = "employee_id")
    public Integer getEmployeeId() {
        return employeeId;
    }
}
