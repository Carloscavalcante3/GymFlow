package models;

public class PlanoTreino {
    private String nome;
    private String descricao;

    public PlanoTreino(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return nome;
    }
}
