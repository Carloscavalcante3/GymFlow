package src.test.model;

import org.junit.jupiter.api.Test;
import src.model.Treino;
import static org.junit.jupiter.api.Assertions.*;

class TreinoTest {
    
    @Test
    void deveCalcularPontuacaoCorretamente() {
        Treino treino = new Treino("Treino Teste");
        treino.adicionarExercicio("Supino", 3, 12);
        treino.adicionarExercicio("Agachamento", 4, 10);
        
        // Primeiro exercício: 10 (base) + (3 * 5) + (12/10 * 3) = 10 + 15 + 3 = 28
        // Segundo exercício: 10 (base) + (4 * 5) + (10/10 * 3) = 10 + 20 + 3 = 33
        // Total: 28 + 33 = 61
        assertEquals(61, treino.getPontuacao());
    }
    
    @Test
    void deveGerenciarExerciciosCorretamente() {
        Treino treino = new Treino("Treino Teste");
        treino.adicionarExercicio("Supino", 3, 12);
        
        assertEquals(1, treino.getExercicios().size());
        assertEquals("Supino (3x12)", treino.getExercicios().get(0).toString());
    }
} 