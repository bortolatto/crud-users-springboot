package com.softplan.codechallenge.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
public class PessoaDTOOutput {

    private Long id;

    private String nome;

    private String sexo;

    private String email;

    private String dataNascimento;

    private String cpf;

    private String naturalidade;

    private String nacionalidade;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dataCriacao;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dataUltimaAtualizacao;
}
