package br.uniesp.si.techback.controller;


import br.uniesp.si.techback.model.Funcionario;
import br.uniesp.si.techback.model.Funcionario;
import br.uniesp.si.techback.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;



import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
    private final FuncionarioService service;


    @GetMapping
    public List<Funcionario> listar() {
        //log.info("Listando todos os filmes");
        List<Funcionario> filmes = service.listar();
        //log.debug("Total de filmes encontrados: {}", filmes.size());
        return filmes;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        try {
            Funcionario filme = service.buscarPorId(id);
            //log.debug("Funcionario encontrado: {}", filme);
            return ResponseEntity.ok(filme);
        } catch (Exception e) {
            // log.error("Erro ao buscar filme com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Funcionario> criar(@Valid @RequestBody Funcionario filme) {
        // log.info("Recebida requisição para criar novo filme: {}", filme.getNome());
        try {
            Funcionario filmeSalvo = service.salvar(filme);
            // log.info("Funcionario criado com sucesso. ID: {}, Título: {}", filmeSalvo.getId(), filmeSalvo.getNome());

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(filmeSalvo.getId())
                    .toUri();
            // log.debug("URI de localização do novo filme: {}", location);

            return ResponseEntity.created(location).body(filmeSalvo);
        } catch (Exception e) {
            //   log.error("Erro ao criar filme: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id, @Valid @RequestBody Funcionario filme) {
        //  log.info("Atualizando filme com ID {}: {}", id, filme);
        try {
            Funcionario filmeAtualizado = service.atualizar(id, filme);
            //  log.debug("Funcionario ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(filmeAtualizado);
        } catch (Exception e) {
            //  log.error("Erro ao atualizar filme ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        //   log.info("Excluindo filme com ID: {}", id);
        try {
            service.excluir(id);
            //  log.debug("Funcionario com ID {} excluído com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // log.error("Erro ao excluir filme com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}
