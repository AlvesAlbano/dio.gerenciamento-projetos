package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Funcionario;
import dio.gerenciamento_projetos.repository.FuncionarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Funcionario", description = "Endpoits relacionados ao gerênciamento de funcionarios.")
@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Operation(summary = "Retorna todos os funcionarios",description = "Esse endpoint retorna todos os funcionarios presentes no banco de dados.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum funcionario encontrado."),
            @ApiResponse(responseCode = "200",description = "Funcionario encontrado."),
    })
    @GetMapping("/todos")
    public ResponseEntity<?> todosFuncionarios(){

        if (funcionarioRepository.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum funcionario encontrado");
        }
        return ResponseEntity.ok(funcionarioRepository.findAll());
    }

    @Operation(summary = "Retorna um funcionario em especifico",description = "Esse endpoint retorna um funcionario identificado pelo ID.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "404",description = "Nenhum funcionario encontrado."),
        @ApiResponse(responseCode = "200",description = "Funcioanrio encontrado."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> funcionarioPorId(@PathVariable("id") Integer funcionarioId){

        var funcionario = funcionarioRepository.findById(funcionarioId);

        if (funcionario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Funcionario não encontrado");
        }
        return ResponseEntity.ok(funcionario);
    }

    @Operation(summary = "Apaga um funcionario em especifico",description = "Esse endpoint apaga um funcionario identificado pelo ID no banco de dados.")
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

    @Operation(summary = "Edita um funcionario em especifico",description = "Esse endpoint edita o funcionario identificado pelo ID, apenas o valor 'nomeFuncionario' é necessario.")
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

    @Operation(summary = "Cria um funcionario",description = "Esse endpoint cria um funcionario, apenas o valor 'nomeFuncionario' é necessario, a URI do funcionario contendo suas informações é retornada.")
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