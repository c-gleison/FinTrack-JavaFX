package com.cg.fintrackgui.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.cg.fintrackgui.dao.TransactionDAO;
import com.cg.fintrackgui.model.Transaction;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


public class MainWindowController implements  Initializable{
    
    // Variaveis
    @FXML private BorderPane mainBorderPane;
    @FXML private StackPane overlayPane;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Integer> colId;
    @FXML private TableColumn<Transaction, String> colName;
    @FXML private TableColumn<Transaction, BigDecimal> colValue;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, String> colDescription;

    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    // Instancias
    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override 
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadTransactions();
    }
     @FXML
    private void addTransaction(ActionEvent event) {
        
        try {
            // Carrega o arquivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewTransactionWindow.fxml"));
            Parent root = loader.load(); 

            // Estabelece a comunicação entre os controladores (Injeção de Dependência)
            NewTransactionWindowController childController = loader.getController();
            childController.setMainWindowController(this);
            
            // Aplica um efeito de desfoque na janela principal
            mainBorderPane.setEffect(new GaussianBlur(15));
            mainBorderPane.setCache(true);
            mainBorderPane.setCacheHint(CacheHint.SPEED);
             
            // Ativa o cache de renderização
            overlayPane.setCache(true);
            overlayPane.setCacheHint(CacheHint.SPEED);

            // Insere a tela de overlay no StackPane e ativa sua exibição
            overlayPane.getChildren().setAll(root);
            overlayPane.setVisible(true);
            // Define a opacidade inicial do overlay antes da animação
            overlayPane.setOpacity(0.0);

            // Roda a animação de abertura
            Platform.runLater(() -> AnimationsUtils.popupOpenAnimation(overlayPane, root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeTransaction(){

        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

        try{
            if (selectedTransaction != null) {

                int transactionId = selectedTransaction.getId();   
                transactionDAO.remove(transactionId);   
            }

            loadTransactions();

        } catch ( NullPointerException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Vincula cada coluna ao getter do Model
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
                    setText(ValidationUtils.formatCurrency(value));
                }
            }   
        });

        colType.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getType()));
        
        colDescription.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDescription()));

        // Associa a lista observável à TableView
        transactionTable.setItems(transactionList);
    }

    // Lê todas as linhas do banco via DAO e popula a lista da tabela
    public void loadTransactions() {
        try {
            transactionList.setAll(transactionDAO.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

