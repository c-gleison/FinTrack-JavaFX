package com.cg.fintrackgui.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;

public class ValidationUtils {

    private static final Locale LOCALE_BR = new Locale("pt", "BR");

    private ValidationUtils(){ }
    

    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            return "R$ 0,00";
        }
        return NumberFormat.getCurrencyInstance(LOCALE_BR).format(value);
    }

    public static TextFormatter<String> currencyFormatter() {

        UnaryOperator<TextFormatter.Change> filter = change -> {

            String digits = change.getControlNewText().replaceAll("\\D", "");

            if (digits.isEmpty()) {
                change.setRange(0, change.getControlText().length());
                change.setText("");
                return change;
            }

            BigDecimal value = new BigDecimal(digits).divide(new BigDecimal("100"));
            String formatted = formatCurrency(value);

            change.setRange(0, change.getControlText().length());
            change.setText(formatted);
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        };

        return new TextFormatter<>(filter);
    }

    

}

