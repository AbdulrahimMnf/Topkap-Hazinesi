module org.example.topkapihazinensi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.topkapihazinensi to javafx.fxml;
    exports org.example.topkapihazinensi;
    exports org.example.topkapihazinensi.models;
    opens org.example.topkapihazinensi.models to javafx.fxml;
}