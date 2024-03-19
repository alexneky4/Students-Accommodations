package info.uaic.proiect.controllers;

import info.uaic.proiect.algorithms.DormitoryAssignmentAlgorithm;
import info.uaic.proiect.algorithms.GeneticAlgorithm;
import info.uaic.proiect.models.Dormitory;
import info.uaic.proiect.models.Room;
import info.uaic.proiect.models.Student;
import info.uaic.proiect.models.ServerState;
import info.uaic.proiect.repositories.DormitoryRepository;
import info.uaic.proiect.repositories.RoomRepository;
import info.uaic.proiect.repositories.StudentRepository;
import info.uaic.proiect.repositories.ServerStateRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentRepository studentRepository;
    private final DormitoryRepository dormitoryRepository;
    private final ServerStateRepository serverStateRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public AdminController(StudentRepository studentRepository,
                           DormitoryRepository dormitoryRepository,
                           ServerStateRepository serverStateRepository,
                           RoomRepository roomRepository)
    {
        this.studentRepository = studentRepository;
        this.dormitoryRepository = dormitoryRepository;
        this.serverStateRepository = serverStateRepository;
        this.roomRepository = roomRepository;
    }

    @GetMapping
    public String getAdminPage(Model model)
    {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String registrationNumber = userDetails.getRegistrationNumber();
        Student currentStudent = studentRepository.findByRegistrationNumber(registrationNumber).get();
        model.addAttribute("name", currentStudent.getName());
        model.addAttribute("admin", currentStudent.getIsAdmin());
        ServerState serverState = serverStateRepository.findFirstByOrderByIdDesc();
        long seconds = 0;
        if(currentStudent.getIsAdmin() == 0) {
            return "redirect:/students";
        }
        if(serverState != null)
        {
            if(serverState.updateTimer())
                serverStateRepository.save(serverState);
            if(!serverState.isDormitory_phase() && !serverState.isStudent_phase()) {
                model.addAttribute("phase", "none");
            }
            if(serverState.isDormitory_phase()) {
                model.addAttribute("phase", "dormitory");
            }
            if(serverState.isStudent_phase()) {
                model.addAttribute("phase", "student");
            }
            if(serverState.isDormitory_phase())
                seconds = Duration.between(LocalDateTime.now(), serverState.getEndingDateDormitoryPhase()).getSeconds();
            else if(serverState.isStudent_phase())
                seconds = Duration.between(LocalDateTime.now(), serverState.getEndingDateStudentPhase()).getSeconds();
        }
        else
        {
            model.addAttribute("phase", "none");
        }
        
        model.addAttribute("timer", seconds);
        model.addAttribute("dormitoryList", dormitoryRepository.findAll());
        return "admin";
    }

    @PostMapping
    public ResponseEntity<?> addDormitory(@RequestBody List<String> dormitoryData)
    {
        Dormitory dormitory = new Dormitory(dormitoryData.get(0));
        dormitoryRepository.save(dormitory);

        int leftPlaces = 0;
        List<Room> rooms = new ArrayList<>();

        for(int i = 1; i < dormitoryData.size(); i++) {
            Room room = new Room(Integer.parseInt(dormitoryData.get(i)));
            room.setDormitory(dormitory);
            roomRepository.save(room);
            rooms.add(room);
            leftPlaces += rooms.get(i - 1).getCapacity();
        }

        dormitory.setLeftPlaces(leftPlaces);

        dormitory.setRooms(rooms);
        dormitoryRepository.save(dormitory);

        return ResponseEntity.ok(new ResponseMessage("New dormitory saved successfully!"));
    }

    @PostMapping("/startDormitoryPhase")
    public ResponseEntity<?> startDormitoryPhase(@RequestBody String date)
    {
        date = date.substring(1, date.length() - 1);
        ServerState serverState = serverStateRepository.findFirstByOrderByIdDesc();
        if(serverState == null)
            serverState = new ServerState();
        serverState.setDormitory_phase(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        serverState.setEndingDateDormitoryPhase(localDate);
        serverState.setStudent_phase(false);

        serverStateRepository.save(serverState);
        return ResponseEntity.ok(new ResponseMessage("Dormitory Phase started successfully!"));
    }

    @PostMapping("/endDormitoryPhase")
    public ResponseEntity<?> endDormitoryPhase(@RequestBody String date)
    {
        System.out.println(date);
        date = date.substring(1, date.length() - 1);
        System.out.println(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        ServerState serverState = serverStateRepository.findFirstByOrderByIdDesc();

        LocalDateTime localDateNow = LocalDateTime.now();
        serverState.setEndingDateDormitoryPhase(localDateNow);
        serverState.setEndingDateStudentPhase(localDate);
        serverState.setStudent_phase(true);
        serverState.setDormitory_phase(false);

        List<Student> studentList = studentRepository.findAll();
        List<Dormitory> dormitoryList = dormitoryRepository.findAll();
        System.out.println("Before:");
        System.out.println(studentList);
        DormitoryAssignmentAlgorithm.assignDormitories(studentList,dormitoryList);
        System.out.println("After:");
        System.out.println(studentList);
        dormitoryRepository.saveAll(dormitoryList);
        for(Student student : studentList) {
            studentRepository.save(student);
        }
        //studentRepository.saveAll(studentList);

        serverStateRepository.save(serverState);
        return ResponseEntity.ok(new ResponseMessage("Dormitory Phase ended successfully!"));
    }
    @PostMapping("/endStudentPhase")
    public ResponseEntity<?> endStudentPhase()
    {
        ServerState serverState = serverStateRepository.findFirstByOrderByIdDesc();

        LocalDateTime localDate = LocalDateTime.now();
        serverState.setEndingDateStudentPhase(localDate);
        serverState.setStudent_phase(false);

        List<Dormitory> dormitories = dormitoryRepository.findAll();
        for(Dormitory dormitory : dormitories) {
            List<Student> students = studentRepository.findAllStudentsForADormitory(dormitory);
            if(students.isEmpty())
                continue;
            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(students, dormitory);
            geneticAlgorithm.runGeneticAlgorithm();
            studentRepository.saveAll(students);
        }

        serverStateRepository.save(serverState);
        return ResponseEntity.ok(new ResponseMessage("Student Phase ended successfully!"));
    }


}


