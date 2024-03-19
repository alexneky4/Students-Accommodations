package info.uaic.proiect.services;

import info.uaic.proiect.models.Dormitory;
import info.uaic.proiect.models.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingService {
    List<Student> students;
    List<Dormitory> dormitories;

    public MatchingService(List<Student> students, List<Dormitory> dormitories) {
        this.students = students;
        this.dormitories = dormitories;
    }


}
