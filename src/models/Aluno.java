package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Aluno extends Pessoa {
    private int id;
    private int idade;
    private double peso;
    private double altura;
    private PlanoTreino planoDeTreinoAtual;
    private List<RegistroTreino> historicoTreinos;
    private int pontos;
    private List<Evento> eventosInscritos;

    private static int proximoId = 1;

    public Aluno(String nome, int idade, double peso, double altura) {
        super(nome);
        this.id = proximoId++;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
        this.historicoTreinos = new ArrayList<>();
        this.pontos = 0;
        this.eventosInscritos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public PlanoTreino getPlanoDeTreinoAtual() {
        return planoDeTreinoAtual;
    }

    public void setPlanoDeTreinoAtual(PlanoTreino planoDeTreinoAtual) {
        this.planoDeTreinoAtual = planoDeTreinoAtual;
    }

    public List<RegistroTreino> getHistoricoTreinos() {
        return historicoTreinos;
    }

    public int getPontos() {
        return pontos;
    }

    public List<Evento> getEventosInscritos() {
        return eventosInscritos;
    }

    public void registrarTreino(RegistroTreino registro) {
        this.historicoTreinos.add(registro);
        adicionarPontos(10);
    }

    public void adicionarPontos(int quantidade) {
        if (quantidade > 0) {
            this.pontos += quantidade;
        }
    }

    public void inscreverEmEvento(Evento evento) {
        if (!eventosInscritos.contains(evento)) {
            eventosInscritos.add(evento);
            evento.adicionarParticipante(this);
            adicionarPontos(15);
        }
    }

    public List<RegistroTreino> consultarHistoricoPorData(LocalDate dataFiltro) {
        return historicoTreinos.stream()
                .filter(registro -> registro.getData().equals(dataFiltro))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Aluno: %s, Idade: %d, Peso: %.1f kg, Altura: %.2f m, Pontos: %d",
                id, getNome(), idade, peso, altura, pontos);
    }
}