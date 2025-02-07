package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Funcionario;
import dio.gerenciamento_projetos.repository.AlocamentoRepository;
import dio.gerenciamento_projetos.repository.FuncionarioRepository;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagarFuncionario(@PathVariable("id") Integer idFuncionario){
        var funcionario = funcionarioRepository.findById(idFuncionario);

        if (funcionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionario não encontrado");
        }

        funcionarioRepository.deleteById(idFuncionario);
        return ResponseEntity.ok(funcionario.get().getNomeFuncionario() + " Deletado com sucesso!");
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarFuncionario(@PathVariable("id") Integer idFuncionario, @RequestBody Funcionario funcionario){
        var funcionarioIdentificado = funcionarioRepository.findById(idFuncionario);

        if (funcionarioIdentificado.isPresent()) {
            var funcionarioBuilder = funcionarioIdentificado.get();

            funcionarioBuilder.setNomeFuncionario(funcionario.getNomeFuncionario());
            funcionarioRepository.saveAndFlush(funcionarioBuilder);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Funcionario - " + funcionarioBuilder.getNomeFuncionario() + " atualizado");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Funcionario não encontrado");
    }

    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarFuncionario(@RequestBody Funcionario funcionario){
        var novoFuncionario = funcionarioRepository.saveAndFlush(funcionario);

        URI uriFuncionario = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/funcionario/{id}")
                .buildAndExpand(novoFuncionario.getIdFuncionario())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Novo funcionario adicionado: " + uriFuncionario);
    }

}