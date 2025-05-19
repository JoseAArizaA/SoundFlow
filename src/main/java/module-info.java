module org.jose.soundflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;
    requires java.desktop;


    opens org.jose.soundflow to javafx.fxml;
    exports org.jose.soundflow;
    exports org.jose.soundflow.DAO;
    opens org.jose.soundflow.DAO to javafx.fxml;
    opens org.jose.soundflow.viewController to javafx.fxml;
    opens org.jose.soundflow.baseDatos to java.xml.bind;
    opens org.jose.soundflow.model to javafx.base;
}