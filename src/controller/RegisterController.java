package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import src.model.User;
import src.model.UserRepository;
import javafx.scene.Parent; // Adicione esta importação se não estiver lá
import javafx.scene.layout.VBox;
public class RegisterController {
    @FXML
    private TextField name, age, weight, height, username;
    @FXML
    private PasswordField password;
    @FXML
    private Label error;

    @FXML
    public void register() throws Exception {
        if (name.getText().isEmpty() || age.getText().isEmpty() || weight.getText().isEmpty() || height.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty()) {
            error.setText("Fill all fields");
            return;
        }

        User user = new User(name.getText(), Integer.parseInt(age.getText()), Double.parseDouble(weight.getText()), Double.parseDouble(height.getText()), username.getText(), password.getText());
        UserRepository.save(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());

        Stage stage = (Stage) name.getScene().getWindow();
        stage.setScene(scene);
    }
}