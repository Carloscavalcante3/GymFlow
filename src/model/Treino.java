package src.model;

import java.util.ArrayList;
import java.util.List;

public class Treino {
    private String nome;
    private List<Exercicio> exercicios;
    private boolean concluido;
    private int pontuacao;
    
    public Treino(String nome) {
        this.nome = nome;
        this.exercicios = new ArrayList<>();
        this.concluido = false;
        this.pontuacao = 0;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<Exercicio> getExercicios() {
        return exercicios;
    }
    
    public void adicionarExercicio(String nome, int series, int repeticoes) {
        exercicios.add(new Exercicio(nome, series, repeticoes));
        calcularPontuacao();
    }
    
    public void removerExercicio(Exercicio exercicio) {
        exercicios.remove(exercicio);
        calcularPontuacao();
    }
    
    public boolean isConcluido() {
        return concluido;
    }
    
    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }
    
    public int getPontuacao() {
        return pontuacao;
    }
    
    private void calcularPontuacao() {
        // Cada exercício vale 10 pontos base
        // Cada série adiciona 5 pontos
        // Cada 10 repetições adicionam 3 pontos
        pontuacao = exercicios.stream()
            .mapToInt(e -> 10 + (e.series * 5) + ((e.repeticoes / 10) * 3))
            .sum();
    }
    
    public static class Exercicio {
        private String nome;
        private int series;
        private int repeticoes;
        
        public Exercicio(String nome, int series, int repeticoes) {
            this.nome = nome;
            this.series = series;
            this.repeticoes = repeticoes;
        }
        
        public String getNome() {
            return nome;
        }
        
        public void setNome(String nome) {
            this.nome = nome;
        }
        
        public int getSeries() {
            return series;
        }
        
        public void setSeries(int series) {
            this.series = series;
        }
        
        public int getRepeticoes() {
            return repeticoes;
        }
        
        public void setRepeticoes(int repeticoes) {
            this.repeticoes = repeticoes;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%dx%d)", nome, series, repeticoes);
        }
    }
} 