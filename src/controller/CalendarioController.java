package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.Main;
import src.model.Aluno;
import src.model.Evento;
import src.model.EventoRepository;
import src.model.Treino;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class CalendarioController {
    @FXML
    private DatePicker calendario;
    
    @FXML
    private ListView<String> listaEventos;
    
    @FXML
    private Text nomeUsuarioText;
    
    @FXML
    private Text pontosText;
    
    private ObservableList<String> eventos;
    private Aluno alunoAtual;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    @FXML
    public void initialize() {
        alunoAtual = LoginController.getUsuarioLogado();
        if (alunoAtual != null) {
            nomeUsuarioText.setText(alunoAtual.getNome());
            atualizarPontos();
        }
        
        eventos = FXCollections.observableArrayList();
        listaEventos.setItems(eventos);
        
        calendario.valueProperty().addListener((obs, oldVal, newVal) -> {
            atualizarEventos(newVal);
        });
        
        calendario.setValue(LocalDate.now());
    }
    
    private void atualizarPontos() {
        if (pontosText != null) {
            pontosText.setText(String.format("%d pts", alunoAtual.getPontos()));
        }
    }
    
    private void atualizarEventos(LocalDate data) {
        eventos.clear();
        if (alunoAtual != null) {
            List<Evento> eventosDoAluno = EventoRepository.carregarEventosDoAluno(alunoAtual.getNome());
            
            for (Evento evento : eventosDoAluno) {
                LocalDate dataEvento = evento.getData().toLocalDate();
                if (dataEvento.equals(data)) {
                    String descricao = String.format("%s %s - %s: %s",
                        evento.getData().format(formatter),
                        evento.getTipo(),
                        evento.getTreino() != null ? evento.getTreino().getNome() : "",
                        evento.getObservacoes());
                    eventos.add(descricao);
                }
            }
        }
    }
    
    @FXML
    private void adicionarEvento() {
        if (alunoAtual == null) return;
        
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Novo Evento");
        dialog.setHeaderText("Adicionar Novo Evento");
        
        // Campos do formulário
        DatePicker dataPicker = new DatePicker(LocalDate.now());
        Spinner<Integer> horaSpinner = new Spinner<>(0, 23, 8);
        Spinner<Integer> minutoSpinner = new Spinner<>(0, 59, 0);
        ComboBox<String> tipoCombo = new ComboBox<>();
        tipoCombo.getItems().addAll(
            "Treino",
            "Corrida",
            "Evento de Comunidade",
            "Avaliação"
        );
        tipoCombo.setValue("Treino");
        TextField nomeField = new TextField();
        Spinner<Integer> duracaoSpinner = new Spinner<>(30, 180, 60);
        TextArea observacoesArea = new TextArea();
        observacoesArea.setPromptText("Observações (opcional)");
        
        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(
            new Label("Data:"), dataPicker,
            new Label("Hora:"), 
            new HBox(5, horaSpinner, new Label(":"), minutoSpinner),
            new Label("Tipo:"), tipoCombo,
            new Label("Nome:"), nomeField,
            new Label("Duração (minutos):"), duracaoSpinner,
            new Label("Observações:"), observacoesArea
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Atualizar visibilidade do campo nome baseado no tipo
        tipoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isTreino = "Treino".equals(newVal);
            nomeField.setPromptText(isTreino ? "Nome do treino" : "Nome do evento");
        });
        
        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            LocalDateTime dataHora = LocalDateTime.of(
                dataPicker.getValue(),
                LocalTime.of(horaSpinner.getValue(), minutoSpinner.getValue())
            );
            
            // Criar um treino apenas se o tipo for "Treino"
            Treino treino = null;
            if ("Treino".equals(tipoCombo.getValue())) {
                treino = new Treino(nomeField.getText());
            }
            
            // Criar e salvar o evento
            Evento novoEvento = new Evento(
                tipoCombo.getValue(),
                alunoAtual.getNome(),
                alunoAtual.getGrupo(),
                dataHora,
                duracaoSpinner.getValue(),
                treino,
                observacoesArea.getText()
            );
            
            EventoRepository.salvarEvento(novoEvento);
            atualizarEventos(calendario.getValue());
        }
    }
    
    @FXML
    private void navegarInicio() {
        navegarTreinos();
    }
    
    @FXML
    private void navegarTreinos() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ListaTreinos.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) calendario.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void navegarCalendario() {
        // Já está na tela de calendário
    }
    
    @FXML
    private void abrirContatoInstrutor() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ContatoInstrutor.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) calendario.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 