package info.uaic.proiect.security;

import info.uaic.proiect.models.Student;
import info.uaic.proiect.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final StudentRepository studentRepository;

    @Autowired
    public UserDetailsServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String registrationNumber) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByRegistrationNumber(registrationNumber);
        if(student.isEmpty()) {
            System.out.println("if");
            throw new UsernameNotFoundException("User Not Found with registration number: " + registrationNumber);
        }

        return UserDetailsImpl.build(student.get());
    }
}