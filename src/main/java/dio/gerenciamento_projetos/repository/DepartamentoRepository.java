package dio.gerenciamento_projetos.repository;

import dio.gerenciamento_projetos.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento,Integer> {

}