package main;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.PlanoTreino;
import repository.PlanoTreinoRepositorio;

public class PlanoTreinoGUI {
    private static final PlanoTreinoRepositorio repositorio = new PlanoTreinoRepositorio();

    public static void exibirTela() {
        Stage stage = new Stage();

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do Plano");

        TextArea descricaoArea = new TextArea();
        descricaoArea.setPromptText("Descrição do treino");

        Button salvarBtn = new Button("Salvar");
        Label status    = new Label();

        ListView<PlanoTreino> lista = new ListView<>();
        lista.setPrefHeight(200);

        salvarBtn.setOnAction(e -> {
            String nome      = nomeField.getText();
            String descricao = descricaoArea.getText();

            if (nome.isEmpty() || descricao.isEmpty()) {
                status.setText("Preencha todos os campos!");
                return;
            }

            PlanoTreino plano = new PlanoTreino(nome, descricao);
            repositorio.adicionarPlano(plano);
            lista.getItems().add(plano);

            status.setText("Plano cadastrado!");
            nomeField.clear();
            descricaoArea.clear();
        });

        VBox layout = new VBox(10,
            new HBox(10, nomeField, salvarBtn),
            descricaoArea,
            status,
            new Label("Planos de treino cadastrados:"),
            lista
        );
        layout.setStyle("-fx-padding: 20");

        stage.setScene(new Scene(layout, 600, 350));
        stage.setTitle("Cadastro e Listagem de Planos");
        stage.show();
    }
}
