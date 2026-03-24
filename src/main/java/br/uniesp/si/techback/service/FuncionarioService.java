package br.uniesp.si.techback.service;

import br.uniesp.si.techback.model.Funcionario;
import br.uniesp.si.techback.repository.FuncionarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public List<Funcionario> listar() {
        log.info("Buscando todos os funcionario cadastrados");
        try {
            List<Funcionario> funcionario = repository.findAll();
            log.debug("Total de funcionario encontrados: {}", funcionario.size());
            return funcionario;
        } catch (Exception e) {
            log.error("Falha ao buscar funcionario: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * @param id o ID do funcionario.
     * @return o funcionario encontrado, ou lança uma exceção {@link RuntimeException} se o funcionario não existir.
     */
    public Funcionario buscarPorId(Long id) {
        log.info("Buscando funcionario pelo ID: {}", id);
        return repository.findById(id)
                .map(funcionario -> {
                    log.debug("funcionario encontrado: ID={}, Título={}", funcionario.getId(), funcionario.getNome());
                    return funcionario;
                })
                .orElseThrow(() -> {
                    String mensagem = String.format("funcionario não encontrado com o ID: %d", id);
                    log.warn(mensagem);
                    return new RuntimeException(mensagem);
                });
    }

    /**
     * Atualiza um funcionario existente.
     *
     * @param id    o ID do funcionario a ser atualizado.
     * @param funcionario o funcionario com as informações atualizadas.
     * @return o funcionario atualizado.
     */
    @Transactional
    public Funcionario atualizar(Long id, Funcionario funcionario) {
        log.info("Atualizando funcionario ID: {}", id);
        return repository.findById(id)
                .map(funcionarioExistente -> {
                    log.debug("Dados atuais do funcionario: {}", funcionarioExistente);
                    log.debug("Novos dados: {}", funcionario);
                    funcionario.setId(id);
                    Funcionario funcionarioAtualizado = repository.save(funcionario);
                    log.info("Funcionario ID: {} atualizado com sucesso. Novo título: {}",
                            id, funcionarioAtualizado.getNome());
                    return funcionarioAtualizado;
                })
                .orElseThrow(() -> {
                    String mensagem = String.format("Falha ao atualizar: funcionario não encontrado com o ID: %d", id);
                    log.warn(mensagem);
                    return new RuntimeException(mensagem);
                });
    }

    /**
     * Salva um novo funcionario.
     *
     * @param funcionario o funcionario a ser salvo.
     * @return o funcionario salvo.
     */
    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        log.info("Salvando novo funcionario: {}", funcionario.getNome());
        try {
            Funcionario funcionarioSalvo = repository.save(funcionario);
            log.info("Funcionario salvo com sucesso. ID: {}, Título: {}", funcionarioSalvo.getId(), funcionarioSalvo.getNome());
            return funcionarioSalvo;
        } catch (Exception e) {
            log.error("Falha ao salvar funcionario '{}': {}", funcionario.getNome(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Exclui um funcionario existente.
     *
     * @param id o ID do funcionario a ser excluído.
     */
    @Transactional
    public void excluir(Long id) {
        log.info("Excluindo funcionario ID: {}", id);
        if (!repository.existsById(id)) {
            String mensagem = String.format("Falha ao excluir: funcionario não encontrado com o ID: %d", id);
            log.warn(mensagem);
            throw new RuntimeException(mensagem);
        }
        try {
            repository.deleteById(id);
            log.info("Funcionario ID: {} excluído com sucesso", id);
        } catch (Exception e) {
            log.error("Erro ao excluir funcionario ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }


}
