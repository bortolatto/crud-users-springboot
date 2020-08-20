package com.softplan.codechallenge.domain.service;

import com.softplan.codechallenge.domain.Pessoa;
import com.softplan.codechallenge.domain.exception.CpfJaExistenteException;
import com.softplan.codechallenge.domain.exception.PessoaNaoEncontradaException;
import com.softplan.codechallenge.domain.repository.Pessoas;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private Pessoas pessoas;

    public Pessoa cadastrar(Pessoa pessoa) {
        Optional<Pessoa> optionalPessoa = pessoas.findByCpf(pessoa.getCpf());
        optionalPessoa.ifPresent(p -> {
            if (!p.equals(pessoa)) {
                throw new CpfJaExistenteException(String.format("Já existe cadastro com o cpf %s", pessoa.getCpf()));
            }
        });
        return pessoas.save(pessoa);
    }

    public Pessoa atualizar(Long pessoaId, Pessoa pessoa) {
        Pessoa pessoaAtual = buscarPorId(pessoaId);
        BeanUtils.copyProperties(pessoa, pessoaAtual, "id", "dataCriacao", "dataUltimaAtualizacao", "cpf");

        return pessoas.save(pessoaAtual);
    }

    public void excluir(Long pessoaId) {
        pessoas.delete(buscarPorId(pessoaId));
    }

    public Pessoa buscarPorId(Long id) {
        return pessoas.findById(id)
                .orElseThrow(() ->
                        new PessoaNaoEncontradaException(String.format("Pessoa de id %d não encontrada!", id)));
    }

}
