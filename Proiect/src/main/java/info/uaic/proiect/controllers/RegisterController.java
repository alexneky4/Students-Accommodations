package info.uaic.proiect.controllers;

import info.uaic.proiect.dto.StudentDto;
import info.uaic.proiect.models.Student;
import info.uaic.proiect.repositories.StudentRepository;
import info.uaic.proiect.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentService studentService;

    @Autowired
    public RegisterController(StudentRepository studentRepository, PasswordEncoder passwordEncoder, StudentService studentService) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentService = studentService;
    }

    @GetMapping
    public String register(Model model) {
        StudentDto student = new StudentDto();
        model.addAttribute("student", student);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dormitories";
        }
        return "register";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("student") StudentDto student,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if(result.hasErrors()) {
            model.addAttribute("student", student);
            return "register";
        }

        if(studentRepository.findByRegistrationNumber(student.getRegistrationNumber()).isPresent()) {
            result.rejectValue("registrationNumber", null, "There is already an account registered with that registration number!");
        }

        if(result.hasErrors()) {
            model.addAttribute("student", student);
            return "register";
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("success", "You successfully made an account! Please login:");
        return "redirect:/login";
        }
}