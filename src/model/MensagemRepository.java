package src.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MensagemRepository {
    private static final String ARQUIVO_MENSAGENS = "data/mensagens.json";
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) 
            (src, typeOfSrc, context) -> context.serialize(src.format(DateTimeFormatter.ISO_DATE_TIME)))
        .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) 
            (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME))
        .setPrettyPrinting()
        .create();

    public static List<Mensagem> carregarMensagens() {
        try {
            File arquivo = new File(ARQUIVO_MENSAGENS);
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                Type type = new TypeToken<Map<String, List<Mensagem>>>(){}.getType();
                Map<String, List<Mensagem>> dados = gson.fromJson(reader, type);
                
                if (dados != null && dados.containsKey("mensagens")) {
                    return dados.get("mensagens");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Mensagem> carregarMensagensDoAluno(String nomeAluno) {
        return carregarMensagens().stream()
            .filter(msg -> msg.getAluno().equals(nomeAluno))
            .toList();
    }

    public static List<Mensagem> carregarMensagensNaoLidas(String nomeAluno) {
        return carregarMensagens().stream()
            .filter(msg -> msg.getAluno().equals(nomeAluno) && !msg.isLida())
            .toList();
    }

    public static void salvarMensagem(Mensagem mensagem) {
        List<Mensagem> mensagens = carregarMensagens();
        mensagens.add(mensagem);
        salvarMensagens(mensagens);
    }

    public static void salvarMensagens(List<Mensagem> mensagens) {
        try {
            File arquivo = new File(ARQUIVO_MENSAGENS);
            Map<String, List<Mensagem>> dados = new HashMap<>();
            dados.put("mensagens", mensagens);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                writer.write(gson.toJson(dados));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void marcarComoLida(Mensagem mensagem) {
        List<Mensagem> mensagens = carregarMensagens();
        for (Mensagem msg : mensagens) {
            if (msg.getData().equals(mensagem.getData()) && 
                msg.getAluno().equals(mensagem.getAluno()) &&
                msg.getAssunto().equals(mensagem.getAssunto())) {
                msg.setLida(true);
                break;
            }
        }
        salvarMensagens(mensagens);
    }

    public static void removerMensagem(Mensagem mensagem) {
        List<Mensagem> mensagens = carregarMensagens();
        mensagens.removeIf(msg -> msg.getData().equals(mensagem.getData()) && 
                                 msg.getAluno().equals(mensagem.getAluno()) &&
                                 msg.getAssunto().equals(mensagem.getAssunto()));
        salvarMensagens(mensagens);
    }
} 