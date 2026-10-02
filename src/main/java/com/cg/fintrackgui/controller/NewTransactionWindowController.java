package com.cg.fintrackgui.controller;

import java.util.ResourceBundle;

import com.cg.fintrackgui.dao.TransactionDAO;
import com.cg.fintrackgui.model.Transaction;
import com.cg.fintrackgui.service.TransactionService;
import com.cg.fintrackgui.util.AnimationsUtils;
import com.cg.fintrackgui.util.ValidationUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;

// Controlador da janela modal de criação e edição de transações
public class NewTransactionWindowController implements Initializable {

    // Componentes da interface injetados pelo FXML
    @FXML private ComboBox<Month> cmbMonth;
    @FXML private VBox vBoxCB;
    @FXML private HBox hBoxID;
    @FXML private RadioButton rbRecurrencyYes;
    @FXML private RadioButton rbRecurrencyNo;
    @FXML private RadioButton rbRevenue;
    @FXML private RadioButton rbExpense;
    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtValue;
    @FXML private Label txtID;
    @FXML private StackPane formWindow;
    @FXML private StackPane WarningOverlayPane;
    @FXML private VBox formMainVbox;
    @FXML private Label lbTitle;
    @FXML private Label lblErrorMessage;
    @FXML private Button btnSave;


    
    // Referência ao controlador da janela principal
    private MainWindowController mainWindowController;
    private TransactionService service;

    // Guarda a referência da janela principal e obtém dela o serviço compartilhado
    public void setMainWindowController(MainWindowController mainWindowController){
        this.mainWindowController = mainWindowController;
        this.service = mainWindowController.getService();
    }

    // Estado inicial da transação e modo de operação
    Transaction currentTransaction = new Transaction(null, null, null, null, null, null, null);
    boolean isNewTransaction = true;

    // Inicializa os seletores, máscaras de entrada e ouvintes do formulário
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeMonths();
        setupFormatters();
        setupListeners();
  
