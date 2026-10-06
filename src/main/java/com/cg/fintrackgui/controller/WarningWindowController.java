package com.cg.fintrackgui.controller;

import com.cg.fintrackgui.util.AnimationsUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

// Controlador da janela modal de confirmação e avisos
public class WarningWindowController {

    // Ação genérica executada ao confirmar o diálogo
    private Runnable onConfirmAction;

    // Componentes da interface injetados pelo FXML
    @FXML Label lbWarningText;
    @FXML VBox warningRootPane;
    @FXML private StackPane warningOverlay;
    @FXML Button btnCancel;

    // Define a mensagem exibidada na caixa de aviso
    public void setWarningMessage(String message){
        lbWarningText.setText(message);
    }

    // Associa uma ação genérica ao botão de confirmação
    public void setOnConfirmAction(Runnable onConfirmAction) {
        this.onConfirmAction = onConfirmAction;    
    }

    // Executa a ação associada e fecha o aviso
    @FXML
    private void handleConfirm() {
        if (onConfirmAction != null) {
            onConfirmAction.run();
        }
        
        handleCloseWarning();
    }

    // Executa o fechamento animado da janela de aviso
    @FXML
    private void handleCloseWarning() {
        AnimationsUtils.closeOverlay(warningRootPane.getParent(), warningRootPane);
    }       
}