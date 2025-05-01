package main;

import models.PlanoTreino;
import repository.PlanoTreinoRepositorio;

public class MainPlanoTreino {
    public static void main(String[] args) {
        PlanoTreinoRepositorio repo = new PlanoTreinoRepositorio();

        PlanoTreino plano1 = new PlanoTreino("Treino A", "Treino de Peitoral, Tríceps e Ombros");
        PlanoTreino plano2 = new PlanoTreino("Treino B", "Treino de Quadriceps, Posterior e Panturrilha");

        repo.adicionarPlano(plano1);
        repo.adicionarPlano(plano2);

        System.out.println("Lista de Planos de Treino:");
        repo.listarPlanos();
    }
}
