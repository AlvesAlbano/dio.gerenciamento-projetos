package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Funcionario;
import dio.gerenciamento_projetos.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @GetMapping("/todos")
    public ResponseEntity<?> todosFuncionarios(){
        return ResponseEntity.ok(funcionarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> funcionarioPorId(@PathVariable("id") Integer funcionarioId){

        var funcionario = funcionarioRepository.findById(funcionarioId);

        if (funcionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionario não encontrado");
        }

        return ResponseEntity.ok(funcionario);
    }



}
