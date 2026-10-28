package br.com.times.gerenciador.model;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
 
@Entity
@Table(name = "times")
public class Time {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    private String nome;
 
    private String cidade;
 
    @Column(name = "ano_fundacao")
    private Integer anoFundacao;
 
    private String estadio;
 
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
 
    public String getCidade() {
        return cidade;
    }
 
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
 
    public Integer getAnoFundacao() {
        return anoFundacao;
    }
 
    public void setAnoFundacao(Integer anoFundacao) {
        this.anoFundacao = anoFundacao;
    }
 
    public String getEstadio() {
        return estadio;
    }
 
    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }
}
