package dio.gerenciamento_projetos.repository;

import dio.gerenciamento_projetos.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto,Integer> {
}
