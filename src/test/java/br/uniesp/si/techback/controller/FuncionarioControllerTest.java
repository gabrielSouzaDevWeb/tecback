package br.uniesp.si.techback.controller;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.exception.ResourceNotFoundException;
import br.uniesp.si.techback.service.FuncionarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FuncionarioController.class)
class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FuncionarioService funcionarioService;

    @Test
    void deveListarFuncionarios() throws Exception {
        when(funcionarioService.listar()).thenReturn(List.of(
                new FuncionarioDTO(1L, "Joao", "Dev"),
                new FuncionarioDTO(2L, "Ana", "QA")
        ));

        mockMvc.perform(get("/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].nome").value("Ana"));
    }

    @Test
    void deveBuscarFuncionarioPorId() throws Exception {
        when(funcionarioService.buscarPorId(10L))
                .thenReturn(new FuncionarioDTO(10L, "Maria", "Gestora"));

        mockMvc.perform(get("/funcionarios/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.cargo").value("Gestora"));
    }

    @Test
    void deveRetornar404QuandoBuscarIdInexistente() throws Exception {
        when(funcionarioService.buscarPorId(999L))
                .thenThrow(new ResourceNotFoundException("Funcionario nao encontrado com o ID: 999"));

        mockMvc.perform(get("/funcionarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deveCriarFuncionario() throws Exception {
        FuncionarioDTO entrada = new FuncionarioDTO(null, "Lia", "RH");
        FuncionarioDTO salvo = new FuncionarioDTO(7L, "Lia", "RH");

        when(funcionarioService.salvar(any(FuncionarioDTO.class))).thenReturn(salvo);

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/funcionarios/7"))
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void deveValidarDadosNaCriacao() throws Exception {
        FuncionarioDTO entrada = new FuncionarioDTO(null, "", "");

        mockMvc.perform(post("/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.nome").exists())
                .andExpect(jsonPath("$.fieldErrors.cargo").exists());
    }

    @Test
    void deveAtualizarFuncionario() throws Exception {
        FuncionarioDTO entrada = new FuncionarioDTO(null, "Paulo", "Tech Lead");
        FuncionarioDTO atualizado = new FuncionarioDTO(5L, "Paulo", "Tech Lead");

        when(funcionarioService.atualizar(eq(5L), any(FuncionarioDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/funcionarios/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void deveExcluirFuncionario() throws Exception {
        doNothing().when(funcionarioService).excluir(3L);

        mockMvc.perform(delete("/funcionarios/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoExcluirInexistente() throws Exception {
        doThrow(new ResourceNotFoundException("Funcionario nao encontrado com o ID: 3"))
                .when(funcionarioService).excluir(3L);

        mockMvc.perform(delete("/funcionarios/3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}


