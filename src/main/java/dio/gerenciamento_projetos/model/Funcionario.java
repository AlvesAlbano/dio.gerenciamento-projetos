package dio.gerenciamento_projetos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Funcionarios")
public class Funcionario {

    @Id
    @Column(name = "id_funcionario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFuncionario;

    @Column(name = "nome_funcionario")
    private String nomeFuncionario;


    @JoinColumn(name = "id_departamento")
    @ManyToOne
    private Departamento departamento;

    public Funcionario(){}

    public Funcionario(Integer idFuncionario, String nomeFuncionario, Departamento departamento) {
        this.idFuncionario = idFuncionario;
        this.nomeFuncionario = nomeFuncionario;
        this.departamento = departamento;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}