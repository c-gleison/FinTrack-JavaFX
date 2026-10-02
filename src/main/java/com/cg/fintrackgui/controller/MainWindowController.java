package com.cg.fintrackgui.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ResourceBundle;

import com.cg.fintrackgui.model.Transaction;
import com.cg.fintrackgui.service.TransactionService;
import com.cg.fintrackgui.util.AnimationsUtils;
import com.cg.fintrackgui.util.ValidationUtils;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

// Controlador principal da interface do aplicativo
public class MainWindowController implements Initializable {
    
    // Componentes da interface injetados pelo FXML
    @FXML private BorderPane mainBorderPane;
    @FXML private StackPane overlayPane;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colId;
    @FXML private TableColumn<Transaction, String> colName;
    @FXML private TableColumn<Transaction, BigDecimal> colValue;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colMonth;
    @FXML private TableColumn<Transaction, String> colDescription;
    @FXML private Button btnRm;
    @FXML private Button btnUpd;
    @FXML private Label txtBalance;

    // Lista observável para armazenamento e renderização na tabela
    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    // Serviço que coordena o banco de dados e o cache em memória
    private final TransactionService service = new TransactionService();

    // Entrega o serviço via getter
    public TransactionService getService() {
        return service;
    }
    
    // Inicializa a tela e carrega os dados essenciais
    @Override 
    public void initialize(URL url, ResourceBundle rb) {
        loadTransactions();
        setupListeners();
        setupFormatters();
        updateTotalBalance();
    }

    // Configura os eventos de seleção da tabela
    private void setupListeners(){
        transactionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateMWButtons());
    }

    // Define as regras de exibição da tabela e atualização dos botões
    private void setupFormatters(){
        setupTableColumns();
        updateMWButtons();
    }

    // Limpa a seleção da tabela e abre o formulário para nova transação
    @FXML
    private void addTransaction(ActionEvent event) {
        transactionTable.getSelectionModel().clearSelection();
        transactionWindow();
    }

    // Solicita confirmação do usuário e remove a transação selecionada
    @FXML
    private void removeTransaction(){
        Transaction t = getSelectedTransaction();
        if (t != null) {
            String transactionId = t.getId();   
            ValidationUtils.showWarningOverlay(overlayPane, mainBorderPane, "Remover a transação?", () -> {
            try {
                service.remove(transactionId);
                refreshTable();
                updateTotalBalance();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });            
        }
    }

    // Abre a janela de edição para a transação selecionada
    @FXML 
    private void updateTransaction(){
        Transaction t = getSelectedTransaction();

        try {
            if (t != null) {
                transactionWindow(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mapeia os dados do modelo para cada coluna da tabela e aplica as formatações visuais
    private void setupTableColumns() {
        colId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        colName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        
        colValue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getValue()));
        colValue.setCellFactory(column -> new TableCell<Transaction, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null) {
                    setText(null);    
                } else {
                    setText(ValidationUtils.currencyMask(value));
                }
            }   
        });
        
        colType.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getType()));
        colDate.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate().toString().trim()));
        
        colMonth.setCellValueFactory(cellData -> {
            Month month = cellData.getValue().getRecurrencyMonth();

            if (month != null) {
                String monthFormatted = month.getDisplayName(TextStyle.SHORT, ValidationUtils.getBrLocation()).toUpperCase();
                return new ReadOnlyObjectWrapper<>(monthFormatted);
            }
            return new ReadOnlyObjectWrapper<>("");
        });
        
        colDescription.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescription()));

        transactionTable.setItems(transactionList);
    }

    // Copia o cache do serviço para a lista da tabela, sem consultar o banco
    public void refreshTable() {
        transactionList.clear();      
        service.copyTo(transactionList);
    }

    // Lê as transações do banco para o cache e atualiza a tabela
    public void loadTransactions() {
        try {
            service.reload();
            refreshTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Obtém o item atualmente selecionado na tabela
    public Transaction getSelectedTransaction(){
        return transactionTable.getSelectionModel().getSelectedItem();
    }

    // Abre o formulário em formato modal com desfoque de fundo e animação
    public void transactionWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewTransactionWindow.fxml"));
            Parent root = loader.load(); 

            NewTransactionWindowController childController = loader.getController();
            childController.setMainWindowController(this);
            childController.setFormData(getSelectedTransaction());
            
            AnimationsUtils.applyBackdropEffect(root, mainBorderPane, new GaussianBlur(15));
             
            overlayPane.setCache(true);
            overlayPane.setCacheHint(CacheHint.SPEED);

            overlayPane.getChildren().setAll(root);
            overlayPane.setVisible(true);
            overlayPane.setOpacity(0.0);

            Platform.runLater(() -> AnimationsUtils.popupOpenAnimation(overlayPane, root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Habilita ou desabilita os botões de ação conforme houver seleção na tabela
    private void updateMWButtons() {
        boolean hasSelection = getSelectedTransaction() != null;

        ValidationUtils.updateButtonState(btnRm, hasSelection, "btn-red");
        ValidationUtils.updateButtonState(btnUpd, hasSelection, "btn-green");
    }

    public void updateTotalBalance(){
        BigDecimal balance = ValidationUtils.calculateTotalBalance(transactionList);
        txtBalance.setText(ValidationUtils.currencyMask(balance));
    }

}