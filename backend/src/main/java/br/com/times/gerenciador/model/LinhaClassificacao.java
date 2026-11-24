package br.com.times.gerenciador.repository;

public interface LinhaClassificacao {
    Integer getTimeId();
    String getTime();
    Long getJogos();
    Long getVitorias();
    Long getEmpates();
    Long getGolsPro();
    Long getGolsContra();

    default long getDerrotas() { return getJogos() - getVitorias() - getEmpates(); }
    default long getPontos() { return getVitorias() * 3 + getEmpates(); }
    default long getSaldo() { return getGolsPro() - getGolsContra(); }
}
