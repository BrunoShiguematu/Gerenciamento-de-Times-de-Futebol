package br.com.times.gerenciador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.times.gerenciador.model.Partida;

public interface PartidaRepository extends JpaRepository<Partida, Integer> {

    List<Partida> findByCampeonatoIdOrderByIdAsc(Integer campeonatoId);

    List<Partida> findByCampeonatoIdAndFaseOrderByOrdemAsc(Integer campeonatoId, String fase);
}
