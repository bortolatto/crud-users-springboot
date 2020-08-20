package com.softplan.codechallenge.model.v2;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class PessoaDTOUpdate {

    @NotNull
    @ApiModelProperty(example = "João da Silva", required = true)
    private String nome;

    @ApiModelProperty(example = "masculino")
    private String sexo;

    @Email(message = "Precisa ser um email válido")
    @ApiModelProperty(example = "name@domain.com")
    private String email;

    @Pattern(regexp = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$",
            message = "Data precisa seguir o padrão dd/MM/yyyy")
    @NotNull
    @ApiModelProperty(example = "01/01/2001", required = true)
    private String dataNascimento;

    @ApiModelProperty(example = "Florianópolis")
    private String naturalidade;

    @ApiModelProperty(example = "Brasileira")
    private String nacionalidade;

    @NotNull
    @ApiModelProperty(example = "Av. Beira Mar Norte 1112, Centro", required = true)
    private String endereco;
}
