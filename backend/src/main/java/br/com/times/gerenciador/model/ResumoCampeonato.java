package br.com.times.gerenciador.repository;

import java.time.LocalDateTime;

public interface ResumoCampeonato {
    Integer getId();
    String getNome();
    LocalDateTime getCriadoEm();
    String getStatus();
    String getCampeao();
    Long getTotalGols();
    String getPlacarFinal();
}
