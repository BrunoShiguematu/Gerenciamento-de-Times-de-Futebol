package br.com.times.gerenciador.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.times.gerenciador.dto.CampeonatoDtos.CampeonatoDetalhe;
import br.com.times.gerenciador.dto.CampeonatoDtos.NomeRequest;
import br.com.times.gerenciador.dto.CampeonatoDtos.PlacarRequest;
import br.com.times.gerenciador.dto.CampeonatoDtos.Ranking;
import br.com.times.gerenciador.model.Campeonato;
import br.com.times.gerenciador.repository.CampeonatoRepository;
import br.com.times.gerenciador.repository.ResumoCampeonato;
import br.com.times.gerenciador.service.CampeonatoService;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    private final CampeonatoService service;
    private final CampeonatoRepository repository;

    public CampeonatoController(CampeonatoService service, CampeonatoRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public List<ResumoCampeonato> historico() {
        return repository.historico();
    }

    @GetMapping("/ranking")
    public Ranking ranking() {
        return new Ranking(repository.titulosPorTime(), repository.artilhariaHistorica());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampeonatoDetalhe criar(@RequestBody(required = false) NomeRequest dados) {
        Campeonato c = service.criar(dados == null ? null : dados.nome());
        return service.detalhe(c.getId());
    }

    @GetMapping("/{id}")
    public CampeonatoDetalhe detalhe(@PathVariable Integer id) {
        return service.detalhe(id);
    }

    @PutMapping("/{id}")
    public Campeonato renomear(@PathVariable Integer id, @RequestBody NomeRequest dados) {
        return service.renomear(id, dados.nome());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        service.excluir(id);
    }

    @PostMapping("/partidas/{partidaId}/simular")
    public CampeonatoDetalhe simular(@PathVariable Integer partidaId) {
        return service.simularPartida(partidaId);
    }

    @PutMapping("/partidas/{partidaId}")
    public CampeonatoDetalhe placar(@PathVariable Integer partidaId, @RequestBody PlacarRequest placar) {
        return service.registrarPlacar(partidaId, placar);
    }
}
