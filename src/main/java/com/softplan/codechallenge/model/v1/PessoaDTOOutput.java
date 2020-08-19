package com.softplan.codechallenge.model.v1;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
public class PessoaDTOOutput {

    @ApiModelProperty(example = "5")
    private Long id;

    @ApiModelProperty(example = "João da Silva")
    private String nome;

    @ApiModelProperty(example = "masculino")
    private String sexo;

    @ApiModelProperty(example = "name@domain.com")
    private String email;

    @ApiModelProperty(example = "01/01/2001")
    private String dataNascimento;

    @ApiModelProperty(example = "427.223.407-20")
    private String cpf;

    @ApiModelProperty(example = "Florianópolis")
    private String naturalidade;

    @ApiModelProperty(example = "Brasileira")
    private String nacionalidade;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dataCriacao;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dataUltimaAtualizacao;
}
