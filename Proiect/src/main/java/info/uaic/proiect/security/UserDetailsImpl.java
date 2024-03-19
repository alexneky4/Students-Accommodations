package info.uaic.proiect.security;

import info.uaic.proiect.models.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String registrationNumber;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private Double averageGrade;


    public UserDetailsImpl(String registrationNumber, String password, String name, Double averageGrade) {
        this.registrationNumber = registrationNumber;
        this.password = password;
        this.name = name;
        this.averageGrade = averageGrade;
    }

    public static UserDetailsImpl build(Student student) {
        return new UserDetailsImpl(student.getRegistrationNumber(), student.getPassword(), student.getName(), student.getAverageGrade());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getName() {
        return name;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}