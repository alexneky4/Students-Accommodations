package info.uaic.proiect.repositories;

import info.uaic.proiect.models.ServerState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ServerStateRepository extends JpaRepository<ServerState, Long>{

        ServerState findFirstByOrderByIdDesc();
}
