package com.softplan.codechallenge.web.v2;

import com.softplan.codechallenge.constants.Constants;
import com.softplan.codechallenge.domain.Pessoa;
import com.softplan.codechallenge.domain.repository.Pessoas;
import com.softplan.codechallenge.domain.service.PessoaService;
import com.softplan.codechallenge.exceptionhandler.Validation;
import com.softplan.codechallenge.model.v2.PessoaDTOInput;
import com.softplan.codechallenge.model.v2.PessoaDTOOutput;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Api(tags = Constants.PESSOAS_TAG)
@RequestMapping("/v2/pessoas")
@RestController
public class PessoaControllerV2 {

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
    @ApiResponses({@ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 200, message = "Request bem sucedida")})
    public List<PessoaDTOOutput> getAll() {
        List<PessoaDTOOutput> pessoas = toDto(this.pessoas.findAll());
        pessoas.forEach((pessoa) ->
                pessoa.add(linkTo(methodOn(PessoaControllerV2.class).findById(pessoa.getId())).withSelfRel()));

        return pessoas;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation("Cadastrar uma nova pessoa")
    @ApiResponses({@ApiResponse(code = 401, message = "Não autorizado"),
                    @ApiResponse(code = 201, message = "Recurso criado com sucesso")})
    public PessoaDTOOutput register(@Valid @RequestBody
                                        @ApiParam(name = "objeto", value = "Dados de uma nova pessoa")
                                                PessoaDTOInput pessoa) {

        PessoaDTOOutput novaPessoa = toDto(service.cadastrar(toPessoa(pessoa)));
        novaPessoa.add(linkTo(methodOn(PessoaControllerV2.class).findById(novaPessoa.getId())).withSelfRel());

        return novaPessoa;
    }

    @Transactional
    @PutMapping("/{id}")
    @ApiOperation("Atualiza uma pessoa informando o Id")
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

        PessoaDTOOutput dto = toDto(pessoaAtualizada);
        dto.add(linkTo(methodOn(PessoaControllerV2.class).update(pessoa,id)).withSelfRel());

        return ResponseEntity.ok(dto);
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

    @GetMapping("/{id}")
    @ApiOperation("Buscar uma pessoa utilizando o Id")
    @ApiResponses({@ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 200, message = "Request bem sucedida"),
            @ApiResponse(code = 404, message = "Recurso não encontrado")})
    public PessoaDTOOutput findById(@PathVariable @ApiParam(value = "Código da pessoa", example = "12") Long id) {
        Pessoa pessoa = service.buscarPorId(id);
        PessoaDTOOutput pessoaDTOOutput = toDto(pessoa);

        pessoaDTOOutput.add(linkTo(methodOn(PessoaControllerV2.class).findById(id)).withSelfRel());
        pessoaDTOOutput.add(linkTo(methodOn(PessoaControllerV2.class).getAll()).withRel("pessoas"));

        return pessoaDTOOutput;
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
