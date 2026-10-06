package com.cg.fintrackgui.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionTest {

    @Test 
    @DisplayName("Deve instanciar uma transação corretamente com todos os atributos")
    void shouldCreateTransactionCorrectly(){

        LocalDate now = LocalDate.now();

        Transaction transaction = new Transaction(
            "AB01", 
            "Mercado", 
            new BigDecimal("150.50"), 
            "DESPESA", 
            now, 
            Month.JANUARY, 
            "Compras da semana"
        );

        assertEquals("AB01", transaction.getId());
        assertEquals("Mercado", transaction.getName());
        assertEquals(new BigDecimal("150.50"), transaction.getValue());
        assertEquals("DESPESA", transaction.getType());
        assertEquals(now, transaction.getDate());
        assertEquals(Month.JANUARY, transaction.getRecurrencyMonth());
        assertEquals("Compras da semana", transaction.getDescription());
    }

    @Test
    @DisplayName("Deve permitir mês de recorrência nulo para transações não recorrentes") 
    void shouldAllowNullRecurrencyMonth() {

        Transaction transaction = new Transaction(
            "T002",
            "Freelance",
            new BigDecimal("500.00"),
            "RECEITA",
            LocalDate.now(),
            null,
            "Projeto pontual"
        );

        assertNull(transaction.getRecurrencyMonth());
    }
    
}
