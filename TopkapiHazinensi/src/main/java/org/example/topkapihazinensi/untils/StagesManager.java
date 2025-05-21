package org.example.topkapihazinensi.untils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StagesManager {

    public  void ChangeStage(String fxml) {
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setWidth(1080);
            stage.setHeight(720);
            stage.centerOnScreen();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene mainScene = new Scene(loader.load());
            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void changeRoot(Scene scene, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            scene.setRoot(loader.load());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void changePage(Scene currentScene, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            currentScene.setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }



    public static void changeToClass(Class<? extends Application> targetClass) {
        try {

            Stage newStage = new Stage();
            Application newApp = targetClass.getDeclaredConstructor().newInstance();
            newApp.start(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
