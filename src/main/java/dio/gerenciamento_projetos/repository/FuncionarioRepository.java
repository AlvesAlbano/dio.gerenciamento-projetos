package dio.gerenciamento_projetos.repository;

import dio.gerenciamento_projetos.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario,Integer> {
}
