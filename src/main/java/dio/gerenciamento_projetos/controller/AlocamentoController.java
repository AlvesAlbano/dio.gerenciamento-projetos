package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Alocamento;
import dio.gerenciamento_projetos.repository.AlocamentoRepository;
import dio.gerenciamento_projetos.repository.FuncionarioRepository;
import dio.gerenciamento_projetos.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/alocamento")
public class AlocamentoController {

    @Autowired
    private AlocamentoRepository alocamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @GetMapping("/todos")
    public ResponseEntity<?> todosAlocamentos(){

        if (alocamentoRepository.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alocamentos não encontrados");
        }
        return ResponseEntity.ok(alocamentoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> alocamentoPorId(@PathVariable("id") Integer idAlocamento){

        var alocamento = alocamentoRepository.findById(idAlocamento);

        if (alocamento.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alocamento não encontrado");
        }
        return ResponseEntity.ok(alocamento);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarAlocamento(@RequestBody Alocamento alocamento){
        var novoAlocamento = alocamentoRepository.saveAndFlush(alocamento);

        URI uriAlocamento = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/alocamento/{id}")
                .buildAndExpand(novoAlocamento.getIdAlocamento())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Novo alocamento adicionado: " + uriAlocamento);
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagarAlocamento(@PathVariable("id") Integer idAlocamento){
        var alocamento = alocamentoRepository.findById(idAlocamento);

        if (alocamento.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alocamento não encontrado");
        }

        alocamentoRepository.deleteById(idAlocamento);
        return ResponseEntity.ok("Alocamento de id " + alocamento.get().getIdAlocamento() + " apagado com sucesso");
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarAlocamento(@PathVariable("id") Integer idAlocamento, @RequestBody Alocamento alocamento){
        var alocamentoIdentificado = alocamentoRepository.findById(idAlocamento);

        if (alocamentoIdentificado.isPresent()) {
            var alocamentoBuilder = alocamentoIdentificado.get();

            if (alocamento.getFuncionario() != null) {
                var novoFuncionario = funcionarioRepository.findById(alocamento.getFuncionario().getIdFuncionario());
                if (novoFuncionario.isPresent()) {
                    alocamentoBuilder.setFuncionario(novoFuncionario.get());
                }
            }

            if (alocamento.getProjeto() != null) {
                var novoProjeto = projetoRepository.findById(alocamento.getProjeto().getIdProjeto());
                if (novoProjeto.isPresent()) {
                    alocamentoBuilder.setProjeto(novoProjeto.get());
                }
            }

            if (alocamento.getDataAlocacao() != null) {
                alocamentoBuilder.setDataAlocacao(alocamento.getDataAlocacao());
            }

            alocamentoRepository.saveAndFlush(alocamentoBuilder);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Alocamento de id " + alocamentoBuilder.getIdAlocamento() + " editado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Alocamento não encontrado");
    }

}