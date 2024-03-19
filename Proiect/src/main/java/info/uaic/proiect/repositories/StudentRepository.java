package info.uaic.proiect.repositories;

import info.uaic.proiect.models.Dormitory;
import info.uaic.proiect.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRegistrationNumber(String registrationNumber);

    Student findByName(String name);

    @Query("SELECT s FROM Student s WHERE s.id <> :studentId AND s.assignedDormitory = :dormitory")
    List<Student> findAllOtherStudents(@Param("studentId") Long studentId, @Param("dormitory") Dormitory dormitory);

    @Query("SELECT s FROM Student s WHERE s.assignedDormitory = :dormitory")
    List<Student> findAllStudentsForADormitory(@Param("dormitory") Dormitory dormitory);
}
