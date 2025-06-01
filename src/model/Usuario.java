package src.model;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String grupo;
    private int pontos;

    public Usuario() {
        // Construtor vazio para o GSON
    }

    public Usuario(String nome, String email, String senha, String grupo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.grupo = grupo;
        this.pontos = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
} 