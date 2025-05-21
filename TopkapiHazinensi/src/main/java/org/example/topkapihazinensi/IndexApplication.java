package org.example.topkapihazinensi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IndexApplication  extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(1080);
        stage.setHeight(720);
        stage.centerOnScreen();
        // app icon
        Image icon = new Image(getClass().getResource("images/icon.png").toExternalForm());
        stage.getIcons().add(icon);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("index.fxml"));
        Scene mainScene = new Scene(loader.load());
        stage.setScene(mainScene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
