import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.model.User;
import src.model.UserRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @BeforeEach
    void configurarAmbienteDeTeste() throws IOException {
        try (FileWriter writer = new FileWriter("data/users.json")) {
            writer.write("[]");
        }
        UserRepository.loadUsersFromFile();
    }

    @Test
    void deveSalvarEEncontrarUsuario() {
        User novoUsuario = new User("Usuário de Teste", 30, 70.5, 1.75, "usuarioteste", "senha123");
        UserRepository.save(novoUsuario);

        User usuarioEncontrado = UserRepository.find("usuarioteste", "senha123");
        assertNotNull(usuarioEncontrado, "O usuário deveria ser encontrado.");
        assertEquals("Usuário de Teste", usuarioEncontrado.getName(), "O nome do usuário não confere.");

        User usuarioNaoEncontrado = UserRepository.find("usuarioteste", "senhaincorreta");
        assertNull(usuarioNaoEncontrado, "O usuário não deveria ser encontrado com a senha incorreta.");
    }

    @Test
    void deveCarregarUsuariosDoArquivo() {
        List<User> usuarios = UserRepository.getUsers();
        assertTrue(usuarios.isEmpty(), "A lista de usuários deveria estar vazia inicialmente.");

        User usuario1 = new User("Alice", 25, 60.0, 1.65, "alice", "segredo");
        UserRepository.save(usuario1);

        UserRepository.loadUsersFromFile(); // Força o recarregamento do arquivo
        usuarios = UserRepository.getUsers();

        assertEquals(1, usuarios.size(), "Deveria haver um usuário na lista.");
        assertEquals("Alice", usuarios.get(0).getName(), "O nome do usuário carregado não confere.");
    }
}