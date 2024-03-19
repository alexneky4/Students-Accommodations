package info.uaic.proiect.controllers;

import info.uaic.proiect.models.Dormitory;
import info.uaic.proiect.models.ServerState;
import info.uaic.proiect.models.Student;
import info.uaic.proiect.repositories.DormitoryRepository;
import info.uaic.proiect.repositories.ServerStateRepository;
import info.uaic.proiect.repositories.StudentRepository;
import info.uaic.proiect.security.UserDetailsImpl;
import info.uaic.proiect.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final ServerStateRepository serverStateRepository;

    private Student currentStudent;
    @Autowired
    public StudentController(StudentRepository studentRepository, ServerStateRepository serverStateRepository)
    {
        this.studentRepository = studentRepository;
        this.serverStateRepository = serverStateRepository;
    }

    @GetMapping
    public String getStudents(Model model)
    {
        ServerState serverState = serverStateRepository.findFirstByOrderByIdDesc();
        if(serverState != null) {
            if (!serverState.isDormitory_phase() && !serverState.isStudent_phase()) {
                return "redirect:/noPhase";
            }
            if (serverState.isDormitory_phase()) {
                return "redirect:/dormitories";
            }
        }
        else return "redirect:/noPhase";

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String registrationNumber = userDetails.getRegistrationNumber();
        currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();

        if(currentStudent.getAssignedDormitory() == null) {
            return "redirect:/noPhase";
        }

        if(serverState.updateTimer() == true)
            serverStateRepository.save(serverState);
        long seconds = 0;
        if(serverState != null) {
            if(serverState.isDormitory_phase() == true)
                seconds = Duration.between(LocalDateTime.now(), serverState.getEndingDateDormitoryPhase()).getSeconds();
            else if(serverState.isStudent_phase() == true)
                seconds = Duration.between(LocalDateTime.now(), serverState.getEndingDateStudentPhase()).getSeconds();
        }
        model.addAttribute("timer", seconds);
        model.addAttribute("name", currentStudent.getName());
        model.addAttribute("grade", currentStudent.getAverageGrade());
        model.addAttribute("admin", currentStudent.getIsAdmin());
        if(currentStudent.getAssignedDormitory() == null)
            model.addAttribute("dormitory", "NA");
        else
            model.addAttribute("dormitory", currentStudent.getAssignedDormitory().getName());
        if(currentStudent.getAssignedRoom() == null)
            model.addAttribute("room", "NA");
        else model.addAttribute("room", currentStudent.getAssignedRoom().getId());
        model.addAttribute("studentList", studentRepository.findAllOtherStudents(currentStudent.getId(), currentStudent.getAssignedDormitory()));
        System.out.println(studentRepository.findAllOtherStudents(currentStudent.getId(), currentStudent.getAssignedDormitory()));
        model.addAttribute("studentNumber", studentRepository.findAllOtherStudents(currentStudent.getId(), currentStudent.getAssignedDormitory()).size());
        return "students";
    }

    @PostMapping
    public ResponseEntity<?> addStudentPreferences(@RequestBody List<String> studentNames)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String registrationNumber = userDetails.getRegistrationNumber();
        Student currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();
        System.out.println(studentNames);
        System.out.println(currentStudent.getName());
        List<Student> students = new ArrayList<>();
        for(String studentName : studentNames)
        {
            students.add(studentRepository.findByName(studentName));
        }
        currentStudent.setPreferencesStudents(students);
        studentRepository.save(currentStudent);

        currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();
        System.out.println(currentStudent.getPreferencesStudents());

        return ResponseEntity.ok(new ResponseMessage("Students saved successfully!"));
    }
}
