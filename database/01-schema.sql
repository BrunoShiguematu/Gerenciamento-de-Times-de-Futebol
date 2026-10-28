SET NAMES utf8mb4;
 
CREATE DATABASE IF NOT EXISTS gerenciador_times
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
 
USE gerenciador_times;
 
DROP TABLE IF EXISTS jogadores;
DROP TABLE IF EXISTS times;
 
CREATE TABLE times (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  nome          VARCHAR(50) NOT NULL UNIQUE,
  cidade        VARCHAR(50) NOT NULL,
  ano_fundacao  INT         NOT NULL,
  estadio       VARCHAR(80) NOT NULL
);
 
CREATE TABLE jogadores (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  nome           VARCHAR(80) NOT NULL,
  posicao        VARCHAR(20) NOT NULL,
  idade          INT         NOT NULL,
  gols           INT         NOT NULL DEFAULT 0,
  numero_camisa  INT         NOT NULL,
  time_id        INT         NOT NULL,
  CONSTRAINT fk_jogadores_times
    FOREIGN KEY (time_id) REFERENCES times (id)
);
