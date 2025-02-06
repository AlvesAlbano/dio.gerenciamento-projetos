package dio.gerenciamento_projetos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Departamentos")
public class Departamento {

    @Id
    @Column(name = "id_departamento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDepartamento;

    @Column(name = "nome_departamento")
    private String nomeDepartamento;

    @Column(name = "quantidade_funcionarios")
    private Integer quantidadeFuncionarios;

    public Departamento(){}

    public Departamento(Integer idDepartamento, String nomeDepartamento) {
        this.idDepartamento = idDepartamento;
        this.nomeDepartamento = nomeDepartamento;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNomeDepartamento() {
        return nomeDepartamento;
    }

    public void setNomeDepartamento(String nomeDepartamento) {
        this.nomeDepartamento = nomeDepartamento;
    }

    public Integer getQuantidadeFuncionarios() {
        return quantidadeFuncionarios;
    }

    public void setQuantidadeFuncionarios(Integer quantidadeFuncionarios) {
        this.quantidadeFuncionarios = quantidadeFuncionarios;
    }
}
