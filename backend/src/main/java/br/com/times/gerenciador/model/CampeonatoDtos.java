package br.com.times.gerenciador.dto;

import java.util.List;

import br.com.times.gerenciador.model.Campeonato;
import br.com.times.gerenciador.model.Gol;
import br.com.times.gerenciador.model.Partida;
import br.com.times.gerenciador.repository.Contagem;
import br.com.times.gerenciador.repository.LinhaClassificacao;

public final class CampeonatoDtos {

    private CampeonatoDtos() {}

    public record CampeonatoDetalhe(Campeonato campeonato, List<Partida> partidas, List<Gol> gols,
            List<LinhaClassificacao> classificacao) {}

    public record PlacarRequest(Integer golsMandante, Integer golsVisitante,
            Integer penaltisMandante, Integer penaltisVisitante) {}

    public record NomeRequest(String nome) {}

    public record Ranking(List<Contagem> titulos, List<Contagem> artilharia) {}
}
