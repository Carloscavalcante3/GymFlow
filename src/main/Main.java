package main;
import repository.AlunoRepositorio;
import models.Aluno;

public class Main {
    public static void main(String[] args) {
        AlunoRepositorio repo = new AlunoRepositorio();

        Aluno aluno1 = new Aluno("Carlos", 19, 65, 1.60);
        repo.adicionarAluno(aluno1);
        repo.listarAlunos();
    }
}
