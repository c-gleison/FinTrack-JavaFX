package com.cg.fintrackgui.util;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.CacheHint;
import javafx.scene.effect.Effect;

// Classe utilitária para gerenciamento de animações e efeitos visuais da interface
public class AnimationsUtils {

    // Constantes de configuração e chaves de propriedades para controle das animações
    private static final double ANIMATION_DURATION = 100;
    private static final String BACKDROP_KEY = "overlay.backdrop";
    private static final String CLOSING_KEY = "overlay.closing";

    // Construtor privado para evitar instanciação
    private AnimationsUtils() {}

    // Estrutura interna para armazenar o estado original do elemento de fundo
    private record BackdropState(Node node, Effect effect, boolean cache, CacheHint cacheHint) {}

    // Aplica efeito visual de fundo e salva o estado anterior para posterior restauração
    public static void applyBackdropEffect(Node popupContent, Node backdrop, Effect effect) {
        if (popupContent == null || backdrop == null) return;

        popupContent.getProperties().put(BACKDROP_KEY,
                new BackdropState(backdrop, backdrop.getEffect(), backdrop.isCache(), backdrop.getCacheHint()));

        backdrop.setEffect(effect);
        backdrop.setCache(true);
        backdrop.setCacheHint(CacheHint.SPEED);
    }
    
    // Executa a animação paralela de opacidade e escala para abertura de popups
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

    // Executa a animação paralela de opacidade e escala para fechamento de popups
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

    // Coordena o fechamento animado do overlay e restaura as propriedades originais do fundo
    public static void closeOverlay(Node overlay, Node popupContent) {
        if (popupContent == null || !(overlay instanceof Pane container)) return;

        // Evita execuções duplicadas ao disparar o fechamento
        if (popupContent.getProperties().putIfAbsent(CLOSING_KEY, Boolean.TRUE) != null) return;

        // Determina se deve animar o container completo ou apenas o popup
        boolean lastPopup = container.getChildren().size() == 1;
        Node fadeTarget = lastPopup ? container : popupContent;

        // Anima o fechamento e executa a limpeza e restauração ao finalizar
        popupCloseAnimation(fadeTarget, popupContent, () -> {
            container.getChildren().remove(popupContent);

            if (popupContent.getProperties().remove(BACKDROP_KEY) instanceof BackdropState s) {
                s.node().setEffect(s.effect());
                s.node().setCache(s.cache());
                s.node().setCacheHint(s.cacheHint());
            }

            if (container.getChildren().isEmpty()) {
                container.setVisible(false);
                container.setCache(false);
            }

            container.setOpacity(1.0);
            popupContent.setOpacity(1.0);
            popupContent.setScaleX(1.0);
            popupContent.setScaleY(1.0);
            popupContent.getProperties().remove(CLOSING_KEY);
        });
    }
}