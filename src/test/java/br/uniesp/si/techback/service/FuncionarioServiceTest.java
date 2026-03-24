package br.uniesp.si.techback.service;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.exception.ResourceNotFoundException;
import br.uniesp.si.techback.mapper.FuncionarioMapper;
import br.uniesp.si.techback.model.Funcionario;
import br.uniesp.si.techback.repository.FuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository repository;

    @Mock
    private FuncionarioMapper mapper;

    @InjectMocks
    private FuncionarioService service;

    @Test
    void deveListarFuncionariosEmDTO() {
        Funcionario funcionario = new Funcionario(1L, "Joao", "Dev");
        FuncionarioDTO dto = new FuncionarioDTO(1L, "Joao", "Dev");

        when(repository.findAll()).thenReturn(List.of(funcionario));
        when(mapper.toDTO(funcionario)).thenReturn(dto);

        List<FuncionarioDTO> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Joao", resultado.getFirst().getNome());
    }

    @Test
    void deveBuscarPorIdQuandoExistir() {
        Funcionario funcionario = new Funcionario(2L, "Ana", "QA");
        FuncionarioDTO dto = new FuncionarioDTO(2L, "Ana", "QA");

        when(repository.findById(2L)).thenReturn(Optional.of(funcionario));
        when(mapper.toDTO(funcionario)).thenReturn(dto);

        FuncionarioDTO resultado = service.buscarPorId(2L);

        assertEquals(2L, resultado.getId());
        assertEquals("Ana", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorIdInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveAtualizarFuncionarioExistente() {
        Funcionario existente = new Funcionario(10L, "Carlos", "Suporte");
        FuncionarioDTO entrada = new FuncionarioDTO(null, "Carlos Silva", "Coordenador");
        Funcionario salvo = new Funcionario(10L, "Carlos Silva", "Coordenador");
        FuncionarioDTO saida = new FuncionarioDTO(10L, "Carlos Silva", "Coordenador");

        when(repository.findById(10L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(salvo);
        when(mapper.toDTO(salvo)).thenReturn(saida);

        FuncionarioDTO resultado = service.atualizar(10L, entrada);

        verify(mapper).updateEntity(existente, entrada);
        assertEquals("Carlos Silva", resultado.getNome());
    }

    @Test
    void deveSalvarNovoFuncionario() {
        FuncionarioDTO entrada = new FuncionarioDTO(null, "Bia", "Estagio");
        Funcionario entidade = new Funcionario(null, "Bia", "Estagio");
        Funcionario salvo = new Funcionario(20L, "Bia", "Estagio");
        FuncionarioDTO saida = new FuncionarioDTO(20L, "Bia", "Estagio");

        when(mapper.toEntity(entrada)).thenReturn(entidade);
        when(repository.save(entidade)).thenReturn(salvo);
        when(mapper.toDTO(salvo)).thenReturn(saida);

        FuncionarioDTO resultado = service.salvar(entrada);

        assertEquals(20L, resultado.getId());
        assertEquals("Bia", resultado.getNome());
    }

    @Test
    void deveExcluirFuncionarioExistente() {
        Funcionario funcionario = new Funcionario(30L, "Lia", "RH");

        when(repository.findById(30L)).thenReturn(Optional.of(funcionario));

        service.excluir(30L);

        verify(repository).delete(funcionario);
    }

    @Test
    void deveLancarExcecaoAoExcluirInexistente() {
        when(repository.findById(77L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.excluir(77L));
        verify(repository).findById(77L);
    }
}


