module com.cg.fintrackgui {
    
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive javafx.fxml;
    requires transitive org.controlsfx.controls;
    requires transitive java.sql;

    opens com.cg.fintrackgui.controller to javafx.fxml;
    opens com.cg.fintrackgui.model to javafx.graphics, javafx.fxml;
    opens com.cg.fintrackgui.util to javafx.graphics, javafx.fxml;

    exports com.cg.fintrackgui.model;
    exports com.cg.fintrackgui.controller;
    exports com.cg.fintrackgui.dao;
    exports com.cg.fintrackgui.util;
    exports com.cg.fintrackgui.service;

}
