package info.uaic.proiect.controllers;

import info.uaic.proiect.models.ServerState;
import info.uaic.proiect.models.Student;
import info.uaic.proiect.repositories.ServerStateRepository;
import info.uaic.proiect.repositories.StudentRepository;
import info.uaic.proiect.security.UserDetailsImpl;
import info.uaic.proiect.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/noPhase")
public class NoPhaseController {
    private final ServerStateRepository serverStateRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public NoPhaseController(ServerStateRepository serverStateRepository, StudentRepository studentRepository) {
        this.serverStateRepository = serverStateRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String getNoPhasePage(Model model) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String registrationNumber = userDetails.getRegistrationNumber();
        Student currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();

        ServerState serverState = serverStateRepository.findFirstByOrderByIdDesc();

        if(serverState != null) {
            if (serverState.isDormitory_phase() || serverState.isStudent_phase()) {
                return "redirect:/students";
            }
        }

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

        return "no-phase";
    }
}
