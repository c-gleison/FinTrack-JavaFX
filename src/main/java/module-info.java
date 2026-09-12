module com.cg.fintrackgui {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;

    opens com.cg.fintrackgui.controller to javafx.fxml;
    opens com.cg.fintrackgui.model to javafx.graphics, javafx.fxml;
    opens com.cg.fintrackgui.util to javafx.graphics, javafx.fxml;

    exports com.cg.fintrackgui.model;
    exports com.cg.fintrackgui.controller;
    exports com.cg.fintrackgui.dao;
    exports com.cg.fintrackgui.util;
}
