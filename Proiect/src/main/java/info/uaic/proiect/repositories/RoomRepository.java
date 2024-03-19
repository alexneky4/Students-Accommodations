package info.uaic.proiect.repositories;

import info.uaic.proiect.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
