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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    
    private static class Evento {
        LocalDate data;
        LocalTime hora;
        String tipo;
        String nome;
        
        public Evento(LocalDate data, LocalTime hora, String tipo, String nome) {
            this.data = data;
            this.hora = hora;
            this.tipo = tipo;
            this.nome = nome;
        }
        
        @Override
        public String toString() {
            return String.format("%s %s - %s: %s", 
                data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                hora.format(DateTimeFormatter.ofPattern("HH:mm")),
                tipo, nome);
        }
    }

    @FXML
    public void initialize() {
        String nomeUsuario = LoginController.getUsuarioLogado().getNome();
        nomeUsuarioText.setText(nomeUsuario);
        atualizarPontos();
        
        eventos = FXCollections.observableArrayList();
        listaEventos.setItems(eventos);
        
        calendario.valueProperty().addListener((obs, oldVal, newVal) -> {
            atualizarEventos(newVal);
        });
        
        calendario.setValue(LocalDate.now());
    }
    
    private void atualizarPontos() {
        if (pontosText != null) {
            pontosText.setText(String.format("%d pts", LoginController.getUsuarioLogado().getPontos()));
        }
    }
    
    private void atualizarEventos(LocalDate data) {
        eventos.clear();
        // Implementar carregamento de eventos do dia selecionado
    }
    
    @FXML
    private void adicionarEvento() {
        Dialog<Evento> dialog = new Dialog<>();
        dialog.setTitle("Novo Evento");
        dialog.setHeaderText("Adicionar Novo Evento");
        
        // Campos do formulário
        DatePicker dataPicker = new DatePicker(LocalDate.now());
        Spinner<Integer> horaSpinner = new Spinner<>(0, 23, 8);
        Spinner<Integer> minutoSpinner = new Spinner<>(0, 59, 0);
        ComboBox<String> tipoCombo = new ComboBox<>();
        tipoCombo.getItems().addAll(
            "Treino",
            "Consulta",
            "Avaliação",
            "Evento de comunidade",
            "Corridas"
        );
        tipoCombo.setValue("Treino");
        TextField nomeField = new TextField();
        
        // Layout
        VBox content = new VBox(10);
        content.getChildren().addAll(
            new Label("Data:"), dataPicker,
            new Label("Hora:"), 
            new HBox(5, horaSpinner, new Label(":"), minutoSpinner),
            new Label("Tipo:"), tipoCombo,
            new Label("Nome:"), nomeField
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                LocalTime hora = LocalTime.of(horaSpinner.getValue(), minutoSpinner.getValue());
                return new Evento(dataPicker.getValue(), hora, tipoCombo.getValue(), nomeField.getText());
            }
            return null;
        });
        
        Optional<Evento> resultado = dialog.showAndWait();
        resultado.ifPresent(evento -> {
            if (evento.data.equals(calendario.getValue())) {
                eventos.add(evento.toString());
            }
        });
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