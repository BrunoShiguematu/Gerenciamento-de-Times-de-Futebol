USE gerenciador_times;

-- 1) JOIN: cada jogador com o nome do seu time
SELECT t.nome AS time, j.nome AS jogador, j.posicao, j.idade, j.gols
FROM jogadores j
JOIN times t ON t.id = j.time_id
ORDER BY t.nome, j.nome;

-- 2) Funções de agregação: resumo de cada time
SELECT t.nome AS time,
       COUNT(j.id) AS total_jogadores,
       ROUND(AVG(j.idade), 1) AS idade_media,
       SUM(j.gols) AS total_gols
FROM times t
LEFT JOIN jogadores j ON j.time_id = t.id
GROUP BY t.id, t.nome
ORDER BY total_gols DESC;

-- 3) Subconsulta: o artilheiro de cada time
SELECT t.nome AS time, j.nome AS artilheiro, j.gols
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