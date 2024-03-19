package info.uaic.proiect.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "server_details")
public class ServerState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_state_seq")
    @Column(name ="id")
    private Long id;

    @Column(name = "ending_date_dormitory_phase")
    private LocalDateTime endingDateDormitoryPhase;

    @Column(name = "ending_date_student_phase")
    private LocalDateTime endingDateStudentPhase;

    @Column(name = "dormitory_phase")
    private boolean dormitory_phase;

    @Column(name = "student_phase")
    private boolean student_phase;

    public ServerState() {
    }

    public ServerState(LocalDateTime endingDateDormitoryPhase, LocalDateTime endingDateStudentPhase) {
        this.endingDateDormitoryPhase = endingDateDormitoryPhase;
        this.endingDateStudentPhase = endingDateStudentPhase;
        dormitory_phase = false;
        student_phase = false;
    }

    public boolean isStudent_phase() {
        return student_phase;
    }

    public void setStudent_phase(boolean student_phase) {
        this.student_phase = student_phase;
    }

    public boolean isDormitory_phase() {
        return dormitory_phase;
    }

    public void setDormitory_phase(boolean dormitory_phase) {
        this.dormitory_phase = dormitory_phase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEndingDateDormitoryPhase() {
        return endingDateDormitoryPhase;
    }

    public void setEndingDateDormitoryPhase(LocalDateTime endingDateDormitoryPhase) {
        this.endingDateDormitoryPhase = endingDateDormitoryPhase;
    }

    public LocalDateTime getEndingDateStudentPhase() {
        return endingDateStudentPhase;
    }

    public void setEndingDateStudentPhase(LocalDateTime endingDateStudentPhase) {
        this.endingDateStudentPhase = endingDateStudentPhase;
    }

    public boolean updateTimer()
    {
        LocalDateTime currentTime = LocalDateTime.now();

        if (endingDateDormitoryPhase != null && currentTime.isBefore(endingDateDormitoryPhase)) {
            dormitory_phase = true;
            student_phase = false;
            return true;
        } else if (endingDateStudentPhase != null && currentTime.isBefore(endingDateStudentPhase)) {
            dormitory_phase = false;
            student_phase = true;
            return true;
        } else if (endingDateStudentPhase != null && currentTime.isAfter(endingDateStudentPhase)){
            dormitory_phase = false;
            student_phase = false;
            return true;
        }
        return false;
    }

}
