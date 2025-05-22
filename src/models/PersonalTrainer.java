package models;

public class PersonalTrainer extends Pessoa {
    private int id;
    private static int proximoId = 1;

    public PersonalTrainer(String nome) {
        super(nome);
        this.id = proximoId++;
    }

    public int getId() {
        return id;
    }

    public PlanoTreino criarPlanoDeTreino(String nomePlano, Aluno aluno) {
        PlanoTreino novoPlano = new PlanoTreino(nomePlano, this, aluno);
        aluno.setPlanoDeTreinoAtual(novoPlano);
        return novoPlano;
    }

    public void adicionarExercicioAoPlano(PlanoTreino plano, Exercicio exercicio) {
        plano.adicionarExercicio(exercicio);
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Personal Trainer: %s", id, getNome());
    }
}