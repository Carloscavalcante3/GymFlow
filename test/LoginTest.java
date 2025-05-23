import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import src.model.User;
import src.model.UserRepository;
import javafx.scene.Node;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testfx.api.FxAssert.verifyThat;

class LoginTest extends ApplicationTest {

    @BeforeEach
    void configurarUsuarioDeTesteParaLogin() throws IOException {
        Path dataDir = Paths.get("data");
        if (!Files.exists(dataDir)) {
            Files.createDirectories(dataDir);
        }
        try (FileWriter writer = new FileWriter("data/users.json")) {
            writer.write("[{\"name\":\"Usuário Login\",\"age\":30,\"weight\":70.0,\"height\":1.80,\"username\":\"loginvalido\",\"password\":\"senhavalida\",\"points\":50}]");
        }
        UserRepository.loadUsersFromFile();
    }

    @AfterEach
    void limparAmbienteAposTeste() throws Exception {
        FxToolkit.cleanupStages();
        release(new KeyCode[]{});
        release(new MouseButton[]{});

        try (FileWriter writer = new FileWriter("data/users.json")) {
            writer.write("[]");
        }
        UserRepository.loadUsersFromFile();
    }

    @Override
    public void start(Stage palco) throws Exception {
        FXMLLoader carregador = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent noPrincipal = carregador.load();
        Scene cena = new Scene(noPrincipal);
        cena.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        palco.setScene(cena);
        palco.show();
        palco.toFront();
    }


    @Test
    void deveRealizarLoginComSucesso() {
        System.out.println("Tentando encontrar #username...");
        lookup("#username").query(); // Se não encontrar, falhará aqui com EmptyNodeQueryException
        System.out.println("Campo #username encontrado, tentando interagir...");

        clickOn("#username").write("loginvalido");
        clickOn("#password").write("senhavalida");
        clickOn("Login");
        verifyThat("#welcome", LabeledMatchers.hasText("Welcome, Usuário Login"));
        verifyThat("#points", LabeledMatchers.hasText("Points: 50"));
    }

    @Test
    void deveFalharLoginComCredenciaisInvalidas() {
        clickOn("#username").write("usuarioerrado");
        clickOn("#password").write("senhaerrada");
        clickOn("Login");
        // A mensagem de erro DEVE ser EXATAMENTE a que sua aplicação exibe.
        // Se o seu LoginController.java faz error.setText("Credenciais inválidas"), use isso.
        verifyThat("#error", LabeledMatchers.hasText("Invalid credentials")); //  OU "Credenciais inválidas"
    }

    @Test
    void deveNavegarParaTelaDeRegistro() throws IOException {
        // Garante que o arquivo de usuários esteja limpo para o contexto de navegação para registro, se necessário
        try (FileWriter writer = new FileWriter("data/users.json")) {
            writer.write("[]");
        }
        UserRepository.loadUsersFromFile();

        clickOn("Register"); // Assumindo que o texto do botão é "Register"
        verifyThat("#name", org.testfx.matcher.base.NodeMatchers.isVisible());
    }
}