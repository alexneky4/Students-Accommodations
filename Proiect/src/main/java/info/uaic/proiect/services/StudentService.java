package info.uaic.proiect.services;

import info.uaic.proiect.dto.StudentDto;
import info.uaic.proiect.models.Student;
import info.uaic.proiect.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public void saveStudent(StudentDto student) {
        studentRepository.save(new Student(student.getName(), student.getPassword(), student.getRegistrationNumber(), student.getAverageGrade()));
    }
}
