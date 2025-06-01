package src.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class TreinoRepository {
    private static final String ARQUIVO_TREINOS = "data/treinos.json";
    private static final Gson gson = new Gson();
    
    public static List<Treino> carregarTreinos(String email) {
        try {
            File arquivo = new File(ARQUIVO_TREINOS);
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));
            Type type = new TypeToken<Map<String, Map<String, List<Map<String, Object>>>>>(){}.getType();
            Map<String, Map<String, List<Map<String, Object>>>> dados = gson.fromJson(reader, type);
            reader.close();
            
            if (dados == null || !dados.containsKey("treinos") || !dados.get("treinos").containsKey(email)) {
                return new ArrayList<>();
            }
            
            List<Map<String, Object>> treinosJson = dados.get("treinos").get(email);
            List<Treino> treinos = new ArrayList<>();
            
            for (Map<String, Object> treinoJson : treinosJson) {
                Treino treino = new Treino((String) treinoJson.get("nome"));
                treino.setConcluido((Boolean) treinoJson.get("concluido"));
                
                List<Map<String, Object>> exerciciosJson = (List<Map<String, Object>>) treinoJson.get("exercicios");
                for (Map<String, Object> exercicioJson : exerciciosJson) {
                    treino.adicionarExercicio(
                        (String) exercicioJson.get("nome"),
                        ((Double) exercicioJson.get("series")).intValue(),
                        ((Double) exercicioJson.get("repeticoes")).intValue()
                    );
                }
                
                treinos.add(treino);
            }
            
            return treinos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public static void salvarTreinos(String email, List<Treino> treinos) {
        try {
            File arquivo = new File(ARQUIVO_TREINOS);
            Map<String, Map<String, List<Map<String, Object>>>> dados;
            
            if (arquivo.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(arquivo));
                Type type = new TypeToken<Map<String, Map<String, List<Map<String, Object>>>>>(){}.getType();
                dados = gson.fromJson(reader, type);
                reader.close();
                
                if (dados == null) {
                    dados = new HashMap<>();
                }
            } else {
                dados = new HashMap<>();
            }
            
            if (!dados.containsKey("treinos")) {
                dados.put("treinos", new HashMap<>());
            }
            
            List<Map<String, Object>> treinosJson = new ArrayList<>();
            
            for (Treino treino : treinos) {
                Map<String, Object> treinoJson = new HashMap<>();
                treinoJson.put("nome", treino.getNome());
                treinoJson.put("concluido", treino.isConcluido());
                treinoJson.put("pontuacao", treino.getPontuacao());
                
                List<Map<String, Object>> exerciciosJson = new ArrayList<>();
                for (Treino.Exercicio exercicio : treino.getExercicios()) {
                    Map<String, Object> exercicioJson = new HashMap<>();
                    exercicioJson.put("nome", exercicio.getNome());
                    exercicioJson.put("series", exercicio.getSeries());
                    exercicioJson.put("repeticoes", exercicio.getRepeticoes());
                    exerciciosJson.add(exercicioJson);
                }
                
                treinoJson.put("exercicios", exerciciosJson);
                treinosJson.add(treinoJson);
            }
            
            dados.get("treinos").put(email, treinosJson);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo));
            writer.write(gson.toJson(dados));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 