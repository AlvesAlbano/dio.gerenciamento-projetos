package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Alocamento;
import dio.gerenciamento_projetos.repository.AlocamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/alocamento")
public class AlocamentoController {

    @Autowired
    private AlocamentoRepository alocamentoRepository;

    @GetMapping("/todos")
    public ResponseEntity<?> todosAlocamentos(){
        return ResponseEntity.ok(alocamentoRepository.findAll());
    }

}