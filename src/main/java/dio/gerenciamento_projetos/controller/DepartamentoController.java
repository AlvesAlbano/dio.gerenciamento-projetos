package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Departamento;
import dio.gerenciamento_projetos.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/departamento")
public class DepartamentoController {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @GetMapping("/todos")
    public ResponseEntity<?> todosDepartamentos(){

        if (departamentoRepository.findAll().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum departamento encontrado");
        }
        return ResponseEntity.ok(departamentoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> departamentoPorId(@PathVariable("id") Integer idDepartamento){

        var departamento = departamentoRepository.findById(idDepartamento);

        if (departamento.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Departamento não encontrado");
        }

        return ResponseEntity.ok(departamento);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarDepartamento(@RequestBody Departamento departamento){
        var novoDepartamento = departamentoRepository.saveAndFlush(departamento);

        URI uriDepartamento = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/departamento/{id}")
                .buildAndExpand(novoDepartamento.getIdDepartamento())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Novo departamento adicionado: " + uriDepartamento);
    }

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
}