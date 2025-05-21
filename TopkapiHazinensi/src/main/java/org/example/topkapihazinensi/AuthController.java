package org.example.topkapihazinensi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.topkapihazinensi.models.User;
import org.example.topkapihazinensi.untils.DatabaseConnection;
import org.example.topkapihazinensi.untils.UserSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController  {

    @FXML
    public TextField registerNameField;

    @FXML
    private  TextField emailField;

    @FXML
    private  TextField  passwordField;

    @FXML
    private  TextField registerEmailField;

    @FXML
    private  TextField  registerPasswordField;

    private DatabaseConnection databaseConnection;

    private UserSession userSession;

    private Alert alert;

    public AuthController() {
        databaseConnection = new DatabaseConnection();
        alert = new Alert(Alert.AlertType.INFORMATION);
    }


    @FXML
    protected void OnLoginButtonClicked(ActionEvent event) throws IOException {

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid email address");
        }
        else {

            String sql = "SELECT * FROM users  WHERE email = '" + email + "' AND password = '" + password + "' ";

            List<Map<String, Object>> users = DatabaseConnection.SelectExecute(sql);
            if (!users.isEmpty()) {
                Map<String, Object> userData = users.getFirst();

                userSession = new UserSession((int) userData.get("id"), (String) userData.get("name"), (String) userData.get("email"));
                userSession.login();
                goToIndex(event);

            } else {
                alert.setHeaderText("Username or Password Incorrect");
                alert.showAndWait();
            }
        }
    }



    @FXML
    protected void OnRegisterBtn(ActionEvent event) throws IOException {
        String name = registerNameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();

        String sql = "INSERT INTO users(name,email,password) VALUES('"+name+"','"+email+"','"+password+"')";

        if (email == "" || password == "")
        {
            alert.setHeaderText("username or password cannot be empty");
            alert.showAndWait();
        }
        else {
            Boolean user = DatabaseConnection.CUDExecute(sql);
            if (user) {
                alert.setContentText("Register successful");
                alert.showAndWait();

                    // Burda tekrar var ama sonra kaldircagimmmmmm
                    String login = "SELECT * FROM users  WHERE email = '" + email + "' AND password = '" + password + "' ";
                    List<Map<String, Object>> users = DatabaseConnection.SelectExecute(login);
                    Map<String, Object> userData = users.getFirst();
                    userSession = new UserSession((int) userData.get("id"), (String) userData.get("name"), (String) userData.get("email"));
                    userSession.login();
                    goToIndex(event);
                    //--------------------------------------------


                } else {
                alert.setContentText("Register failed");
                alert.showAndWait();
            }

        }

    }


    protected void goToIndex(ActionEvent event) throws IOException
    {
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("index.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    protected void OnCloseBtn() {
        System.exit(0);
    }


}
