package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import src.Main;
import src.model.Aluno;
import src.model.Mensagem;
import src.model.MensagemRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ContatoInstrutorController {
    @FXML
    private TextArea mensagemArea;
    
    @FXML
    private TextField assuntoField;
    
    @FXML
    private ListView<String> historicoListView;
    
    @FXML
    private Text mensagemEnviadaText;
    
    private ObservableList<String> mensagens;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private Aluno alunoAtual;
    
    @FXML
    public void initialize() {
        alunoAtual = LoginController.getUsuarioLogado();
        mensagens = FXCollections.observableArrayList();
        historicoListView.setItems(mensagens);
        carregarHistoricoMensagens();
    }
    
    private void carregarHistoricoMensagens() {
        if (alunoAtual != null) {
            List<Mensagem> historicoMensagens = MensagemRepository.carregarMensagensDoAluno(alunoAtual.getNome());
            mensagens.clear();
            
            for (Mensagem msg : historicoMensagens) {
                String formatoMensagem = String.format("[%s] %s\nAssunto: %s\n%s\n",
                    msg.getData().format(formatter),
                    msg.getAluno(),
                    msg.getAssunto(),
                    msg.getMensagem());
                mensagens.add(formatoMensagem);
            }
        }
    }
    
    @FXML
    private void enviarMensagem() {
        if (alunoAtual == null) {
            mensagemEnviadaText.setText("Erro: Usuário não está logado");
            mensagemEnviadaText.setVisible(true);
            return;
        }
        
        String assunto = assuntoField.getText().trim();
        String mensagem = mensagemArea.getText().trim();
        
        if (assunto.isEmpty() || mensagem.isEmpty()) {
            mensagemEnviadaText.setText("Por favor, preencha todos os campos");
            mensagemEnviadaText.setVisible(true);
            return;
        }
        
        Mensagem novaMensagem = new Mensagem(
            alunoAtual.getNome(),
            assunto,
            mensagem,
            LocalDateTime.now(),
            null // instrutor será definido quando a mensagem for respondida
        );
        
        MensagemRepository.salvarMensagem(novaMensagem);
        
        // Limpar campos e mostrar confirmação
        assuntoField.clear();
        mensagemArea.clear();
        mensagemEnviadaText.setText("Mensagem enviada com sucesso!");
        mensagemEnviadaText.setVisible(true);
        
        // Atualizar histórico
        carregarHistoricoMensagens();
    }
    
    @FXML
    private void navegarCalendario() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/Calendario.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) mensagemArea.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void navegarTreinos() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ListaTreinos.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) mensagemArea.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void voltar() {
        navegarTreinos();
    }
} 