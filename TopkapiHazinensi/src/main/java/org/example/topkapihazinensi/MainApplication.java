package org.example.topkapihazinensi;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
         splashScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }


    // Splash Screen by ımage vıew
    private  void splashScreen()
    {
        Image splashImage = new Image(getClass().getResource("images/splash.jpg").toExternalForm());
        ImageView imageView = new ImageView(splashImage);
        imageView.setFitWidth(1080);
        imageView.setFitHeight(720);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);
        Scene splashScene = new Scene(root, 1080, 720);

        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(splashScene);
        splashStage.centerOnScreen();
        // app icon
        Image icon = new Image(getClass().getResource("images/icon.png").toExternalForm());
        splashStage.getIcons().add(icon);

        splashStage.show();

        // Simulate loading
        Task<Void> sleeper = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(3000);
                return null;
            }
        };

        sleeper.setOnSucceeded(event -> {
            // Go to auth page
            splashStage.close();  // 👈 Close the splash screen here
            authPage();

        });

        new Thread(sleeper).start();
    }

    // Call Auth(Login & Register) page
    private void authPage()  {
        try {
            Stage authStage = new Stage();
            authStage.initStyle(StageStyle.UNDECORATED);
            authStage.setWidth(1080);
            authStage.setHeight(720);
            authStage.centerOnScreen();
            // app icon
            Image icon = new Image(getClass().getResource("images/icon.png").toExternalForm());
            authStage.getIcons().add(icon);
            //----------------
            FXMLLoader loader = new FXMLLoader(getClass().getResource("auth.fxml"));
            Scene mainScene = new Scene(loader.load());
            authStage.setScene(mainScene);
            authStage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
