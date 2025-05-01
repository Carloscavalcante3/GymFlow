package repository;

import models.PlanoTreino;
import java.util.ArrayList;
import java.util.List;

public class PlanoTreinoRepositorio {
    private List<PlanoTreino> planos = new ArrayList<>();

    public void adicionarPlano(PlanoTreino plano) {
        planos.add(plano);
    }

    public void listarPlanos() {
        for (PlanoTreino plano : planos) {
            plano.exibirPlano();
            System.out.println("------------------");
        }
    }
}
