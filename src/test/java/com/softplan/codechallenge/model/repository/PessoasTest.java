package com.softplan.codechallenge.model.repository;

import com.softplan.codechallenge.model.Pessoa;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PessoasTest {

    @Autowired
    private Pessoas pessoas;

    @PersistenceContext
    private EntityManager entityManager;

    private Pessoa pessoa;

    @Before
    public void setUp() {
        pessoa = new Pessoa();
        pessoa.setNome("aPerson");
        pessoa.setDataNascimento(LocalDateTime.now());
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

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowErrorWhenEmailIsInvalid() {
        pessoa.setEmail("person.com");
        pessoas.save(pessoa);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowErrorWhenCpfIsInvalid() {
        pessoa.setCpf("9999999999");
        pessoas.save(pessoa);
    }
}