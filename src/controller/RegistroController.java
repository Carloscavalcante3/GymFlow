package src.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.Main;
import src.model.Aluno;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class RegistroController {
    @FXML
    private TextField nomeField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField senhaField;
    
    @FXML
    private ComboBox<String> grupoComboBox;
    
    @FXML
    private Text mensagemErro;
    
    @FXML
    public void initialize() {
        grupoComboBox.getItems().addAll(
            "Iniciante",
            "Intermediário",
            "Avançado"
        );
    }
    
    @FXML
    private void cadastrar() {
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        String senha = senhaField.getText().trim();
        String grupo = grupoComboBox.getValue();
        
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || grupo == null) {
            mensagemErro.setText("Por favor, preencha todos os campos.");
            mensagemErro.setVisible(true);
            return;
        }
        
        try {
            // Garante que o diretório data existe
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            
            // Lê o arquivo de configuração existente
            File configFile = new File("data/users.json");
            Map<String, List<Map<String, String>>> config;
            
            if (configFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                Type type = new TypeToken<Map<String, List<Map<String, String>>>>(){}.getType();
                config = new Gson().fromJson(reader, type);
                reader.close();
            } else {
                config = new HashMap<>();
                config.put("users", new ArrayList<>());
            }
            
            // Verifica se o e-mail já está cadastrado
            boolean emailExists = config.get("users").stream()
                .anyMatch(user -> user.get("email").equals(email));
            
            if (emailExists) {
                mensagemErro.setText("Este e-mail já está cadastrado.");
                mensagemErro.setVisible(true);
                return;
            }
            
            // Adiciona o novo usuário
            Map<String, String> newUser = new HashMap<>();
            newUser.put("email", email);
            newUser.put("senha", senha);
            newUser.put("nome", nome);
            newUser.put("grupo", grupo);
            config.get("users").add(newUser);
            
            // Salva o arquivo atualizado
            try (Writer writer = new FileWriter(configFile)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(config, writer);
            }
            
            // Faz login com o novo usuário
            LoginController.setUsuarioLogado(new Aluno(nome, grupo));
            navegarParaTreinos();
            
        } catch (Exception e) {
            e.printStackTrace();
            mensagemErro.setText("Erro ao cadastrar usuário.");
            mensagemErro.setVisible(true);
        }
    }
    
    @FXML
    private void voltarParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) nomeField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
            mensagemErro.setText("Erro ao voltar para tela de login.");
            mensagemErro.setVisible(true);
        }
    }
    
    private void navegarParaTreinos() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ListaTreinos.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) nomeField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
            mensagemErro.setText("Erro ao abrir tela principal.");
            mensagemErro.setVisible(true);
        }
    }
} 