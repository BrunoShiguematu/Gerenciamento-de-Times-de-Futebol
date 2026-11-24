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
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "campeonato_id")
    private Integer campeonatoId;

    private String fase;

    private Integer ordem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mandante_id")
    private Time mandante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitante_id")
    private Time visitante;

    @Column(name = "gols_mandante")
    private Integer golsMandante;

    @Column(name = "gols_visitante")
    private Integer golsVisitante;

    @Column(name = "penaltis_mandante")
    private Integer penaltisMandante;

    @Column(name = "penaltis_visitante")
    private Integer penaltisVisitante;

    public boolean isJogada() {
        return golsMandante != null && golsVisitante != null;
    }

    // Vencedor no mata-mata: placar normal e, se empatar, os pênaltis
    public Time vencedor() {
        if (!isJogada()) return null;
        if (golsMandante > golsVisitante) return mandante;
        if (golsVisitante > golsMandante) return visitante;
        if (penaltisMandante == null || penaltisVisitante == null) return null;
        return penaltisMandante > penaltisVisitante ? mandante : visitante;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCampeonatoId() { return campeonatoId; }
    public void setCampeonatoId(Integer campeonatoId) { this.campeonatoId = campeonatoId; }
    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }
    public Time getMandante() { return mandante; }
    public void setMandante(Time mandante) { this.mandante = mandante; }
    public Time getVisitante() { return visitante; }
    public void setVisitante(Time visitante) { this.visitante = visitante; }
    public Integer getGolsMandante() { return golsMandante; }
    public void setGolsMandante(Integer golsMandante) { this.golsMandante = golsMandante; }
    public Integer getGolsVisitante() { return golsVisitante; }
    public void setGolsVisitante(Integer golsVisitante) { this.golsVisitante = golsVisitante; }
    public Integer getPenaltisMandante() { return penaltisMandante; }
    public void setPenaltisMandante(Integer penaltisMandante) { this.penaltisMandante = penaltisMandante; }
    public Integer getPenaltisVisitante() { return penaltisVisitante; }
    public void setPenaltisVisitante(Integer penaltisVisitante) { this.penaltisVisitante = penaltisVisitante; }
}
