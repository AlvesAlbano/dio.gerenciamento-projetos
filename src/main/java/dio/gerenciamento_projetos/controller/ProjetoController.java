package dio.gerenciamento_projetos.controller;

import dio.gerenciamento_projetos.model.Projeto;
import dio.gerenciamento_projetos.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;

    @GetMapping("todos")
    public ResponseEntity<?> todosProjetos(){

        if (projetoRepository.findAll().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum projeto encontrado");
        }
        return ResponseEntity.ok(projetoRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> projetoPorId(@PathVariable("id") Integer idProjeto){

        var projeto = projetoRepository.findById(idProjeto);

        if (projeto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Projeto não encotrado");
        }
        return ResponseEntity.ok(projeto);
    }

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