package com.cg.fintrackgui.dao;

import com.cg.fintrackgui.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDAOTest {

    private TransactionDAO transactionDAO;
    private Connection keepAliveConnection;

    @BeforeEach
    void setUp() throws SQLException {
        DbConnection.setUrl("jdbc:sqlite:file:memdb?mode=memory&cache=shared");

        keepAliveConnection = DbConnection.getConnection();

        try (Statement stmt = keepAliveConnection.createStatement()) {
            stmt.execute("DELETE FROM transactions;");
        }

        transactionDAO = new TransactionDAO();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (keepAliveConnection != null && !keepAliveConnection.isClosed()) {
            keepAliveConnection.close();
        }
        DbConnection.setUrl("jdbc:sqlite:fintrack.db");
    }

    @Test
    @DisplayName("Deve salvar uma transação no banco e buscá-la por ID com sucesso")
    void shouldSaveAndFindTransactionById() throws SQLException {
        Transaction transaction = new Transaction(
            "AB01",
            "Salário",
            new BigDecimal("3500.00"),
            "RECEITA",
            LocalDate.of(2026, 10, 1),
            Month.OCTOBER,
            "Pagamento mensal"
        );

        transactionDAO.save(transaction);
        Transaction fetched = transactionDAO.findByID("AB01");

        assertNotNull(fetched);
        assertEquals("AB01", fetched.getId());
        assertEquals("Salário", fetched.getName());
        assertEquals(0, new BigDecimal("3500.00").compareTo(fetched.getValue()));
        assertEquals("RECEITA", fetched.getType());
        assertEquals(Month.OCTOBER, fetched.getRecurrencyMonth());
    }

    @Test
    @DisplayName("Deve retornar null ao buscar ID inexistente no banco")
    void shouldReturnNullWhenIdNotFound() throws SQLException {
        Transaction fetched = transactionDAO.findByID("ID_INEXISTENTE");
        assertNull(fetched);
    }

    @Test
    @DisplayName("Deve listar todas as transações cadastradas")
    void shouldFindAllTransactions() throws SQLException {
        Transaction t1 = new Transaction("AB01", "Mercado", new BigDecimal("200.00"), "DESPESA", LocalDate.now(), null, "Compras");
        Transaction t2 = new Transaction("AB02", "Pix Recebido", new BigDecimal("50.00"), "RECEITA", LocalDate.now(), null, "Venda");

        transactionDAO.save(t1);
        transactionDAO.save(t2);

        List<Transaction> list = transactionDAO.findAll();

        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Deve atualizar os dados de uma transação existente")
    void shouldUpdateTransaction() throws SQLException {
        Transaction transaction = new Transaction("AB01", "Aluguel", new BigDecimal("1000.00"), "DESPESA", LocalDate.now(), null, "Antigo");
        transactionDAO.save(transaction);

        Transaction updatedTransaction = new Transaction("AB01", "Aluguel Reajustado", new BigDecimal("1200.00"), "DESPESA", LocalDate.now(), null, "Novo valor");
        transactionDAO.update(updatedTransaction);

        Transaction fetched = transactionDAO.findByID("AB01");
        assertNotNull(fetched);
        assertEquals("Aluguel Reajustado", fetched.getName());
        assertEquals(0, new BigDecimal("1200.00").compareTo(fetched.getValue()));
    }

    @Test
    @DisplayName("Deve remover uma transação do banco")
    void shouldRemoveTransaction() throws SQLException {
        Transaction transaction = new Transaction("AB01", "Lanche", new BigDecimal("30.00"), "DESPESA", LocalDate.now(), null, "Padaria");
        transactionDAO.save(transaction);

        transactionDAO.remove("AB01");

        Transaction fetched = transactionDAO.findByID("AB01");
        assertNull(fetched);
        assertEquals(0, transactionDAO.findAll().size());
    }

    @Test
    @DisplayName("Deve lançar SQLException ao tentar salvar transação com ID duplicado")
    void shouldThrowExceptionOnDuplicateId() throws SQLException {
        Transaction t1 = new Transaction("AB01", "Mercado", new BigDecimal("100.00"), "DESPESA", LocalDate.now(), null, "Compras");
        Transaction t2 = new Transaction("AB01", "Farmácia", new BigDecimal("50.00"), "DESPESA", LocalDate.now(), null, "Remédios");

        transactionDAO.save(t1);

        assertThrows(SQLException.class, () -> {
            transactionDAO.save(t2);
        });
    }
}