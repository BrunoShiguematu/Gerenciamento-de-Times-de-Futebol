FROM jogadores j
JOIN times t ON t.id = j.time_id
WHERE j.gols = (SELECT MAX(gols) FROM jogadores WHERE time_id = j.time_id)
ORDER BY j.gols DESC;
 
-- 4) Subconsulta com média: jogadores acima da média de gols do próprio time
SELECT t.nome AS time, j.nome AS jogador, j.gols
FROM jogadores j
JOIN times t ON t.id = j.time_id
WHERE j.gols > (SELECT AVG(gols) FROM jogadores WHERE time_id = j.time_id)
ORDER BY t.nome, j.gols DESC;
