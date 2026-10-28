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
import org.springframework.web.server.ResponseStatusException;
 
import br.com.times.gerenciador.dto.JogadorRequest;
import br.com.times.gerenciador.model.Jogador;
import br.com.times.gerenciador.model.Time;
import br.com.times.gerenciador.repository.JogadorRepository;
import br.com.times.gerenciador.repository.TimeRepository;
 
@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {
 
    private final JogadorRepository jogadorRepository;
    private final TimeRepository timeRepository;
 
    public JogadorController(JogadorRepository jogadorRepository, TimeRepository timeRepository) {
        this.jogadorRepository = jogadorRepository;
        this.timeRepository = timeRepository;
    }
 
    @GetMapping
    public List<Jogador> listar() {
        return jogadorRepository.findAll();
    }
 
    @GetMapping("/{id}")
    public Jogador buscar(@PathVariable Integer id) {
        return buscarJogador(id);
    }
 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Jogador criar(@RequestBody JogadorRequest dados) {
        Jogador jogador = new Jogador();
        preencher(jogador, dados);
        return jogadorRepository.save(jogador);
    }
 
    @PutMapping("/{id}")
    public Jogador atualizar(@PathVariable Integer id, @RequestBody JogadorRequest dados) {
        Jogador jogador = buscarJogador(id);
        preencher(jogador, dados);
        return jogadorRepository.save(jogador);
    }
 
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer id) {
        buscarJogador(id);
        jogadorRepository.deleteById(id);
    }
 
    private Jogador buscarJogador(Integer id) {
        return jogadorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado"));
    }
 
    private void preencher(Jogador jogador, JogadorRequest dados) {
        if (dados.nome() == null || dados.nome().isBlank()) {
            throw invalido("Informe o nome do jogador");
        }
        if (dados.posicao() == null || dados.posicao().isBlank()) {
            throw invalido("Informe a posição do jogador");
        }
        if (dados.idade() == null || dados.idade() <= 0) {
            throw invalido("Informe uma idade válida");
        }
        if (dados.gols() == null || dados.gols() < 0) {
            throw invalido("Informe a quantidade de gols");
        }
        if (dados.numeroCamisa() == null || dados.numeroCamisa() <= 0) {
            throw invalido("Informe o número da camisa");
        }
        Time time = null;
        if (dados.timeId() != null) {
            time = timeRepository.findById(dados.timeId()).orElse(null);
        }
        if (time == null) {
            throw invalido("Time não encontrado");
        }
 
        jogador.setNome(dados.nome().trim());
        jogador.setPosicao(dados.posicao());
        jogador.setIdade(dados.idade());
        jogador.setGols(dados.gols());
        jogador.setNumeroCamisa(dados.numeroCamisa());
        jogador.setTime(time);
    }
 
    private ResponseStatusException invalido(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
