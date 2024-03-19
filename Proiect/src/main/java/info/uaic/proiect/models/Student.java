package info.uaic.proiect.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "average_grade")
    private Double averageGrade;

    @Column(name = "is_admin")
    private int isAdmin;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dormitory_preferences",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "dormitory_id"))
    private List<Dormitory> preferencesDormitory;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "students_preferences",
            joinColumns = @JoinColumn(name = "first_student_id"),
            inverseJoinColumns = @JoinColumn(name = "second_student_id"))
    private List<Student> preferencesStudents;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="room_id")
    private Room assignedRoom;

    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory assignedDormitory;

    public Student() {
    }

    public Student(String name, String password, String registrationNumber, Double averageGrade) {
        this.name = name;
        this.password = password;
        this.registrationNumber = registrationNumber;
        this.averageGrade = averageGrade;
    }

    public Student(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public List<Dormitory> getPreferencesDormitory() {
        return preferencesDormitory;
    }

    public void setPreferencesDormitory(List<Dormitory> preferencesDormitory) {
        this.preferencesDormitory = preferencesDormitory;
    }

    public List<Student> getPreferencesStudents() {
        return preferencesStudents;
    }

    public void setPreferencesStudents(List<Student> preferencesStudents) {
        this.preferencesStudents = preferencesStudents;
    }

    public Room getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Dormitory getAssignedDormitory() {
        return assignedDormitory;
    }

    public void setAssignedDormitory(Dormitory assignedDormitory) {
        this.assignedDormitory = assignedDormitory;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", assignedRoom=" + assignedRoom +
                ", assignedDormitory=" +
                ((assignedDormitory != null) ? assignedDormitory.getId() : "null") +
                '}';
    }
}
