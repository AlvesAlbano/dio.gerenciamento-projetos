package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Projeto;
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

@Tag(name = "Projeto", description = "Endpoits relacionados ao gerênciamento de projetos.")
@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Operation(summary = "Retorna todos os projetos",description = "Esse endpoint retorna todos os projetos presentes no banco de dados.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum projeto encontrado."),
            @ApiResponse(responseCode = "200",description = "Projeto encontrado."),
    })
    @GetMapping("todos")
    public ResponseEntity<?> todosProjetos(){

        if (projetoRepository.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum projeto encontrado");
        }
        return ResponseEntity.ok(projetoRepository.findAll());
    }

    @Operation(summary = "Retorna um projeto em especifico",description = "Esse endpoint retorna todos os projetos presentes no banco de dados.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum projeto encontrado."),
            @ApiResponse(responseCode = "200",description = "Projeto encontrado."),
    })
    @GetMapping("{id}")
    public ResponseEntity<?> projetoPorId(@PathVariable("id") Integer idProjeto){

        var projeto = projetoRepository.findById(idProjeto);

        if (projeto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Projeto não encotrado");
        }
        return ResponseEntity.ok(projeto);
    }

    @Operation(summary = "Cria um projeto",description = "Esse endpoint cria um projeto no banco de dados e retorna a URI do mesmo, os valores nomeProjeto, descricaoProjeto, dataInicio são obrigatorios e o valor da dataFim e opcional. As datas devem estar no formato dia-mês-ano.")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum projeto encontrado."),
            @ApiResponse(responseCode = "200",description = "Projeto encontrado."),
    })
    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarProjeto(@RequestBody Projeto projeto){

        var novoProjeto = projetoRepository.saveAndFlush(projeto);

        URI uriprojeto = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/projeto/{id}")
                .buildAndExpand(novoProjeto.getIdProjeto())
                .toUri();

        return ResponseEntity.ok("Novo projeto adicionado: " + uriprojeto);
    }

    @Operation(summary = "Apaga um projeto em especifico",description = "Esse endpoint apaga um projeto identificado pelo ID no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum projeto encontrado."),
            @ApiResponse(responseCode = "200",description = "Projeto encontrado."),
    })
    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<?> apagarProjeto(@PathVariable("id") Integer idProjeto){

        var projeto = projetoRepository.findById(idProjeto);

        if (projeto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Projeto não encontrado");
        }

        projetoRepository.deleteById(idProjeto);
        return ResponseEntity.ok("Projeto " + projeto.get().getNomeProjeto() + " apagado com sucesso.");
    }

    @Operation(summary = "Edita um projeto em especifico",description = "Esse endpoint edita um projeto identificado pelo ID no banco de dados")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "404",description = "Nenhum projeto encontrado."),
            @ApiResponse(responseCode = "200",description = "Projeto encontrado."),
    })
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarProjeto(@PathVariable("id") Integer idProjeto, @RequestBody Projeto projeto){
        var projetoIdentificado = projetoRepository.findById(idProjeto);

        if (projetoIdentificado.isPresent()){
            var projetoBuilder = projetoIdentificado.get();

            projetoBuilder.setNomeProjeto(projeto.getNomeProjeto());
            projetoBuilder.setDescricaoProjeto(projeto.getDescricaoProjeto());
            projetoBuilder.setDataInicio(projeto.getDataInicio());
            projetoBuilder.setDataFim(projeto.getDataFim());

            projetoRepository.saveAndFlush(projetoBuilder);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Projeto - " + projeto.getNomeProjeto() + " atualizado.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Projeto não encontrado");
    }
}