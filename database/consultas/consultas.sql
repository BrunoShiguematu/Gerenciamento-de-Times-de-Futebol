USE gerenciador_times;


SELECT t.nome AS time, j.nome AS jogador, j.posicao, j.idade, j.gols
FROM jogadores j
JOIN times t ON t.id = j.time_id
ORDER BY t.nome, j.nome;


SELECT t.nome AS time,
       COUNT(j.id) AS total_jogadores,
       ROUND(AVG(j.idade), 1) AS idade_media,
       SUM(j.gols) AS total_gols
FROM times t
LEFT JOIN jogadores j ON j.time_id = t.id
GROUP BY t.id, t.nome
ORDER BY total_gols DESC;


SELECT t.nome AS time, j.nome AS artilheiro, j.gols
FROM jogadores j
JOIN times t ON t.id = j.time_id
WHERE j.gols = (SELECT MAX(gols) FROM jogadores WHERE time_id = j.time_id)
ORDER BY j.gols DESC;


SELECT t.nome AS time, j.nome AS jogador, j.gols
FROM jogadores j
JOIN times t ON t.id = j.time_id
WHERE j.gols > (SELECT AVG(gols) FROM jogadores WHERE time_id = j.time_id)
ORDER BY t.nome, j.gols DESC;


SELECT t.nome AS time,
       COUNT(p.id) AS jogos,
       SUM(CASE WHEN (p.mandante_id = t.id AND p.gols_mandante > p.gols_visitante)
                  OR (p.visitante_id = t.id AND p.gols_visitante > p.gols_mandante) THEN 1 ELSE 0 END) AS vitorias,
       SUM(CASE WHEN p.gols_mandante = p.gols_visitante THEN 1 ELSE 0 END) AS empates,
       SUM(CASE WHEN p.mandante_id = t.id THEN p.gols_mandante ELSE p.gols_visitante END) AS gols_pro,
       SUM(CASE WHEN p.mandante_id = t.id THEN p.gols_visitante ELSE p.gols_mandante END) AS gols_contra
FROM times t
LEFT JOIN partidas p
       ON p.campeonato_id = 1 AND p.fase = 'GRUPO' AND p.gols_mandante IS NOT NULL
      AND (p.mandante_id = t.id OR p.visitante_id = t.id)
GROUP BY t.id, t.nome
ORDER BY vitorias DESC, gols_pro DESC;


SELECT t.nome AS time, COUNT(c.id) AS titulos
FROM times t
LEFT JOIN campeonatos c ON c.campeao_id = t.id
GROUP BY t.id, t.nome
ORDER BY titulos DESC, t.nome;


SELECT j.nome AS jogador, t.nome AS time, COUNT(g.id) AS gols
FROM gols g
JOIN jogadores j ON j.id = g.jogador_id
JOIN times t ON t.id = j.time_id
GROUP BY j.id, j.nome, t.nome
ORDER BY gols DESC
LIMIT 10;


SELECT c.id, c.nome, SUM(p.gols_mandante + p.gols_visitante) AS total_gols
FROM campeonatos c
JOIN partidas p ON p.campeonato_id = c.id
GROUP BY c.id, c.nome
HAVING total_gols > (
  SELECT AVG(soma) FROM (
    SELECT SUM(gols_mandante + gols_visitante) AS soma
    FROM partidas GROUP BY campeonato_id
  ) AS por_campeonato
);