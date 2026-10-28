package br.com.times.gerenciador.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import br.com.times.gerenciador.model.Time;
 
public interface TimeRepository extends JpaRepository<Time, Integer> {
}
