package com.cg.fintrackgui.util;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class AnimationsUtils {
    
    // Variaveis globais
    private static final double ANIMATION_DURATION = 100;

    private AnimationsUtils() {}

    public static void popupOpenAnimation(Node overlay, Node popupContent){

        Duration duration = Duration.millis(ANIMATION_DURATION);

        FadeTransition fade = new FadeTransition(duration, overlay);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        ScaleTransition scale = new ScaleTransition(duration, popupContent);
        scale.setFromX(0.2);
        scale.setFromY(0.2);
        scale.setToX(1.0);
        scale.setToY(1.0);

        ParallelTransition animacaoSaida = new ParallelTransition(fade, scale);

        animacaoSaida.play();
    }

    public static void popupCloseAnimation(Node overlay, Node popupContent, Runnable onFinished){

        Duration duration = Duration.millis(ANIMATION_DURATION);

        FadeTransition fade = new FadeTransition(duration, overlay);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ScaleTransition scale = new ScaleTransition(duration, popupContent);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.2);
        scale.setToY(0.2);

        ParallelTransition animacaoSaida = new ParallelTransition(fade, scale);
        
        if (onFinished != null) {
            animacaoSaida.setOnFinished(e -> onFinished.run());
        }

        animacaoSaida.play();
    }

    public static void closeOverlay(Node overlay, Node popupContent) {

        if (popupContent == null) return;

        AnimationsUtils.popupCloseAnimation(overlay, popupContent, () -> {

            if (popupContent.getScene() != null) {
                Node mainPane = popupContent.getScene().lookup("#mainBorderPane");
                if (mainPane != null) {
                    mainPane.setEffect(null);
                }
            }

            if (overlay instanceof Pane parentPane) {
                parentPane.setVisible(false);
                parentPane.getChildren().remove(popupContent);

                // Desliga o cache de renderização
                parentPane.setCache(false);

                // Restaura a opacidade padrão do overlay para a próxima abertura
                overlay.setOpacity(1.0);
            }

            // Restaura as escalas do conteúdo
            popupContent.setScaleX(1.0);
            popupContent.setScaleY(1.0);
            popupContent.setOpacity(1.0);
        });
    }
     
}
