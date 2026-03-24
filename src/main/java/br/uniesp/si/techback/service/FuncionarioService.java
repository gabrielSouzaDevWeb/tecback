package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.exception.ResourceNotFoundException;
import br.uniesp.si.techback.mapper.FuncionarioMapper;
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
    private final FuncionarioMapper mapper;

    public List<FuncionarioDTO> listar() {
        log.info("Buscando todos os funcionarios cadastrados");
        List<FuncionarioDTO> funcionarios = repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
        log.debug("Total de funcionarios encontrados: {}", funcionarios.size());
        return funcionarios;
    }

    public FuncionarioDTO buscarPorId(Long id) {
        log.info("Buscando funcionario pelo ID: {}", id);
        Funcionario funcionario = buscarFuncionarioEntidadePorId(id);
        log.debug("Funcionario encontrado: ID={}, Nome={}", funcionario.getId(), funcionario.getNome());
        return mapper.toDTO(funcionario);
    }

    @Transactional
    public FuncionarioDTO atualizar(Long id, FuncionarioDTO funcionarioDTO) {
        log.info("Atualizando funcionario ID: {}", id);
        Funcionario funcionarioExistente = buscarFuncionarioEntidadePorId(id);
        mapper.updateEntity(funcionarioExistente, funcionarioDTO);
        Funcionario funcionarioAtualizado = repository.save(funcionarioExistente);
        log.info("Funcionario ID: {} atualizado com sucesso", id);
        return mapper.toDTO(funcionarioAtualizado);
    }

    @Transactional
    public FuncionarioDTO salvar(FuncionarioDTO funcionarioDTO) {
        log.info("Salvando novo funcionario: {}", funcionarioDTO.getNome());
        Funcionario funcionarioSalvo = repository.save(mapper.toEntity(funcionarioDTO));
        log.info("Funcionario salvo com sucesso. ID: {}, Nome: {}", funcionarioSalvo.getId(), funcionarioSalvo.getNome());
        return mapper.toDTO(funcionarioSalvo);
    }

    @Transactional
    public void excluir(Long id) {
        log.info("Excluindo funcionario ID: {}", id);
        Funcionario funcionario = buscarFuncionarioEntidadePorId(id);
        repository.delete(funcionario);
        log.info("Funcionario ID: {} excluido com sucesso", id);
    }

    private Funcionario buscarFuncionarioEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario nao encontrado com o ID: " + id));
    }


}
