package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button alunoBtn = new Button("Alunos");
        Button planoBtn = new Button("Planos de Treino");

        alunoBtn.setOnAction(e -> AlunoGUI.exibirTela());
        planoBtn.setOnAction(e -> PlanoTreinoGUI.exibirTela());

        VBox root = new VBox(10, alunoBtn, planoBtn);
        root.setStyle("-fx-padding: 20");

        primaryStage.setTitle("GymFlow - Menu");
        primaryStage.setScene(new Scene(root, 200, 120));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
