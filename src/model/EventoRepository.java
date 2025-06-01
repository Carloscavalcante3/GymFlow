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

public class EventoRepository {
    private static final String ARQUIVO_EVENTOS = "data/eventos.json";
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) 
            (src, typeOfSrc, context) -> context.serialize(src.format(DateTimeFormatter.ISO_DATE_TIME)))
        .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) 
            (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME))
        .setPrettyPrinting()
        .create();

    public static List<Evento> carregarEventos() {
        try {
            File arquivo = new File(ARQUIVO_EVENTOS);
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                Type type = new TypeToken<Map<String, List<Evento>>>(){}.getType();
                Map<String, List<Evento>> dados = gson.fromJson(reader, type);
                
                if (dados != null && dados.containsKey("eventos")) {
                    return dados.get("eventos");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Evento> carregarEventosDoAluno(String nomeAluno) {
        return carregarEventos().stream()
            .filter(evento -> evento.getNomeAluno().equals(nomeAluno))
            .toList();
    }

    public static void salvarEvento(Evento evento) {
        List<Evento> eventos = carregarEventos();
        eventos.add(evento);
        salvarEventos(eventos);
    }

    public static void salvarEventos(List<Evento> eventos) {
        try {
            File arquivo = new File(ARQUIVO_EVENTOS);
            Map<String, List<Evento>> dados = new HashMap<>();
            dados.put("eventos", eventos);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                writer.write(gson.toJson(dados));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removerEvento(Evento evento) {
        List<Evento> eventos = carregarEventos();
        eventos.removeIf(e -> e.getData().equals(evento.getData()) && 
                             e.getNomeAluno().equals(evento.getNomeAluno()) &&
                             e.getTipo().equals(evento.getTipo()));
        salvarEventos(eventos);
    }

    public static void atualizarEvento(Evento eventoAntigo, Evento eventoNovo) {
        List<Evento> eventos = carregarEventos();
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            if (e.getData().equals(eventoAntigo.getData()) && 
                e.getNomeAluno().equals(eventoAntigo.getNomeAluno()) &&
                e.getTipo().equals(eventoAntigo.getTipo())) {
                eventos.set(i, eventoNovo);
                break;
            }
        }
        salvarEventos(eventos);
    }
} 