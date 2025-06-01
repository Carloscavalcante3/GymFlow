package src.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.application.Platform;

public class Aluno {
    private String nome;
    private String grupo;
    private List<Treino> treinos;
    private IntegerProperty pontos;

    public Aluno(String nome, String grupo) {
        this.nome = nome;
        this.grupo = grupo;
        this.treinos = new ArrayList<>();
        this.pontos = new SimpleIntegerProperty(0);
    }

    public void adicionarTreino(Treino treino) {
        treinos.add(treino);
        atualizarPontos();
    }

    public void removerTreino(Treino treino) {
        treinos.remove(treino);
        if (treino.isConcluido()) {
            Platform.runLater(() -> {
                int novosPontos = Math.max(0, getPontos() - treino.getPontuacao());
                pontos.set(novosPontos);
            });
        }
    }

    public void concluirTreino(Treino treino) {
        Platform.runLater(() -> {
            int novosPontos = getPontos() + treino.getPontuacao();
            pontos.set(novosPontos);
        });
    }

    public void desconcluirTreino(Treino treino) {
        Platform.runLater(() -> {
            int novosPontos = Math.max(0, getPontos() - treino.getPontuacao());
            pontos.set(novosPontos);
        });
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public List<Treino> getTreinos() {
        return treinos;
    }

    public void setTreinos(List<Treino> treinos) {
        this.treinos = treinos;
        atualizarPontos();
    }

    private void atualizarPontos() {
        Platform.runLater(() -> {
            int novosPontos = treinos.stream()
                .filter(Treino::isConcluido)
                .mapToInt(Treino::getPontuacao)
                .sum();
            pontos.set(novosPontos);
        });
    }

    public int getPontos() {
        return pontos.get();
    }

    public void setPontos(int value) {
        Platform.runLater(() -> {
            pontos.set(Math.max(0, value));
        });
    }

    public IntegerProperty pontosProperty() {
        return pontos;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %d pts", nome, grupo, getPontos());
    }
} 