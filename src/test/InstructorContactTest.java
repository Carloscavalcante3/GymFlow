package src.test;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import src.Main;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InstructorContactTest {
    
    @Test
    public void testInstructorContact() throws Exception {
        Platform.startup(() -> {});
        
        Platform.runLater(() -> {
            try {
                // 1. Abrir o aplicativo
                Stage stage = new Stage();
                Main main = new Main();
                main.start(stage);
                
                Scene scene = stage.getScene();
                
                // 2. Preencher campos de login
                TextField emailField = (TextField) scene.lookup("#emailField");
                TextField senhaField = (TextField) scene.lookup("#senhaField");
                Button loginButton = (Button) scene.lookup(".login-button");
                
                assertNotNull(emailField, "Campo de email não encontrado");
                assertNotNull(senhaField, "Campo de senha não encontrado");
                assertNotNull(loginButton, "Botão de login não encontrado");
                
                emailField.setText("aluno@gymflow.com");
                senhaField.setText("123456");
                
                // 3. Clicar no botão de entrar
                Thread.sleep(1000);
                loginButton.fire();
                
                // 4. Verificar a funcionalidade de contato com instrutor
                Thread.sleep(1000);
                Button instructorButton = (Button) scene.lookup("Button[text='📨 Instrutor']");
                assertNotNull(instructorButton, "Botão de contato com instrutor não encontrado");
                
                instructorButton.fire();
                Thread.sleep(1000);
                
                assertNotNull(scene.lookup("#contatoInstrutor"), "Tela de contato com instrutor não encontrada");
                
                // 5. Fechar o aplicativo
                Thread.sleep(1000);
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        Thread.sleep(5000);
    }
} 