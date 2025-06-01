package src.model;

import java.time.LocalDateTime;

public class Mensagem {
    private String aluno;
    private String assunto;
    private String mensagem;
    private LocalDateTime data;
    private String instrutor;
    private boolean lida;

    public Mensagem(String aluno, String assunto, String mensagem, LocalDateTime data, String instrutor) {
        this.aluno = aluno;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.data = data;
        this.instrutor = instrutor;
        this.lida = false;
    }

    // Getters e Setters
    public String getAluno() {
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getInstrutor() {
        return instrutor;
    }

    public void setInstrutor(String instrutor) {
        this.instrutor = instrutor;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }
} 