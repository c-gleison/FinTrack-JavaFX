package com.cg.fintrackgui.model;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle("FinTrack - Gerenciador financeiro");
        stage.setScene(scene);
        stage.setMinWidth(520);
        stage.setMinHeight(430);
        //stage.initStyle(StageStyle.TRANSPARENT);

        stage.show();
    }
}
