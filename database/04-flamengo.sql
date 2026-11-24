SET NAMES utf8mb4;

USE gerenciador_times;


INSERT INTO times (id, nome, cidade, ano_fundacao, estadio) VALUES
  (5, 'Flamengo', 'Rio de Janeiro', 1895, 'Maracanã');

INSERT INTO jogadores (nome, posicao, idade, gols, numero_camisa, time_id) VALUES
  ('Rossi',            'Goleiro',  30,  0,  1, 5),
  ('Léo Ortiz',        'Zagueiro', 29,  2,  3, 5),
  ('Arrascaeta',       'Meia',     31, 11, 10, 5),
  ('Jorginho',         'Volante',  33,  3, 21, 5),
  ('Pedro',            'Atacante', 28, 15,  9, 5),
  ('Bruno Henrique',   'Atacante', 34,  9, 27, 5);