        txtValue.setTextFormatter(ValidationUtils.currencyFormatter());
    }

    // Configura os ouvintes para revalidar o formulário a cada alteração dos campos
    private void setupListeners(){
        txtName.textProperty().addListener((obs, oldVal, newVal) -> isFormValid());
        txtValue.textProperty().addListener((obs, oldVal, newVal) -> isFormValid());
        cmbMonth.valueProperty().addListener((obs, oldVal, newVal) -> isFormValid());

        rbRecurrencyYes.selectedProperty().addListener((obs, oldVal, isNowSelected) -> {
            if (!isNowSelected) {
                cmbMonth.getSelectionModel().clearSelection();
            }
            isFormValid();
        });
    }

    // Define a validação inicial e formatação em caixa alta para o nome
    private void setupFormatters(){
        isFormValid();
        txtName.setTextFormatter(ValidationUtils.upperCaseFormatter());
    }
    
    // Preenche os campos do formulário com dados de uma transação existente
    @FXML 
    public void setFormData(Transaction transaction){
        if (transaction != null) {
            lbTitle.setText("Editar transação");

            this.isNewTransaction = false;
            this.currentTransaction = transaction;

            txtName.setText(currentTransaction.getName());
            txtValue.setText(ValidationUtils.currencyMask(currentTransaction.getValue()));   
            txtDescription.setText(currentTransaction.getDescription()); 
            
            boolean isRevenue = "RECEITA".equalsIgnoreCase(transaction.getType());
            rbRevenue.setSelected(isRevenue);
            rbExpense.setSelected(!isRevenue);

            boolean isRecurrent = currentTransaction.getRecurrencyMonth() != null;
            rbRecurrencyYes.setSelected(isRecurrent);
            rbRecurrencyNo.setSelected(!isRecurrent);

            if (currentTransaction.getRecurrencyMonth() != null) {
                cmbMonth.setValue(currentTransaction.getRecurrencyMonth());  
            } else {
                cmbMonth.getSelectionModel().clearSelection();
            }
    
            hBoxID.visibleProperty().set(true);
            hBoxID.setManaged(true);
            hBoxID.setMaxHeight(10);
            txtID.setText(transaction.getId());
        } 
    }
    
    // Processa o salvamento ou alteração da transação
    @FXML
    private void handleSave(){
        try {  
            if (!isFormValid()) {
                return;
            }

            // Coleta e converte os valores dos campos do formulário
            String description = txtDescription.getText();
            String name = txtName.getText().trim();
            String digits = txtValue.getText().replaceAll("\\D", "");
            BigDecimal value = digits.isEmpty() ? BigDecimal.ZERO : new BigDecimal(digits).divide(new BigDecimal("100"));
            String type = rbRevenue.isSelected() ? "RECEITA" : "DESPESA";
            String id = isNewTransaction ? ValidationUtils.generateShortId() : currentTransaction.getId();
            LocalDate date = isNewTransaction ? LocalDate.now() : currentTransaction.getDate();
            Month recurrencyMonth = rbRecurrencyYes.isSelected() ? cmbMonth.getValue() : null;
            Transaction transaction = new Transaction(id, name, value, type, date, recurrencyMonth, description);

            // Persiste via serviço conforme modo de criação ou edição
            if (isNewTransaction == true) {
                service.add(transaction);
                finishSave();
            } else {
                ValidationUtils.showWarningOverlay(WarningOverlayPane, formMainVbox,"Salvar edição?", () -> {
                    try {
                        service.update(transaction);
                        finishSave();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Popula o seletor de meses em português e vincula sua exibição à opção de recorrência
    public void initializeMonths(){
        cmbMonth.getItems().setAll(Month.values());

        cmbMonth.setCellFactory(param -> new ListCell<>() {
            @Override 
            protected void updateItem(Month item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);       
                } else {
                    String namePt = item.getDisplayName(TextStyle.FULL, ValidationUtils.getBrLocation());
                    setText(namePt.substring(0,1).toUpperCase() + namePt.substring(1));
                }
            }
        });

        cmbMonth.setButtonCell(cmbMonth.getCellFactory().call(null));
        cmbMonth.getSelectionModel().clearSelection();

        vBoxCB.visibleProperty().bind(rbRecurrencyYes.selectedProperty());
        vBoxCB.managedProperty().bind(vBoxCB.visibleProperty());
    }

    // Executa o fechamento animado do painel sobreposto
    @FXML
    private void closeOverlay() {
        AnimationsUtils.closeOverlay(formWindow.getParent(), formWindow);
    }

    // Valida os campos obrigatórios e atualiza a visibilidade das mensagens e botões
    private boolean isFormValid() {
        boolean isNameValid = txtName.getText() != null && !txtName.getText().trim().isEmpty();

        String digits = txtValue.getText() != null ? txtValue.getText().replaceAll("\\D", "") : "";
        boolean isValueValid = !digits.isEmpty() && new java.math.BigDecimal(digits).compareTo(java.math.BigDecimal.ZERO) > 0;

        boolean isMonthValid = !rbRecurrencyYes.isSelected() || cmbMonth.getValue() != null;

        boolean isValid = isNameValid && isValueValid && isMonthValid;

        ValidationUtils.updateButtonState(btnSave, isValid, "btn-green");

        if (!isValid) {
            lblErrorMessage.setText("Preencha todos os campos obrigatórios.");
            lblErrorMessage.setVisible(true);
        } else {
            lblErrorMessage.setText("");
            lblErrorMessage.setVisible(false);
        }

        return isValid;
    }

    // Finaliza o fluxo salvando, atualizando a tela principal e fechando a janela
    private void finishSave() {
        mainWindowController.refreshTable();
        mainWindowController.updateTotalBalance();
        AnimationsUtils.closeOverlay(formWindow.getParent(), formWindow);
    }
}