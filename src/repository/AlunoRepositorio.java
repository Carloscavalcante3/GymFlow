package repository;

import models.Aluno;
import java.util.ArrayList;
import java.util.List;

public class AlunoRepositorio {
    private final List<Aluno> alunos = new ArrayList<>();

    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }
}
