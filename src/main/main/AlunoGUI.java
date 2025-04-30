package main;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Aluno;
import repository.AlunoRepositorio;

public class AlunoGUI {
    private static final AlunoRepositorio repositorio = new AlunoRepositorio();

    public static void exibirTela() {
        Stage stage = new Stage();

        TextField nomeField   = new TextField(); nomeField.setPromptText("Nome");
        TextField idadeField  = new TextField(); idadeField.setPromptText("Idade");
        TextField pesoField   = new TextField(); pesoField.setPromptText("Peso");
        TextField alturaField = new TextField(); alturaField.setPromptText("Altura");
        Button   salvarBtn    = new Button("Salvar");
        Label    status       = new Label();

        ListView<Aluno> lista = new ListView<>();
        lista.setPrefHeight(200);

        salvarBtn.setOnAction(e -> {
            try {
                String nome   = nomeField.getText();
                int    idade  = Integer.parseInt(idadeField.getText());
                double peso   = Double.parseDouble(pesoField.getText());
                double altura = Double.parseDouble(alturaField.getText());

                if (nome.isEmpty()) {
                    status.setText("Preencha todos os campos!");
                    return;
                }

                Aluno novo = new Aluno(nome, idade, peso, altura);
                repositorio.adicionarAluno(novo);
                lista.getItems().add(novo);

                status.setText("Aluno cadastrado!");
                nomeField.clear(); idadeField.clear();
                pesoField.clear(); alturaField.clear();
            } catch (Exception ex) {
                status.setText("Erro ao cadastrar — verifique os valores.");
            }
        });

        VBox layout = new VBox(10,
            new HBox(10, nomeField, idadeField, pesoField, alturaField, salvarBtn),
            status,
            new Label("Alunos cadastrados:"),
            lista
        );
        layout.setStyle("-fx-padding: 20");

        stage.setScene(new Scene(layout, 600, 350));
        stage.setTitle("Cadastro e Listagem de Alunos");
        stage.show();
    }
}
