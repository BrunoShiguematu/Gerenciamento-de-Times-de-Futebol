SET NAMES utf8mb4;

USE gerenciador_times;

INSERT INTO times (id, nome, cidade, ano_fundacao, estadio) VALUES
  (1, 'São Paulo',   'São Paulo',     1930, 'Morumbis'),
  (2, 'Corinthians', 'São Paulo',     1910, 'Neo Química Arena'),
  (3, 'Santos',      'Santos',        1912, 'Vila Belmiro'),
  (4, 'Vasco',       'Rio de Janeiro', 1898, 'São Januário');

INSERT INTO jogadores (nome, posicao, idade, gols, numero_camisa, time_id) VALUES
  ('Rafael',         'Goleiro',  36,  0,  1, 1),
  ('Arboleda',       'Zagueiro', 34,  2,  5, 1),
  ('Alisson',        'Meia',     33,  3,  8, 1),
  ('Lucas Moura',    'Meia',     34,  5,  7, 1),
  ('Calleri',        'Atacante', 32, 14,  9, 1),
  ('Luciano',        'Atacante', 32,  8, 10, 1),

  ('Hugo Souza',     'Goleiro',  26,  0,  1, 2),
  ('André Ramalho',  'Zagueiro', 34,  1,  5, 2),
  ('Matheus Bidu',   'Lateral',  27,  1,  6, 2),
  ('Rodrigo Garro',  'Meia',     28,  6,  8, 2),
  ('Memphis Depay',  'Atacante', 32,  9, 10, 2),
  ('Yuri Alberto',   'Atacante', 25, 16,  9, 2),

  ('Gabriel Brazão', 'Goleiro',  25,  0,  1, 3),
  ('Gil',            'Zagueiro', 38,  1,  4, 3),
  ('João Schmidt',   'Volante',  33,  2,  5, 3),
  ('Neymar',         'Atacante', 34,  9, 10, 3),
  ('Miguelito',      'Atacante', 25,  5,  9, 3),
  ('Guilherme',      'Atacante', 22,  7,  7, 3),

  ('Léo Jardim',     'Goleiro',  31,  0,  1, 4),
  ('João Victor',    'Zagueiro', 27,  3,  4, 4),
  ('Paulo Henrique', 'Lateral',  28,  2,  2, 4),
  ('Coutinho',       'Meia',     33,  8, 10, 4),
  ('Vegetti',        'Atacante', 37, 13, 99, 4),
  ('Rayan',          'Atacante', 20, 10,  7, 4);
