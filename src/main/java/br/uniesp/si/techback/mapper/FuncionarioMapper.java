package br.uniesp.si.techback.mapper;

import br.uniesp.si.techback.dto.FuncionarioDTO;
import br.uniesp.si.techback.model.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

	public FuncionarioDTO toDTO(Funcionario funcionario) {
		if (funcionario == null) {
			return null;
		}
		return new FuncionarioDTO(
				funcionario.getId(),
				funcionario.getNome(),
				funcionario.getCargo()
		);
	}

	public Funcionario toEntity(FuncionarioDTO funcionarioDTO) {
		if (funcionarioDTO == null) {
			return null;
		}
		return new Funcionario(
				funcionarioDTO.getId(),
				funcionarioDTO.getNome(),
				funcionarioDTO.getCargo()
		);
	}

	public void updateEntity(Funcionario funcionario, FuncionarioDTO funcionarioDTO) {
		funcionario.setNome(funcionarioDTO.getNome());
		funcionario.setCargo(funcionarioDTO.getCargo());
	}
}

