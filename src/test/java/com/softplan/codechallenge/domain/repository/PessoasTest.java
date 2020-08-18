package com.softplan.codechallenge.domain.repository;

import com.softplan.codechallenge.domain.Pessoa;
import com.softplan.codechallenge.domain.exception.CpfJaExistenteException;
import com.softplan.codechallenge.domain.exception.PessoaNaoEncontradaException;
import com.softplan.codechallenge.domain.service.PessoaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PessoasTest {

    @Autowired
    private Pessoas pessoas;

    @PersistenceContext
    private EntityManager entityManager;

    private Pessoa pessoa;

    @Autowired
    private PessoaService service;

    @Before
    public void setUp() {
        pessoa = new Pessoa();
        pessoa.setNome("aPerson");
        pessoa.setDataNascimento(LocalDate.now());
    }

    @Test
    @Transactional
    public void shouldInsertAPerson() {
        pessoa.setCpf("999.999.999-99");

        Pessoa save = pessoas.save(pessoa);
        save.setEmail("person@acme.com");
        pessoas.save(save);
        entityManager.flush();

        Pessoa pessoaRecuperada = pessoas.getOne(pessoa.getId());

        assertNotNull(pessoaRecuperada.getDataCriacao());
        assertNotNull(pessoaRecuperada.getDataUltimaAtualizacao());
    }

    @Test(expected = PessoaNaoEncontradaException.class)
    public void shoulRaiseAnErrorWhenPersonDoesntExists() {
        service.excluir(999L);
    }

    @Test
    public void shouldUpdate() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("foo");
        pessoa.setDataNascimento(LocalDate.of(1984, 10, 28));

        Pessoa pessoaCadastrada = service.cadastrar(pessoa);
        pessoaCadastrada.setCpf("999.999.999-00");
        Pessoa pessoaAtualizada = service.atualizar(pessoaCadastrada.getId(), pessoaCadastrada);

        entityManager.clear();

        pessoaAtualizada = service.buscarPorId(pessoaAtualizada.getId());

        assertEquals("999.999.999-00", pessoaAtualizada.getCpf());
        assertNotNull(pessoaAtualizada.getDataUltimaAtualizacao());

    }

    @Test(expected = PessoaNaoEncontradaException.class)
    public void shouldDelete() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("foo");
        pessoa.setCpf("0000");

        Pessoa pessoaCadastrada = service.cadastrar(pessoa);

        service.excluir(pessoaCadastrada.getId());

        Pessoa pessoaExcluida = service.buscarPorId(pessoaCadastrada.getId());

        assertNull(pessoaExcluida);
    }

    @Test(expected = CpfJaExistenteException.class)
    public void shouldNotAllowTwoPeopleWithSameCpf() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setCpf("001");
        service.cadastrar(pessoa1);

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setCpf("001");
        service.cadastrar(pessoa2);
    }
}