package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Departamento;
import dio.gerenciamento_projetos.repository.DepartamentoRepository;
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

@Tag(name = "Departamento", description = "Endpoits relacionados ao gerênciamento de departamentos.")
@RestController
@RequestMapping("/departamento")
public class DepartamentoController {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Operation(summary = "Retorna todos os departamentos",description = "Esse endpoint retorna todos os departamentos presentes no banco de dados.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum departamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Departamento encontrado."),
    })
    @GetMapping("/todos")
    public ResponseEntity<?> todosDepartamentos(){

        if (departamentoRepository.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum departamento encontrado");
        }
        return ResponseEntity.ok(departamentoRepository.findAll());
    }

    @Operation(summary = "Retorna um departamento em especifico",description = "Esse endpoint retorna um departamento identificado pelo ID.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum departamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Departamento encontrado."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> departamentoPorId(@PathVariable("id") Integer idDepartamento){

        var departamento = departamentoRepository.findById(idDepartamento);

        if (departamento.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Departamento não encontrado");
        }
        return ResponseEntity.ok(departamento);
    }

    @Operation(summary = "Cria um departamento",description = "Esse endpoint cria um departamento no banco de dados e retorna a URI do mesmo. Apenas o valor 'nomeDepartamento' é necessario.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum departamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Departamento encontrado."),
    })
    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarDepartamento(@RequestBody Departamento departamento){
        var novoDepartamento = departamentoRepository.saveAndFlush(departamento);

        URI uriDepartamento = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/departamento/{id}")
                .buildAndExpand(novoDepartamento.getIdDepartamento())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Novo departamento adicionado: " + uriDepartamento);
    }

    @Operation(summary = "Apaga um departamento em especifico",description = "Esse endpoint apaga um departamento identificado pelo ID do banco de dados.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum departamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Departamento encontrado."),
    })
    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagarDepartamento(@PathVariable("id") Integer idDepartamento) {
        var departamento = departamentoRepository.findById(idDepartamento);

        if (departamento.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Departamento não encontrado");
        }

        departamentoRepository.deleteById(idDepartamento);
        return ResponseEntity.ok("Departamento: " + departamento.get().getNomeDepartamento() + " apagado com sucesso.");
    }

    @Operation(summary = "Edita um departamento em especifico",description = "Esse endpoint edita os valores do departamento identificado pelo ID, apenas o valor 'nomeDepartamento' é necessario.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum departamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Departamento encontrado."),
    })
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarDepartamento(@PathVariable("id") Integer idDepartamento, @RequestBody Departamento departamento){
        var departamentoIdentificado = departamentoRepository.findById(idDepartamento);

        if (departamentoIdentificado.isPresent()){
            var departamentoBuilder = departamentoIdentificado.get();

            departamentoBuilder.setNomeDepartamento(departamento.getNomeDepartamento());
            departamentoRepository.saveAndFlush(departamentoBuilder);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Departamento - " + departamentoBuilder.getNomeDepartamento() + " atualizado");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Departamento não encontrado");

    }
}