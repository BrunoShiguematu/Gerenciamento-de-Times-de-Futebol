package br.com.times.gerenciador.repository;
 
import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 
import br.com.times.gerenciador.model.Jogador;
 
public interface JogadorRepository extends JpaRepository<Jogador, Integer> {
 
    // Consulta com JOIN: jogadores de um time
    @Query(value = """
            SELECT j.*
            FROM jogadores j
            JOIN times t ON t.id = j.time_id
            WHERE t.id = :timeId
            ORDER BY j.nome
            """, nativeQuery = true)
    List<Jogador> listarPorTime(@Param("timeId") Integer timeId);
 
    // Consulta com funções de agregação: COUNT, AVG e SUM
    @Query(value = """
            SELECT COUNT(*) AS total,
                   CAST(COALESCE(AVG(idade), 0) AS DOUBLE) AS media,
                   CAST(COALESCE(SUM(gols), 0) AS SIGNED) AS gols
            FROM jogadores
            WHERE time_id = :timeId
            """, nativeQuery = true)
    ResumoTime resumoDoTime(@Param("timeId") Integer timeId);
 
    // Consulta com subconsulta: o artilheiro do time
    @Query(value = """
            SELECT *
            FROM jogadores
            WHERE time_id = :timeId
              AND gols = (SELECT MAX(gols) FROM jogadores WHERE time_id = :timeId)
            ORDER BY nome
            LIMIT 1
            """, nativeQuery = true)
    Optional<Jogador> artilheiroDoTime(@Param("timeId") Integer timeId);
}
