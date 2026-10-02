package com.cg.fintrackgui.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;

import com.cg.fintrackgui.controller.WarningWindowController;

import com.cg.fintrackgui.model.Transaction;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;

import java.security.SecureRandom;


// Classe utilitária para validações, formatações e exibição de diálogos
public class ValidationUtils {

    // Configurações regionais e constantes para geração de ID
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 4;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    // Construtor privado para evitar instanciação
    private ValidationUtils(){ }

    // Formata um valor numérico para a representação monetária brasileira (R$)
    public static String currencyMask(BigDecimal value) {
        if (value == null) {
            return "R$ 0,00";
        }
        return NumberFormat.getCurrencyInstance(LOCALE_BR).format(value);
    }

    // Cria um formatador de texto para máscara monetária da direita para a esquerda
    public static TextFormatter<String> currencyFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String digits = change.getControlNewText().replaceAll("\\D", "");

            if (digits.isEmpty()) {
                change.setRange(0, change.getControlText().length());
                change.setText("");
                return change;
            }

            BigDecimal value = new BigDecimal(digits).divide(new BigDecimal("100"));
            String formatted = currencyMask(value);

            change.setRange(0, change.getControlText().length());
            change.setText(formatted);
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        };
        
        return new TextFormatter<>(filter);
    }

    // Gera um código identificador alfanumérico aleatório de 4 caracteres
    public static String generateShortId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        
        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(ALPHA_NUMERIC.length());
            sb.append(ALPHA_NUMERIC.charAt(randomIndex));
        }   
        return sb.toString();
    }

    // Gerencia a habilitação e o estilo visual CSS de um botão com base em uma condição
    public static void updateButtonState(Button button, boolean activeCondition, String styleClass) {
        if (button == null) {
            return;
        }

        button.setDisable(!activeCondition);

        if (styleClass != null && !styleClass.isBlank()) {
            if (activeCondition) {
                if (!button.getStyleClass().contains(styleClass)) {
                    button.getStyleClass().add(styleClass);
                }
            } else {
                button.getStyleClass().remove(styleClass);
            }
        }
    }

    // Retorna um formatador de texto para conversão automática em maiúsculas
    public static TextFormatter<String> upperCaseFormatter(){
        return new TextFormatter<>(change -> {
            change.setText(change.getText().toUpperCase());
            return change;
        });
    }

    // Retorna a localização geográfica configurada para o Brasil
    public static Locale getBrLocation(){
        return LOCALE_BR; 
    }

    // Carrega e exibe uma janela modal sobreposta de aviso com fundo desfocado
    public static void showWarningOverlay(Pane overlayPane, Node nodeToBlur, String message, Runnable onConfirmAction) {
        try {
            FXMLLoader loader = new FXMLLoader(ValidationUtils.class.getResource("/fxml/WarningWindow.fxml"));
            Parent warningRoot = loader.load();

            WarningWindowController warningController = loader.getController();
            warningController.setWarningMessage(message);

            if (onConfirmAction != null) {
                warningController.setOnConfirmAction(onConfirmAction);
            }
            
            if (nodeToBlur != null) {
                AnimationsUtils.applyBackdropEffect(warningRoot, nodeToBlur, new GaussianBlur(15));
            }

            overlayPane.getChildren().setAll(warningRoot);
            overlayPane.setOpacity(0);
            overlayPane.setVisible(true);

            Platform.runLater(() -> AnimationsUtils.popupOpenAnimation(overlayPane, warningRoot)); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Calcula o saldo total somando receitas e subtraindo despesas
    public static BigDecimal calculateTotalBalance(List<? extends Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal balance = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if ("RECEITA".equalsIgnoreCase(t.getType())) {
                balance = balance.add(t.getValue());
            } else {
                balance = balance.subtract(t.getValue());
            }
        }

        return balance;
    }
}