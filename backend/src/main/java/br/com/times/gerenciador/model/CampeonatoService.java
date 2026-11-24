package br.com.times.gerenciador.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.times.gerenciador.dto.CampeonatoDtos.CampeonatoDetalhe;
import br.com.times.gerenciador.dto.CampeonatoDtos.PlacarRequest;
import br.com.times.gerenciador.model.Campeonato;
import br.com.times.gerenciador.model.Gol;
import br.com.times.gerenciador.model.Jogador;
import br.com.times.gerenciador.model.Partida;
import br.com.times.gerenciador.model.Time;
import br.com.times.gerenciador.repository.CampeonatoRepository;
import br.com.times.gerenciador.repository.GolRepository;
import br.com.times.gerenciador.repository.JogadorRepository;
import br.com.times.gerenciador.repository.LinhaClassificacao;
import br.com.times.gerenciador.repository.PartidaRepository;
import br.com.times.gerenciador.repository.TimeRepository;

@Service
public class CampeonatoService {

    private final CampeonatoRepository campeonatos;
    private final PartidaRepository partidas;
    private final GolRepository gols;
    private final TimeRepository times;
    private final JogadorRepository jogadores;
    private final Random sorteio = new Random();

    public CampeonatoService(CampeonatoRepository campeonatos, PartidaRepository partidas, GolRepository gols,
            TimeRepository times, JogadorRepository jogadores) {
        this.campeonatos = campeonatos;
        this.partidas = partidas;
        this.gols = gols;
        this.times = times;
        this.jogadores = jogadores;
    }

