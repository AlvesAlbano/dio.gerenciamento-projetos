package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Alocamento;
import dio.gerenciamento_projetos.repository.AlocamentoRepository;
import dio.gerenciamento_projetos.repository.FuncionarioRepository;
import dio.gerenciamento_projetos.repository.ProjetoRepository;
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

@Tag(name = "Alocamento", description = "Endpoits relacionados ao gerênciamento de alocamentos.")
@RestController
@RequestMapping("/alocamento")
public class AlocamentoController {

    @Autowired
    private AlocamentoRepository alocamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Operation(summary = "Retorna todos os alocamentos",description = "Esse endpoint retorna todos os alocamentos presentes no banco de dados.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum alocamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Alocamento encontrado."),
    })
    @GetMapping("/todos")
    public ResponseEntity<?> todosAlocamentos(){

        if (alocamentoRepository.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alocamentos não encontrados");
        }
        return ResponseEntity.ok(alocamentoRepository.findAll());
    }

    @Operation(summary = "Retorna um alocamento em especifico",description = "Esse endpoint retorna um alocamento identificado pelo ID.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum alocamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Alocamento encontrado.", useReturnTypeSchema = true),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> alocamentoPorId(@PathVariable("id") Integer idAlocamento){

        var alocamento = alocamentoRepository.findById(idAlocamento);

        if (alocamento.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alocamento não encontrado");
        }
        return ResponseEntity.ok(alocamento);
    }

    @Operation(summary = "Cria um alocamento",description = "Esse endpoint cria um alocamento no banco de dados e retorna a URI do mesmo, apenas os valores: idFuncionario, idProjeto e dataAlocacao são necessarios.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum alocamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Alocamento encontrado."),
    })
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

    @Operation(summary = "Apaga um alocamento em especifico",description = "Esse endpoint apaga um alocamento identificado pelo ID no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum alocamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Alocamento encontrado."),
    })
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

    @Operation(summary = "Edita um alocamento em especifico",description = "Esse endpoint edita um alocamento identificado pelo ID no banco de dados, apenas os valores: idFuncionario, idProjeto e dataAlocacao são necessarios.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum alocamento encontrado."),
            @ApiResponse(responseCode = "200",description = "Alocamento encontrado."),
    })
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