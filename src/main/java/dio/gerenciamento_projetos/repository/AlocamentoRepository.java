package dio.gerenciamento_projetos.repository;

import dio.gerenciamento_projetos.model.Alocamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlocamentoRepository extends JpaRepository<Alocamento,Integer> {
}
