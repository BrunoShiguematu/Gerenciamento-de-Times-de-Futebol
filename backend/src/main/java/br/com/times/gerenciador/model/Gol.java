package br.com.times.gerenciador.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "gols")
public class Gol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "partida_id")
    private Integer partidaId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jogador_id")
    private Jogador jogador;

    private Integer minuto;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPartidaId() { return partidaId; }
    public void setPartidaId(Integer partidaId) { this.partidaId = partidaId; }
    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }
    public Integer getMinuto() { return minuto; }
    public void setMinuto(Integer minuto) { this.minuto = minuto; }
}
