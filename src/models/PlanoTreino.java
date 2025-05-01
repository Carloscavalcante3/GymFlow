package models;

public class PlanoTreino {
    private String nome;
    private String descricao;

    public PlanoTreino(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    public void exibirPlano() {
        System.out.println("Plano de Treino: " + nome);
        System.out.println("Descrição: " + descricao);
    }
}
