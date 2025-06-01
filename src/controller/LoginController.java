package src.controller;

import com.google.gson.Gson;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField senhaField;
    
    @FXML
    private Text mensagemErro;
    
    private static Aluno usuarioLogado;
    private static String emailUsuario;
    
    public static Aluno getUsuarioLogado() {
        return usuarioLogado;
    }
    
    public static void setUsuarioLogado(Aluno aluno) {
        usuarioLogado = aluno;
    }

    public static String getEmailUsuario() {
        return emailUsuario;
    }
    
    protected boolean autenticarUsuario(String email, String senha) {
        try {
            File configFile = new File("data/users.json");
            if (!configFile.exists()) {
                return false;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            Type type = new TypeToken<Map<String, List<Map<String, String>>>>(){}.getType();
            Map<String, List<Map<String, String>>> config = new Gson().fromJson(reader, type);
            reader.close();
            
            if (config == null || !config.containsKey("users")) {
                return false;
            }
            
            Optional<Map<String, String>> usuario = config.get("users").stream()
                .filter(user -> user.get("email").equals(email) && user.get("senha").equals(senha))
                .findFirst();
            
            if (usuario.isPresent()) {
                Map<String, String> dadosUsuario = usuario.get();
                usuarioLogado = new Aluno(dadosUsuario.get("nome"), dadosUsuario.get("grupo"));
                emailUsuario = email;
                return true;
            }
            
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @FXML
    private void fazerLogin() {
        String email = emailField.getText().trim();
        String senha = senhaField.getText().trim();
        
        if (email.isEmpty() || senha.isEmpty()) {
            mensagemErro.setText("Por favor, preencha todos os campos.");
            mensagemErro.setVisible(true);
            return;
        }
        
        if (autenticarUsuario(email, senha)) {
            navegarParaTreinos();
        } else {
            mensagemErro.setText("Email ou senha inválidos.");
            mensagemErro.setVisible(true);
        }
    }
    
    @FXML
    private void abrirTelaRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/Registro.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
            mensagemErro.setText("Erro ao abrir tela de registro.");
            mensagemErro.setVisible(true);
        }
    }
    
    protected void navegarParaTreinos() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ListaTreinos.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
            if (mensagemErro != null) {
                mensagemErro.setText("Erro ao abrir tela principal.");
                mensagemErro.setVisible(true);
            }
        }
    }
}
