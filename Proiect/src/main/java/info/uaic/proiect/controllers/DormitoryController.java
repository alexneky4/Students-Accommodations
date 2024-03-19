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
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dormitories")
public class DormitoryController {

    private final StudentRepository studentRepository;

    private final DormitoryRepository dormitoryRepository;

    private final ServerStateRepository serverStateRepository;

    @Autowired
    public DormitoryController(StudentRepository studentRepository, DormitoryRepository dormitoryRepository, ServerStateRepository serverStateRepository)
    {
        this.studentRepository = studentRepository;
        this.dormitoryRepository = dormitoryRepository;
        this.serverStateRepository = serverStateRepository;
    }
    @GetMapping
    public String getStudentDormitory(Model model)
    {
        ServerState serverState = serverStateRepository.findFirstByOrderByIdDesc();
        if(serverState != null) {
            if (!serverState.isDormitory_phase() && !serverState.isStudent_phase()) {
                return "redirect:/noPhase";
            }
            if (serverState.isStudent_phase()) {
                return "redirect:/students";
            }
        }
        else return "redirect:/noPhase";
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String registrationNumber = userDetails.getRegistrationNumber();
        Student currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();


        long seconds = 0;
        if(serverState.updateTimer())
            serverStateRepository.save(serverState);
        if(serverState.isDormitory_phase())
            seconds = Duration.between(LocalDateTime.now(), serverState.getEndingDateDormitoryPhase()).getSeconds();
        else if(serverState.isStudent_phase())
            seconds = Duration.between(LocalDateTime.now(), serverState.getEndingDateStudentPhase()).getSeconds();
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
        model.addAttribute("dormitoryList", dormitoryRepository.findAll());
        model.addAttribute("dormitoryNumber", dormitoryRepository.findAll().size());
        System.out.println(dormitoryRepository.findAll());
        return "dormitory";
    }

    @PostMapping
    public ResponseEntity<?> addDormitoryPreferences(@RequestBody List<String> dormitoryNames)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String registrationNumber = userDetails.getRegistrationNumber();
        Student currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();
        System.out.println(dormitoryNames);
        System.out.println(currentStudent.getName());
        List<Dormitory> dormitories = new ArrayList<>();
        for(String dormitoryName : dormitoryNames)
        {
            dormitories.add(dormitoryRepository.findByName(dormitoryName));
        }
        currentStudent.setPreferencesDormitory(dormitories);
        studentRepository.save(currentStudent);

        currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();
        System.out.println(currentStudent.getPreferencesDormitory());

        return ResponseEntity.ok(new ResponseMessage("Dormitories saved successfully!"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDormitory(@RequestBody String dormitoryName) {
        dormitoryName = dormitoryName.substring(1, dormitoryName.length() - 1);
        dormitoryRepository.delete(dormitoryRepository.findByName(dormitoryName));

        return ResponseEntity.ok(new ResponseMessage("Successfully deleted the dormitory!"));
    }

}
