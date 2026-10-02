package com.cg.fintrackgui.model;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// Classe principal que inicializa a aplicação JavaFX
public class App extends Application {
    
    // Ponto de entrada da interface gráfica
    @Override
    public void start(Stage stage) throws IOException {
        
        // Carrega a fonte de ícones FontAwesome na memória
        Font fa = Font.loadFont(getClass().getResourceAsStream("/fonts/fa-solid-900.ttf"), 12);     

        // Carrega a tela principal a partir do arquivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 960, 576);
        scene.setFill(Color.TRANSPARENT); // Permite bordas arredondadas e transparência na cena

        // Configura o título e a cena na janela principal
        stage.setTitle("FinTrack - Gerenciador financeiro");
        stage.setScene(scene);

        // Define as dimensões mínimas permitidas para a janela
        stage.setMinWidth(768);
        stage.setMinHeight(432);

        // Exibe a janela na tela
        stage.show();    
    }
}