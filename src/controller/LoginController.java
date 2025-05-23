package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import src.model.User;
import src.model.UserRepository;

public class LoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Label error;

    @FXML
    public void login() throws Exception {
        User user = UserRepository.find(username.getText(), password.getText());
        if (user != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

            DashboardController controller = loader.getController();
            controller.setUser(user);
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(scene);
        } else {
            error.setText("Invalid credentials");
        }
    }

    @FXML
    public void goToRegister() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        Stage stage = (Stage) username.getScene().getWindow();
        stage.setScene(scene);
    }
}
