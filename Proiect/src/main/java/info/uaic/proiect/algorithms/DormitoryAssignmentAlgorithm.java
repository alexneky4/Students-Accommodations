package info.uaic.proiect.algorithms;

import info.uaic.proiect.models.Dormitory;
import info.uaic.proiect.models.Room;
import info.uaic.proiect.models.Student;

import java.util.List;

public class DormitoryAssignmentAlgorithm {

    private static void assignDormitoryToStudent(Student student, List<Dormitory> dormitoryList)
    {
        List<Dormitory> preferences = student.getPreferencesDormitory();
        for(Dormitory dormitory : preferences) {
            if (dormitory.getLeftPlaces() > 0) {
                student.setAssignedDormitory(dormitory);
                dormitory.setLeftPlaces(dormitory.getLeftPlaces() - 1);
                return;
            }
        }

        for(Dormitory dormitory : dormitoryList) {
            if(dormitory.getLeftPlaces() > 0) {
                student.setAssignedDormitory(dormitory);
                dormitory.setLeftPlaces(dormitory.getLeftPlaces() - 1);
                return;
            }
        }
    }

    public static void assignDormitories(List<Student> studentList, List<Dormitory> dormitoryList)
    {
        studentList.sort((s1,s2) -> Double.compare(s2.getAverageGrade(), s1.getAverageGrade()));
        for(Student student : studentList)
            assignDormitoryToStudent(student, dormitoryList);
    }
}
