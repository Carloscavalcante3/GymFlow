package repository;

import java.util.ArrayList;
import java.util.List;
import models.Aluno;

public class AlunoRepositorio {
    private List<Aluno> alunos = new ArrayList<>();

    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public void listarAlunos() {
        for (Aluno aluno : alunos) {
            aluno.exibirDados();
            System.out.println("------------------");
        }
    }
}
