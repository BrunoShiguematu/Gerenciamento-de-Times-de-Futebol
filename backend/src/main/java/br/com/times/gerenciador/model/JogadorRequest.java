package br.com.times.gerenciador.dto;
 
public record JogadorRequest(
        String nome,
        String posicao,
        Integer idade,
        Integer gols,
        Integer numeroCamisa,
        Integer timeId) {
}
