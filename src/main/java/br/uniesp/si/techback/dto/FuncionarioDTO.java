package br.uniesp.si.techback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {

    private Long id;

    @NotBlank(message = "O nome e obrigatorio")
    @Size(max = 255, message = "O nome deve ter no maximo 255 caracteres")
    private String nome;

    @NotBlank(message = "O cargo e obrigatorio")
    @Size(max = 255, message = "O cargo deve ter no maximo 255 caracteres")
    private String cargo;
}

