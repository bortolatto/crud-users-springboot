package com.softplan.codechallenge.web.v1;

import com.softplan.codechallenge.constants.Constants;
import com.softplan.codechallenge.domain.Pessoa;
import com.softplan.codechallenge.domain.repository.Pessoas;
import com.softplan.codechallenge.domain.service.PessoaService;
import com.softplan.codechallenge.exceptionhandler.Validation;
import com.softplan.codechallenge.model.v1.PessoaDTOInput;
import com.softplan.codechallenge.model.v1.PessoaDTOOutput;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = Constants.PESSOAS_TAG)
@RequestMapping("/v1/pessoas")
@RestController
public class PessoaControllerV1 {

    @Autowired
    private Pessoas pessoas;

    @Autowired
    private PessoaService service;

    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    @ApiOperation("Buscar todas as pessoas cadastradas")
    public List<PessoaDTOOutput> getAll() {
        return toDto(pessoas.findAll());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation("Cadastrar uma nova pessoa")
    @ApiResponses({@ApiResponse(code = 401, message = "Não autorizado"),
                    @ApiResponse(code = 201, message = "Recurso criado com sucesso")})
    public PessoaDTOOutput register(@Valid @RequestBody
                                        @ApiParam(name = "objeto", value = "Dados de uma nova pessoa")
                                                PessoaDTOInput pessoa) {
        return toDto(service.cadastrar(toPessoa(pessoa)));
    }

    @Transactional
    @PutMapping("/{id}")
    @ApiOperation("Buscar uma pessoa informando o Id")
    @ApiResponses({@ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 200, message = "Recurso atualizado com sucesso"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada", response = Validation.class)})
    public ResponseEntity<PessoaDTOOutput> update(@Valid @RequestBody
                                                      @ApiParam(name = "objeto", value = "Dados novos da pessoa")
                                                              PessoaDTOInput pessoa,
                                                  @PathVariable @ApiParam(value = "Código da pessoa", example = "10")
                                                          Long id) {

        Pessoa pessoaAtualizada = service.atualizar(id, toPessoa(pessoa));

        // somente para que a data de atualização
        // não fique null após o update
        entityManager.flush();
        return ResponseEntity.ok(toDto(pessoaAtualizada));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Excluir o registro de uma pessoa informando o Id")
    @ApiResponses({@ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 204, message = "Recurso excluído com sucesso"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada", response = Validation.class)})
    public void delete(@PathVariable @ApiParam(value = "Código da pessoa", example = "2") Long id) {
        service.excluir(id);
    }

    private Pessoa toPessoa(PessoaDTOInput pessoaDTO) {
        return modelMapper.map(pessoaDTO, Pessoa.class);
    }

    private PessoaDTOOutput toDto(Pessoa pessoa) {
        return modelMapper.map(pessoa, PessoaDTOOutput.class);
    }

    private List<PessoaDTOOutput> toDto(List<Pessoa> pessoas) {
        return pessoas.stream().map(this::toDto).collect(Collectors.toList());
    }
}
