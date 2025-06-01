package src.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContatoInstrutorController {
    @FXML
    private TextField assuntoField;
    
    @FXML
    private TextArea mensagemArea;
    
    @FXML
    private Text mensagemFeedback;
    
    @FXML
    private ListView<String> historicoMensagens;
    
    @FXML
    private Text nomeUsuarioText;
    
    @FXML
    private Text pontosText;
    
    private ObservableList<String> mensagens;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        String nomeUsuario = LoginController.getUsuarioLogado().getNome();
        nomeUsuarioText.setText(nomeUsuario);
        atualizarPontos();
        
        mensagens = FXCollections.observableArrayList();
        historicoMensagens.setItems(mensagens);
        carregarHistoricoMensagens();
    }

    private void atualizarPontos() {
        if (pontosText != null) {
            pontosText.setText(String.format("%d pts", LoginController.getUsuarioLogado().getPontos()));
        }
    }

    private void carregarHistoricoMensagens() {
        try {
            File file = new File("data/mensagens.json");
            if (!file.exists() || file.length() == 0) {
                return;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                
                // Remove colchetes e divide por vírgulas
                String jsonContent = content.toString().replace("[", "").replace("]", "");
                String[] mensagensJson = jsonContent.split("},");
                
                for (String mensagemJson : mensagensJson) {
                    if (!mensagemJson.endsWith("}")) {
                        mensagemJson += "}";
                    }
                    
                    try {
                        Map<String, String> mensagem = new Gson().fromJson(mensagemJson, new TypeToken<Map<String, String>>(){}.getType());
                        if (mensagem != null) {
                            LocalDateTime data = LocalDateTime.parse(mensagem.get("data"));
                            String formatoMensagem = String.format("[%s] %s\nAssunto: %s\n%s\n",
                                data.format(formatter),
                                mensagem.get("aluno"),
                                mensagem.get("assunto"),
                                mensagem.get("mensagem"));
                            mensagens.add(formatoMensagem);
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao processar mensagem: " + mensagemJson);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void enviarMensagem() {
        String assunto = assuntoField.getText().trim();
        String mensagem = mensagemArea.getText().trim();
        
        if (assunto.isEmpty() || mensagem.isEmpty()) {
            mensagemFeedback.setText("Por favor, preencha todos os campos.");
            mensagemFeedback.setStyle("-fx-fill: red;");
            mensagemFeedback.setVisible(true);
            return;
        }
        
        // Formata a mensagem com data e hora
        LocalDateTime agora = LocalDateTime.now();
        String dataHora = agora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String mensagemFormatada = String.format("[%s] %s\n%s", dataHora, assunto, mensagem);
        
        // Adiciona ao histórico
        mensagens.add(0, mensagemFormatada);
        
        // Limpa os campos
        assuntoField.clear();
        mensagemArea.clear();
        
        // Mostra feedback
        mensagemFeedback.setText("Mensagem enviada com sucesso!");
        mensagemFeedback.setStyle("-fx-fill: green;");
        mensagemFeedback.setVisible(true);
    }
    
    @FXML
    private void navegarInicio() {
        voltar();
    }
    
    @FXML
    private void voltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ListaTreinos.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) assuntoField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void navegarCalendario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Calendario.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) assuntoField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 