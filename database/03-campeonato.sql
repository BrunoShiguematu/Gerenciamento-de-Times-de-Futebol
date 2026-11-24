SET NAMES utf8mb4;

USE gerenciador_times;

DROP TABLE IF EXISTS gols;
DROP TABLE IF EXISTS partidas;
DROP TABLE IF EXISTS campeonatos;


CREATE TABLE campeonatos (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  nome        VARCHAR(80) NOT NULL,
  criado_em   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status      VARCHAR(20) NOT NULL DEFAULT 'EM_ANDAMENTO',
  campeao_id  INT NULL,
  CONSTRAINT fk_campeonatos_campeao
    FOREIGN KEY (campeao_id) REFERENCES times (id)
);


CREATE TABLE partidas (
  id                  INT AUTO_INCREMENT PRIMARY KEY,
  campeonato_id       INT         NOT NULL,
  fase                VARCHAR(10) NOT NULL,
  ordem               INT         NOT NULL,
  mandante_id         INT         NOT NULL,
  visitante_id        INT         NOT NULL,
  gols_mandante       INT NULL,
  gols_visitante      INT NULL,
  penaltis_mandante   INT NULL,
  penaltis_visitante  INT NULL,
  CONSTRAINT fk_partidas_campeonato
    FOREIGN KEY (campeonato_id) REFERENCES campeonatos (id) ON DELETE CASCADE,
  CONSTRAINT fk_partidas_mandante  FOREIGN KEY (mandante_id)  REFERENCES times (id),
  CONSTRAINT fk_partidas_visitante FOREIGN KEY (visitante_id) REFERENCES times (id)
);


CREATE TABLE gols (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  partida_id  INT NOT NULL,
  jogador_id  INT NOT NULL,
  minuto      INT NOT NULL,
  CONSTRAINT fk_gols_partida FOREIGN KEY (partida_id) REFERENCES partidas (id) ON DELETE CASCADE,
  CONSTRAINT fk_gols_jogador FOREIGN KEY (jogador_id) REFERENCES jogadores (id) ON DELETE CASCADE
);