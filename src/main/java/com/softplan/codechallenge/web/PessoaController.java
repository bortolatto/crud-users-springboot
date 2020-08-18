package com.softplan.codechallenge.web;

import com.softplan.codechallenge.domain.Pessoa;
import com.softplan.codechallenge.domain.repository.Pessoas;
import com.softplan.codechallenge.domain.service.PessoaService;
import com.softplan.codechallenge.model.PessoaDTOInput;
import com.softplan.codechallenge.model.PessoaDTOOutput;
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

@RequestMapping("/pessoas")
@RestController
public class PessoaController {

    @Autowired
    private Pessoas pessoas;

    @Autowired
    private PessoaService service;

    @Autowired
    private ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public List<PessoaDTOOutput> getAll() {
        return toDto(pessoas.findAll());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PessoaDTOOutput register(@Valid @RequestBody PessoaDTOInput pessoa) {
        return toDto(service.cadastrar(toPessoa(pessoa)));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTOOutput> update(@Valid @RequestBody PessoaDTOInput pessoa,
                                                  @PathVariable Long id) {

        Pessoa pessoaAtualizada = service.atualizar(id, toPessoa(pessoa));

        // somente para que a data de atualização
        // não fique null após o update
        entityManager.flush();
        return ResponseEntity.ok(toDto(pessoaAtualizada));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
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
