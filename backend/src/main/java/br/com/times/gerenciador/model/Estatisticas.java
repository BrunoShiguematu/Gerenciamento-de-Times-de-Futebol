package br.com.times.gerenciador.dto;
 
public record Estatisticas(
        long totalJogadores,
        double idadeMedia,
        long totalGols,
        String artilheiro) {
}