    // Cria o campeonato e os jogos da fase de grupos (todos contra todos, pontos corridos).
    // Com 4 times são 6 jogos; com 5 times, 10 jogos. Os 4 primeiros vão para a semifinal.
    @Transactional
    public Campeonato criar(String nome) {
        if (times.count() < 4) {
            throw invalido("Cadastre pelo menos 4 times para simular");
        }
        Campeonato c = new Campeonato();
        long numero = campeonatos.count() + 1;
        c.setNome(nome == null || nome.isBlank() ? "Campeonato #" + numero : nome.trim());
        c.setCriadoEm(LocalDateTime.now());
        c.setStatus("EM_ANDAMENTO");
        c = campeonatos.save(c);

        List<Time> lista = times.findAll();
        int ordem = 1;
        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {
                novaPartida(c.getId(), "GRUPO", ordem++, lista.get(i), lista.get(j));
            }
        }
        return c;
    }

    public CampeonatoDetalhe detalhe(Integer id) {
        Campeonato c = buscar(id);
        return new CampeonatoDetalhe(c, partidas.findByCampeonatoIdOrderByIdAsc(id), gols.doCampeonato(id),
                classificacao(id));
    }

    public List<LinhaClassificacao> classificacao(Integer id) {
        List<LinhaClassificacao> linhas = new ArrayList<>(campeonatos.classificacao(id));
        linhas.sort(Comparator.comparingLong(LinhaClassificacao::getPontos).reversed()
                .thenComparing(Comparator.comparingLong(LinhaClassificacao::getSaldo).reversed())
                .thenComparing(Comparator.comparingLong(LinhaClassificacao::getGolsPro).reversed())
                .thenComparing(LinhaClassificacao::getTime));
        return linhas;
    }

    // Simula uma partida: placar ponderado pela força do elenco e autores dos gols
    @Transactional
    public CampeonatoDetalhe simularPartida(Integer partidaId) {
        Partida p = buscarPartida(partidaId);
        double forcaM = forca(p.getMandante());
        double forcaV = forca(p.getVisitante());
        double media = (forcaM + forcaV) / 2;
        int gm = poisson(1.4 * Math.sqrt(forcaM / media));
        int gv = poisson(1.2 * Math.sqrt(forcaV / media));

        limparResultado(p);
        p.setGolsMandante(gm);
        p.setGolsVisitante(gv);
        if (!p.getFase().equals("GRUPO") && gm == gv) {
            int pm, pv;
            do {
                pm = 2 + sorteio.nextInt(4);
                pv = 2 + sorteio.nextInt(4);
            } while (pm == pv);
            p.setPenaltisMandante(pm);
            p.setPenaltisVisitante(pv);
        }
        partidas.save(p);
        sortearAutores(p.getId(), p.getMandante(), gm);
        sortearAutores(p.getId(), p.getVisitante(), gv);
        avancar(p);
        return detalhe(p.getCampeonatoId());
    }

    // Placar digitado à mão (sem autores dos gols)
    @Transactional
    public CampeonatoDetalhe registrarPlacar(Integer partidaId, PlacarRequest placar) {
        Partida p = buscarPartida(partidaId);
        if (placar.golsMandante() == null || placar.golsVisitante() == null
                || placar.golsMandante() < 0 || placar.golsVisitante() < 0) {
            throw invalido("Informe os gols dos dois times");
        }
        boolean mataMata = !p.getFase().equals("GRUPO");
        boolean empate = placar.golsMandante().equals(placar.golsVisitante());
        if (mataMata && empate && (placar.penaltisMandante() == null || placar.penaltisVisitante() == null
                || placar.penaltisMandante().equals(placar.penaltisVisitante()))) {
            throw invalido("Empate no mata-mata: informe os pênaltis (sem empate)");
        }
        limparResultado(p);
        p.setGolsMandante(placar.golsMandante());
        p.setGolsVisitante(placar.golsVisitante());
        if (mataMata && empate) {
            p.setPenaltisMandante(placar.penaltisMandante());
            p.setPenaltisVisitante(placar.penaltisVisitante());
        }
        partidas.save(p);
        avancar(p);
        return detalhe(p.getCampeonatoId());
    }

    @Transactional
    public Campeonato renomear(Integer id, String nome) {
        if (nome == null || nome.isBlank()) throw invalido("Informe o nome");
        Campeonato c = buscar(id);
        c.setNome(nome.trim());
        return campeonatos.save(c);
    }

    @Transactional
    public void excluir(Integer id) {
        buscar(id);
        campeonatos.deleteById(id); // partidas e gols saem juntos (ON DELETE CASCADE)
    }

    // Quando uma fase termina, cria a próxima; editar uma fase refaz as seguintes
    private void avancar(Partida alterada) {
        Integer id = alterada.getCampeonatoId();
        Campeonato c = buscar(id);
        if (alterada.getFase().equals("GRUPO")) {
            apagarFase(id, "SEMI");
            apagarFase(id, "FINAL");
        } else if (alterada.getFase().equals("SEMI")) {
            apagarFase(id, "FINAL");
        }
        c.setCampeao(null);
        c.setStatus("EM_ANDAMENTO");

        List<Partida> grupo = partidas.findByCampeonatoIdAndFaseOrderByOrdemAsc(id, "GRUPO");
        List<Partida> semis = partidas.findByCampeonatoIdAndFaseOrderByOrdemAsc(id, "SEMI");
        List<Partida> fin = partidas.findByCampeonatoIdAndFaseOrderByOrdemAsc(id, "FINAL");

        if (grupo.stream().allMatch(Partida::isJogada) && semis.isEmpty()) {
            List<LinhaClassificacao> tab = classificacao(id);
            novaPartida(id, "SEMI", 1, time(tab.get(0)), time(tab.get(3)));
            novaPartida(id, "SEMI", 2, time(tab.get(1)), time(tab.get(2)));
        } else if (semis.size() == 2 && semis.stream().allMatch(Partida::isJogada) && fin.isEmpty()) {
            novaPartida(id, "FINAL", 1, semis.get(0).vencedor(), semis.get(1).vencedor());
        } else if (fin.size() == 1 && fin.get(0).isJogada()) {
            c.setCampeao(fin.get(0).vencedor());
            c.setStatus("FINALIZADO");
        }
        campeonatos.save(c);
    }

    private void apagarFase(Integer campeonatoId, String fase) {
        partidas.deleteAll(partidas.findByCampeonatoIdAndFaseOrderByOrdemAsc(campeonatoId, fase));
        partidas.flush();
    }

    private void limparResultado(Partida p) {
        gols.deleteAll(gols.findByPartidaId(p.getId()));
        p.setPenaltisMandante(null);
        p.setPenaltisVisitante(null);
    }

    private void novaPartida(Integer campeonatoId, String fase, int ordem, Time mandante, Time visitante) {
        Partida p = new Partida();
        p.setCampeonatoId(campeonatoId);
        p.setFase(fase);
        p.setOrdem(ordem);
        p.setMandante(mandante);
        p.setVisitante(visitante);
        partidas.save(p);
    }

    // Força = total de gols do elenco (consulta com SUM que já existia)
    private double forca(Time t) {
        return Math.max(1, jogadores.resumoDoTime(t.getId()).getGols());
    }

    private int poisson(double lambda) {
        double limite = Math.exp(-lambda), produto = 1;
        int k = 0;
        do {
            k++;
            produto *= sorteio.nextDouble();
        } while (produto > limite);
        return Math.min(k - 1, 7);
    }

    // Quem marcou: jogadores com mais gols na carreira têm mais chance
    private void sortearAutores(Integer partidaId, Time time, int quantidade) {
        List<Jogador> elenco = jogadores.listarPorTime(time.getId()).stream()
                .filter(j -> !j.getPosicao().equals("Goleiro"))
                .toList();
        if (elenco.isEmpty()) return;
        int pesoTotal = elenco.stream().mapToInt(j -> j.getGols() + 1).sum();
        for (int i = 0; i < quantidade; i++) {
            int r = sorteio.nextInt(pesoTotal);
            Jogador autor = elenco.get(0);
            for (Jogador j : elenco) {
                r -= j.getGols() + 1;
                if (r < 0) { autor = j; break; }
            }
            Gol g = new Gol();
            g.setPartidaId(partidaId);
            g.setJogador(autor);
            g.setMinuto(1 + sorteio.nextInt(90));
            gols.save(g);
        }
    }

    private Time time(LinhaClassificacao linha) {
        return times.findById(linha.getTimeId()).orElseThrow();
    }

    private Campeonato buscar(Integer id) {
        return campeonatos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campeonato não encontrado"));
    }

    private Partida buscarPartida(Integer id) {
        return partidas.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));
    }

    private ResponseStatusException invalido(String mensagem) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
