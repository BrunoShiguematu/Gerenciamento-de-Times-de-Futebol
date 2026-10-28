package br.com.times.gerenciador.controller;
 
import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
 
import br.com.times.gerenciador.dto.Estatisticas;
import br.com.times.gerenciador.model.Jogador;
import br.com.times.gerenciador.model.Time;
import br.com.times.gerenciador.repository.JogadorRepository;
import br.com.times.gerenciador.repository.ResumoTime;
import br.com.times.gerenciador.repository.TimeRepository;
 
@RestController
@RequestMapping("/api/times")
public class TimeController {
 
    private final TimeRepository timeRepository;
    private final JogadorRepository jogadorRepository;
 
    public TimeController(TimeRepository timeRepository, JogadorRepository jogadorRepository) {
        this.timeRepository = timeRepository;
        this.jogadorRepository = jogadorRepository;
    }
 
    @GetMapping
    public List<Time> listar() {
        return timeRepository.findAll();
    }
 
    @GetMapping("/{id}/jogadores")
    public List<Jogador> jogadoresDoTime(@PathVariable Integer id) {
        buscarTime(id);
        return jogadorRepository.listarPorTime(id);
    }
 
    @GetMapping("/{id}/estatisticas")
    public Estatisticas estatisticas(@PathVariable Integer id) {
        buscarTime(id);
        ResumoTime resumo = jogadorRepository.resumoDoTime(id);
        String artilheiro = jogadorRepository.artilheiroDoTime(id)
                .map(Jogador::getNome)
                .orElse(null);
        double idadeMedia = Math.round(resumo.getMedia() * 10.0) / 10.0;
        return new Estatisticas(resumo.getTotal(), idadeMedia, resumo.getGols(), artilheiro);
    }
 
    private Time buscarTime(Integer id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time não encontrado"));
    }
}
