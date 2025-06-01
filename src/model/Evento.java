package src.model;

import java.time.LocalDateTime;

public class Evento {
    private String tipo;
    private String nomeAluno;
    private String grupoAluno;
    private LocalDateTime data;
    private int duracao; // em minutos
    private Treino treino;
    private String observacoes;

    public Evento(String tipo, String nomeAluno, String grupoAluno, LocalDateTime data, int duracao, Treino treino, String observacoes) {
        this.tipo = tipo;
        this.nomeAluno = nomeAluno;
        this.grupoAluno = grupoAluno;
        this.data = data;
        this.duracao = duracao;
        this.treino = treino;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getGrupoAluno() {
        return grupoAluno;
    }

    public void setGrupoAluno(String grupoAluno) {
        this.grupoAluno = grupoAluno;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public Treino getTreino() {
        return treino;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
} 