package br.com.times.gerenciador.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "campeonatos")
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    private String status;

    @ManyToOne
    @JoinColumn(name = "campeao_id")
    private Time campeao;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Time getCampeao() { return campeao; }
    public void setCampeao(Time campeao) { this.campeao = campeao; }
}
