package com.cg.fintrackgui.repository;

import com.cg.fintrackgui.model.Transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenericRepositoryTest {

    private GenericRepository<Transaction> repository;
    private Transaction sampleTransaction;

    @BeforeEach
    void setUp() {
        repository = new GenericRepository<>();
        sampleTransaction = new Transaction(
            "AB01",
            "Investimento",
            new BigDecimal("500.00"),
            "RECEITA",
            LocalDate.now(),
            null,
            "Aporte mensal"
        );
    }

    @Test
    @DisplayName("Deve adicionar um item no repositório genérico e encontrá-lo na lista")
    void shouldAddItemToRepository() {
        repository.add(sampleTransaction);

        List<Transaction> all = repository.findAll();
        assertEquals(1, all.size());
        assertTrue(all.contains(sampleTransaction));
    }

    @Test
    @DisplayName("Deve remover um elemento do repositório com sucesso")
    void shouldRemoveItemFromRepository() {
        repository.add(sampleTransaction);
        assertEquals(1, repository.findAll().size());

        boolean removed = repository.remove(sampleTransaction);

        assertTrue(removed);
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @DisplayName("Deve comprovar o funcionamento de Generics com outros tipos de objeto")
    void shouldWorkWithOtherDataTypes() {
        GenericRepository<String> stringRepository = new GenericRepository<>();

        stringRepository.add("Teste Generics");
        assertEquals(1, stringRepository.findAll().size());
        assertEquals("Teste Generics", stringRepository.findAll().get(0));
    }
}

    
