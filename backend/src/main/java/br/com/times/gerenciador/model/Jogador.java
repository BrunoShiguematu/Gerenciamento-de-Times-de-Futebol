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
@Table(name = "jogadores")
public class Jogador {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    private String nome;
 
    private String posicao;
 
    private Integer idade;
 
    private Integer gols;
 
    @Column(name = "numero_camisa")
    private Integer numeroCamisa;
 
    @ManyToOne(optional = false)
    @JoinColumn(name = "time_id")
    private Time time;
 
    public Integer getId() {
        return id;
    }
 
    public void setId(Integer id) {
        this.id = id;
    }
 
    public String getNome() {
        return nome;
    }
 
    public void setNome(String nome) {
        this.nome = nome;
    }
 
    public String getPosicao() {
        return posicao;
    }
 
    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }
 
    public Integer getIdade() {
        return idade;
    }
 
    public void setIdade(Integer idade) {
        this.idade = idade;
    }
 
    public Integer getGols() {
        return gols;
    }
 
    public void setGols(Integer gols) {
        this.gols = gols;
    }
 
    public Integer getNumeroCamisa() {
        return numeroCamisa;
    }
 
    public void setNumeroCamisa(Integer numeroCamisa) {
        this.numeroCamisa = numeroCamisa;
    }
 
    public Time getTime() {
        return time;
    }
 
    public void setTime(Time time) {
        this.time = time;
    }
}
