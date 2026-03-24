package br.uniesp.si.techback.repository;

import br.uniesp.si.techback.model.Funcionario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Test
    void deveSalvarEBuscarFuncionarioPorId() {
        Funcionario funcionario = new Funcionario(null, "Maria", "Analista");

        Funcionario salvo = funcionarioRepository.save(funcionario);
        Optional<Funcionario> encontrado = funcionarioRepository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Maria", encontrado.get().getNome());
        assertEquals("Analista", encontrado.get().getCargo());
    }
}

