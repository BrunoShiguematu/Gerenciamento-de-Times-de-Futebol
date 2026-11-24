package br.com.times.gerenciador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.times.gerenciador.model.Campeonato;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Integer> {

    // Histórico: JOIN com times (campeão) + agregação de partidas (SUM)
    @Query(value = """
            SELECT c.id AS id, c.nome AS nome, c.criado_em AS criadoEm, c.status AS status,
                   t.nome AS campeao,
                   CAST(COALESCE(SUM(p.gols_mandante + p.gols_visitante), 0) AS SIGNED) AS totalGols,
                   (SELECT CONCAT(m.nome, ' ', f.gols_mandante, ' x ', f.gols_visitante, ' ', v.nome,
                           IF(f.penaltis_mandante IS NULL, '',
                              CONCAT(' (pên. ', f.penaltis_mandante, ' x ', f.penaltis_visitante, ')')))
                      FROM partidas f
                      JOIN times m ON m.id = f.mandante_id
                      JOIN times v ON v.id = f.visitante_id
                     WHERE f.campeonato_id = c.id AND f.fase = 'FINAL' AND f.gols_mandante IS NOT NULL
                     LIMIT 1) AS placarFinal
            FROM campeonatos c
            LEFT JOIN times t ON t.id = c.campeao_id
            LEFT JOIN partidas p ON p.campeonato_id = c.id
            GROUP BY c.id, c.nome, c.criado_em, c.status, t.nome
            ORDER BY c.criado_em DESC, c.id DESC
            """, nativeQuery = true)
    List<ResumoCampeonato> historico();

    // Classificação do grupo calculada no SQL (JOIN + SUM com CASE)
    @Query(value = """
            SELECT t.id AS timeId, t.nome AS time,
                   CAST(COUNT(p.id) AS SIGNED) AS jogos,
                   CAST(COALESCE(SUM(CASE WHEN (p.mandante_id = t.id AND p.gols_mandante > p.gols_visitante)
                                         OR (p.visitante_id = t.id AND p.gols_visitante > p.gols_mandante) THEN 1 ELSE 0 END), 0) AS SIGNED) AS vitorias,
                   CAST(COALESCE(SUM(CASE WHEN p.gols_mandante = p.gols_visitante THEN 1 ELSE 0 END), 0) AS SIGNED) AS empates,
                   CAST(COALESCE(SUM(CASE WHEN p.mandante_id = t.id THEN p.gols_mandante ELSE p.gols_visitante END), 0) AS SIGNED) AS golsPro,
                   CAST(COALESCE(SUM(CASE WHEN p.mandante_id = t.id THEN p.gols_visitante ELSE p.gols_mandante END), 0) AS SIGNED) AS golsContra
            FROM times t
            LEFT JOIN partidas p
                   ON p.campeonato_id = :id AND p.fase = 'GRUPO' AND p.gols_mandante IS NOT NULL
                  AND (p.mandante_id = t.id OR p.visitante_id = t.id)
            WHERE EXISTS (SELECT 1 FROM partidas x
                           WHERE x.campeonato_id = :id
                             AND (x.mandante_id = t.id OR x.visitante_id = t.id))
            GROUP BY t.id, t.nome
            """, nativeQuery = true)
    List<LinhaClassificacao> classificacao(@Param("id") Integer id);

    // Títulos por time (COUNT + GROUP BY)
    @Query(value = """
            SELECT t.nome AS nome, CAST(COUNT(c.id) AS SIGNED) AS total
            FROM times t
            LEFT JOIN campeonatos c ON c.campeao_id = t.id
            GROUP BY t.id, t.nome
            ORDER BY total DESC, t.nome
            """, nativeQuery = true)
    List<Contagem> titulosPorTime();

    // Artilharia de todos os campeonatos (JOIN de 3 tabelas + COUNT)
    @Query(value = """
            SELECT CONCAT(j.nome, ' (', t.nome, ')') AS nome, CAST(COUNT(g.id) AS SIGNED) AS total
            FROM gols g
            JOIN jogadores j ON j.id = g.jogador_id
            JOIN times t ON t.id = j.time_id
            GROUP BY j.id, j.nome, t.nome
            ORDER BY total DESC, j.nome
            LIMIT 10
            """, nativeQuery = true)
    List<Contagem> artilhariaHistorica();
}
