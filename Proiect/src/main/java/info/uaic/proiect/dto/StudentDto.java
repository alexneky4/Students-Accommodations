package info.uaic.proiect.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class StudentDto
{
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotEmpty(message = "Registration number should not be empty")
    private String registrationNumber;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    @Min(value = 0, message = "Average Grade must be greater than or equal to 0")
    @Max(value = 10, message = "Average Grade must be less than or equal to 10")
    @NotNull(message = "Average Grade should not be empty")
    private Double averageGrade;

    public StudentDto(String name, String registrationNumber, String password, Double averageGrade) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.password = password;
        this.averageGrade = averageGrade;
    }

    public StudentDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }
}