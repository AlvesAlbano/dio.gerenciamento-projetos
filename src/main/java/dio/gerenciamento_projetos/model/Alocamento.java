package dio.gerenciamento_projetos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "Alocamentos")
public class Alocamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alocamento")
    private Integer idAlocamento;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_alocacao")
    private LocalDate dataAlocacao;

    public Alocamento(){}

    public Alocamento(Integer idAlocamento, Funcionario funcionario, Projeto projeto, LocalDate dataAlocacao) {
        this.idAlocamento = idAlocamento;
        this.funcionario = funcionario;
        this.projeto = projeto;
        this.dataAlocacao = dataAlocacao;
    }

    public Integer getIdAlocamento() {
        return idAlocamento;
    }

    public void setIdAlocamento(Integer idAlocamento) {
        this.idAlocamento = idAlocamento;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public LocalDate getDataAlocacao() {
        return dataAlocacao;
    }

    public void setDataAlocacao(LocalDate dataAlocacao) {
        this.dataAlocacao = dataAlocacao;
    }
}
