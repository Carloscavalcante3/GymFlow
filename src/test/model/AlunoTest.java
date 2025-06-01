package src.test.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.model.Aluno;
import src.model.Treino;
import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("Aluno Teste", "Iniciante");
    }

    @Test
    void deveAcumularPontosAoConcluirTreino() {
        Treino treino = new Treino("Treino Teste");
        treino.adicionarExercicio("Supino", 3, 12);
        treino.adicionarExercicio("Agachamento", 4, 10);
        
        aluno.adicionarTreino(treino);
        assertEquals(0, aluno.getPontos());
        
        aluno.concluirTreino(treino);
        assertTrue(treino.isConcluido());
        assertEquals(61, aluno.getPontos());
    }

    @Test
    void deveGerenciarTreinosCorretamente() {
        Treino treino1 = new Treino("Treino A");
        Treino treino2 = new Treino("Treino B");
        
        aluno.adicionarTreino(treino1);
        aluno.adicionarTreino(treino2);
        
        assertEquals(2, aluno.getTreinos().size());
        assertTrue(aluno.getTreinos().contains(treino1));
        assertTrue(aluno.getTreinos().contains(treino2));
    }
} 