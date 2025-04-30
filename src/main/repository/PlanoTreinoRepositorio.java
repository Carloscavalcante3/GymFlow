package repository;

import models.PlanoTreino;
import java.util.ArrayList;
import java.util.List;

public class PlanoTreinoRepositorio {
    private final List<PlanoTreino> planos = new ArrayList<>();

    public void adicionarPlano(PlanoTreino plano) {
        planos.add(plano);
    }

    public List<PlanoTreino> getPlanos() {
        return planos;
    }
}
