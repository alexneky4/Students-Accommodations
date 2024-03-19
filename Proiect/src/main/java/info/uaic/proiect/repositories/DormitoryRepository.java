package info.uaic.proiect.repositories;

import info.uaic.proiect.models.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {
    Dormitory findByName(String name);
}
