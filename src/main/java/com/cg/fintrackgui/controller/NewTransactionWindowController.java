package com.cg.fintrackgui.controller;

import java.util.ResourceBundle;

import com.cg.fintrackgui.dao.TransactionDAO;
import com.cg.fintrackgui.model.Transaction;
import com.cg.fintrackgui.util.AnimationsUtils;
import com.cg.fintrackgui.util.ValidationUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewTransactionWindowController implements Initializable {

    // Variaveis
    @FXML private ComboBox<String> choiceMonths;
    @FXML private VBox vBoxCB;
    @FXML private RadioButton rbYes;
    @FXML private RadioButton rb1No;
    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtValue;
    @FXML private VBox rootBox;
    @FXML private Label lbTitle;

    // Instância
    private final TransactionDAO transactionDAO = new TransactionDAO();
    
    // Referência de controller
    private MainWindowController mainWindowController;
    public void setMainWindowController(MainWindowController mainWindowController){
        this.mainWindowController = mainWindowController;
    }

    //Variaveis globais
    Transaction currentTransaction = new Transaction(0, null, null, null, null, null);
    boolean isNewTransaction = true;


    // Chamar metodos na inicialização
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initializeMonths();
  
        // Aplica o filtro de formatação
        txtValue.setTextFormatter(ValidationUtils.currencyFormatter());
    }

    @FXML 
    public void setFormData(Transaction transaction){

        if (transaction != null) {

            // Muda o titulo da janela in-runtime
            lbTitle.setText("Editar transação");

            this.isNewTransaction = false;
            this.currentTransaction = transaction;

            txtName.setText(currentTransaction.getName());
            txtValue.setText(currentTransaction.getValue().toString().trim());   
            txtDescription.setText(currentTransaction.getDescription()); 
            
            boolean isRevenue = "RECEITA".equalsIgnoreCase(transaction.getType());

            rbYes.setSelected(isRevenue);
            rb1No.setSelected(!isRevenue);

        } 
    }
    
    // Ação acionada pelo botão Salvar
    @FXML
    private void handleSave(){
        try {  

            Transaction transaction = new Transaction(0, null, null, null, null, null);

            // Obtém e converte os dados dos componentes
            String description = txtDescription.getText();
            String name = txtName.getText();
            String digits = txtValue.getText().replaceAll("\\D", "");
            BigDecimal value = digits.isEmpty() ? BigDecimal.ZERO : new BigDecimal(digits).divide(new BigDecimal("100"));
            String type = rbYes.isSelected() ? "RECEITA" : "DESPESA";
            int id = isNewTransaction ? 0 : currentTransaction.getId();

            transaction = new Transaction(id, name, value, type, null, description);


            if (isNewTransaction == true) {

                // Instancía a transação e grava no banco SQLite 
                transactionDAO.save(transaction);    

            }else{
                
                transactionDAO.update(transaction);
            }

            // Atualiza visualmente a lista de transações
            mainWindowController.loadTransactions();

            // Roda a animação de fechamento
            AnimationsUtils.closeOverlay(rootBox.getParent(), rootBox);
    
        } catch (SQLException e) {
            // Imprime erros de banco de dados no console
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Imprime erros de conversão do valor numérico no console
            e.printStackTrace();
        }
    }

    // Popula a comboBox com os meses do ano
    public void initializeMonths(){

        choiceMonths.getItems().addAll(
            "JAN", "FEV", "MAR", "ABR", "MAI", "JUN",
            "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"
        );

        choiceMonths.getSelectionModel().clearSelection();

        vBoxCB.visibleProperty().bind(rbYes.selectedProperty());
        vBoxCB.managedProperty().bind(vBoxCB.visibleProperty());

    }

    // Sobrecarga, para permitir chamar o metodo direto no FXML
    @FXML
    private void closeOverlay() {
        // Metodo original
        AnimationsUtils.closeOverlay(rootBox.getParent(), rootBox);
    }

}
