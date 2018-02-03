package boot.tale.model.repository;


import boot.tale.model.entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository<Logs,Integer> {
}
