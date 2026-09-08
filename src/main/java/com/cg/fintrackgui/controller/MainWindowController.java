package com.cg.fintrackgui.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainWindowController {
     @FXML
    private void addTransaction(ActionEvent event) {
        
        try {
            // Load the FXML layout file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewTransactionWindow.fxml"));
            Parent root = loader.load();  

            // Initialize the new stage and scene
            Stage formerStage = new Stage();
            formerStage.setScene(new Scene(root));
            // formerStage.initStyle(StageStyle.UNDECORATED);

            // Retrieve parent window and configure owner and modality
            Stage parentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            formerStage.initOwner(parentStage);
            formerStage.initModality(Modality.WINDOW_MODAL);

            // Center the modal window relative to the parent stage once rendered
            formerStage.setOnShown(e -> {
                double centerX = parentStage.getX() + (parentStage.getWidth() - formerStage.getWidth()) / 2;
                double centerY = parentStage.getY() + (parentStage.getHeight() - formerStage.getHeight()) / 2;

                formerStage.setX(centerX);
                formerStage.setY(centerY);
            });

            // Display the modal window and wait for user interaction
            formerStage.setResizable(false);
            formerStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
