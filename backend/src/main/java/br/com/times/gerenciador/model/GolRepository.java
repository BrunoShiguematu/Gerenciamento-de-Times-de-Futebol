package br.com.times.gerenciador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.times.gerenciador.model.Gol;

public interface GolRepository extends JpaRepository<Gol, Integer> {

    // Gols de um campeonato inteiro (JOIN com partidas)
    @Query(value = """
            SELECT g.* FROM gols g
            JOIN partidas p ON p.id = g.partida_id
            WHERE p.campeonato_id = :id
            ORDER BY g.partida_id, g.minuto
            """, nativeQuery = true)
    List<Gol> doCampeonato(@Param("id") Integer campeonatoId);

    List<Gol> findByPartidaId(Integer partidaId);
}
