package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import src.Main;
import javafx.scene.text.Text;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.*;
import java.lang.reflect.Type;

public class RegisterController {
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
        grupoComboBox.getItems().addAll("Iniciante", "Intermediário", "Avançado");
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
            File configFile = new File("data/users.json");
            Map<String, List<Map<String, String>>> config;
            
            if (configFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                Type type = new TypeToken<Map<String, List<Map<String, String>>>>(){}.getType();
                config = new Gson().fromJson(reader, type);
                reader.close();
                
                if (config == null) {
                    config = new HashMap<>();
                    config.put("users", new ArrayList<>());
                }
            } else {
                config = new HashMap<>();
                config.put("users", new ArrayList<>());
            }
            
            // Verificar se o email já existe
            boolean emailExiste = config.get("users").stream()
                .anyMatch(user -> user.get("email").equals(email));
            
            if (emailExiste) {
                mensagemErro.setText("Este email já está cadastrado.");
                mensagemErro.setVisible(true);
                return;
            }
            
            // Adicionar novo usuário
            Map<String, String> novoUsuario = new HashMap<>();
            novoUsuario.put("email", email);
            novoUsuario.put("senha", senha);
            novoUsuario.put("nome", nome);
            novoUsuario.put("grupo", grupo);
            
            config.get("users").add(novoUsuario);
            
            // Salvar arquivo
            try (FileWriter writer = new FileWriter(configFile)) {
                new Gson().toJson(config, writer);
            }
            
            // Navegar para tela de login
            voltarParaLogin();
            
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
}