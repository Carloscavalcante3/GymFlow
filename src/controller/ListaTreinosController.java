package src.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.Main;
import src.model.Aluno;
import src.model.Treino;
import src.model.TreinoRepository;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;

public class ListaTreinosController {
    @FXML
    private TableView<Treino> tabelaTreinos;
    
    @FXML
    private TableColumn<Treino, String> colunaNome;
    
    @FXML
    private TableColumn<Treino, String> colunaExercicios;
    
    @FXML
    private TableColumn<Treino, Integer> colunaPontos;
    
    @FXML
    private TableColumn<Treino, String> colunaStatus;
    
    @FXML
    private TableColumn<Treino, Void> colunaAcoes;
    
    @FXML
    private Text saudacaoText;
    
    @FXML
    private Text nomeUsuarioText;
    
    @FXML
    private Text pontosText;
    
    @FXML
    private Button btnAdicionarTreino;
    
    @FXML
    private Button btnLogout;
    
    private Aluno alunoAtual;
    private ObservableList<Treino> treinos;
    private boolean isTestMode = false;
    private String emailUsuario;

    protected void setTestMode(boolean testMode) {
        this.isTestMode = testMode;
    }

    protected void initializeTestMode() {
        if (treinos == null) {
            treinos = FXCollections.observableArrayList();
        }
    }

    @FXML
    public void initialize() {
        if (isTestMode) {
            treinos = FXCollections.observableArrayList();
            return;
        }

        alunoAtual = LoginController.getUsuarioLogado();
        emailUsuario = LoginController.getEmailUsuario();
        
        if (alunoAtual != null) {
            saudacaoText.setText("Bem-vindo de volta!");
            nomeUsuarioText.setText(alunoAtual.getNome());
            
            treinos = FXCollections.observableArrayList(TreinoRepository.carregarTreinos(emailUsuario));
            alunoAtual.setTreinos(new ArrayList<>(treinos));
            
            configurarColunas();
            tabelaTreinos.setItems(treinos);
            
            // Configurar binding dos pontos
            pontosText.textProperty().bind(alunoAtual.pontosProperty().asString("%d pts"));
            
            if (btnLogout != null) {
                btnLogout.setId("btnLogout");
            }
        }
    }
    
    private void configurarColunas() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        
        colunaExercicios.setCellValueFactory(cellData -> {
            Treino treino = cellData.getValue();
            String exercicios = treino.getExercicios().stream()
                .map(Treino.Exercicio::toString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("Nenhum exercício");
            return new SimpleStringProperty(exercicios);
        });
        
        colunaPontos.setCellValueFactory(new PropertyValueFactory<>("pontuacao"));
        
        colunaStatus.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isConcluido() ? "Concluído ✅" : "Pendente ⏳")
        );
        
        configurarColunaAcoes();
        
        tabelaTreinos.setItems(treinos);
    }
    
    private void configurarColunaAcoes() {
        colunaAcoes.setCellFactory(col -> {
            TableCell<Treino, Void> cell = new TableCell<>() {
                private final HBox acoes = new HBox(10);
                
                private final Button btnEditar = createActionButton("✏️", "Editar treino", "#310667");
                private final Button btnConcluir = createActionButton("✅", "Marcar como concluído", "#28a745");
                private final Button btnExcluir = createActionButton("🗑️", "Excluir treino", "#dc3545");
                
                {
                    acoes.setAlignment(javafx.geometry.Pos.CENTER);
                    acoes.getChildren().addAll(btnEditar, btnConcluir, btnExcluir);
                    
                    btnEditar.setOnAction(event -> {
                        if (getTableRow() != null && getTableRow().getItem() != null) {
                            editarExercicios(getTableRow().getItem());
                        }
                    });
                    
                    btnConcluir.setOnAction(event -> {
                        if (getTableRow() != null && getTableRow().getItem() != null) {
                            Treino treino = getTableRow().getItem();
                            boolean novoStatus = !treino.isConcluido();
                            
                            // Atualizar pontos do aluno primeiro
                            if (novoStatus) {
                                alunoAtual.concluirTreino(treino);
                            } else {
                                alunoAtual.desconcluirTreino(treino);
                            }

                            // Depois atualizar o estado do treino
                            treino.setConcluido(novoStatus);
                            
                            // Salvar alterações
                            TreinoRepository.salvarTreinos(emailUsuario, new ArrayList<>(treinos));
                            
                            // Atualizar estado global
                            LoginController.setUsuarioLogado(alunoAtual);
                            
                            // Atualizar UI
                            btnConcluir.setText(novoStatus ? "❌" : "✅");
                            btnConcluir.setTooltip(new Tooltip(novoStatus ? "Desmarcar como concluído" : "Marcar como concluído"));
                            
                            // Forçar atualização da tabela
                            Platform.runLater(() -> {
                                tabelaTreinos.refresh();
                            });
                        }
                    });
                    
                    btnExcluir.setOnAction(event -> {
                        if (getTableRow() != null && getTableRow().getItem() != null) {
                            Treino treino = getTableRow().getItem();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmar Exclusão");
                            alert.setHeaderText("Excluir Treino");
                            alert.setContentText("Tem certeza que deseja excluir este treino?");
                            
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                // Primeiro remover pontos se necessário
                                if (treino.isConcluido()) {
                                    alunoAtual.desconcluirTreino(treino);
                                }
                                
                                // Remover treino da lista e do aluno
                                treinos.remove(treino);
                                alunoAtual.removerTreino(treino);
                                
                                // Salvar alterações
                                TreinoRepository.salvarTreinos(emailUsuario, new ArrayList<>(treinos));
                                
                                // Atualizar estado global
                                LoginController.setUsuarioLogado(alunoAtual);
                                
                                // Forçar atualização da tabela e pontos
                                tabelaTreinos.refresh();
                            }
                        }
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(acoes);
                        if (getTableRow() != null && getTableRow().getItem() != null) {
                            Treino treino = getTableRow().getItem();
                            btnConcluir.setText(treino.isConcluido() ? "❌" : "✅");
                            btnConcluir.setTooltip(new Tooltip(treino.isConcluido() ? "Desmarcar como concluído" : "Marcar como concluído"));
                        }
                    }
                }
            };
            return cell;
        });
    }
    
    private Button createActionButton(String icon, String tooltip, String color) {
        Button button = new Button();
        button.setGraphic(new Text(icon));
        button.setTooltip(new Tooltip(tooltip));
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-font-size: 16;" +
            "-fx-cursor: hand;" +
            String.format("-fx-text-fill: %s;", color)
        );
        return button;
    }
    
    @FXML
    private void navegarInicio() {
        atualizarTreinos();
    }
    
    @FXML
    private void navegarTreinos() {
        atualizarTreinos();
    }
    
    private void atualizarTreinos() {
        treinos = FXCollections.observableArrayList(TreinoRepository.carregarTreinos(emailUsuario));
        alunoAtual.setTreinos(new ArrayList<>(treinos));
        tabelaTreinos.setItems(treinos);
        tabelaTreinos.refresh();
    }
    
    @FXML
    private void navegarCalendario() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/Calendario.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) tabelaTreinos.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void adicionarTreino() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Novo Treino");
        dialog.setHeaderText("Adicionar Novo Treino");
        
        // Configurar tamanho mínimo
        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(200);
        
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20;");
        
        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do treino");
        nomeField.setPrefWidth(300);
        
        content.getChildren().addAll(
            new Label("Nome do treino:"),
            nomeField
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        nomeField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return nomeField.getText().trim();
            }
            return null;
        });
        
        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(nomeTreino -> {
            if (!nomeTreino.isEmpty()) {
                Treino novoTreino = new Treino(nomeTreino);
                alunoAtual.adicionarTreino(novoTreino);
                treinos.add(novoTreino);
                TreinoRepository.salvarTreinos(emailUsuario, new ArrayList<>(treinos));
            }
        });
    }
    
    private void adicionarExercicio(Treino treino) {
        Dialog<Treino.Exercicio> dialog = new Dialog<>();
        dialog.setTitle("Novo Exercício");
        dialog.setHeaderText("Adicionar Exercício ao Treino: " + treino.getNome());
        
        // Configurar tamanho mínimo
        dialog.getDialogPane().setMinWidth(500);
        dialog.getDialogPane().setMinHeight(300);
        
        VBox content = new VBox(15);
        content.setStyle("-fx-padding: 20;");
        
        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do exercício");
        nomeField.setPrefWidth(300);
        
        Spinner<Integer> seriesSpinner = new Spinner<>(1, 10, 3);
        seriesSpinner.setEditable(true);
        seriesSpinner.setPrefWidth(100);
        
        Spinner<Integer> repeticoesSpinner = new Spinner<>(1, 50, 12);
        repeticoesSpinner.setEditable(true);
        repeticoesSpinner.setPrefWidth(100);
        
        content.getChildren().addAll(
            new Label("Nome do exercício:"), nomeField,
            new Label("Séries:"), seriesSpinner,
            new Label("Repetições:"), repeticoesSpinner
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        nomeField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return new Treino.Exercicio(
                    nomeField.getText(),
                    seriesSpinner.getValue(),
                    repeticoesSpinner.getValue()
                );
            }
            return null;
        });
        
        Optional<Treino.Exercicio> resultado = dialog.showAndWait();
        resultado.ifPresent(exercicio -> {
            treino.adicionarExercicio(
                exercicio.getNome(),
                exercicio.getSeries(),
                exercicio.getRepeticoes()
            );
            TreinoRepository.salvarTreinos(emailUsuario, new ArrayList<>(treinos));
            tabelaTreinos.refresh();
        });
    }
    
    private void editarExercicios(Treino treino) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Editar Exercícios");
        dialog.setHeaderText("Editar Exercícios do Treino: " + treino.getNome());
        
        // Configurar tamanho mínimo
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(600);
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 30; -fx-background-color: white;");
        List<HBox> exerciciosBoxes = new ArrayList<>();
        
        // Seção do nome do treino
        VBox headerSection = new VBox(10);
        headerSection.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-background-radius: 10;");
        
        Label titleLabel = new Label("Nome do Treino");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #310667;");
        
        TextField nomeTreinoField = new TextField(treino.getNome());
        nomeTreinoField.setPrefWidth(400);
        nomeTreinoField.setStyle("-fx-font-size: 14; -fx-padding: 10;");
        
        headerSection.getChildren().addAll(titleLabel, nomeTreinoField);
        content.getChildren().add(headerSection);
        
        // Separador com título "Exercícios"
        Label exerciciosLabel = new Label("Exercícios");
        exerciciosLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #310667; -fx-padding: 10 0;");
        content.getChildren().add(exerciciosLabel);
        
        // Container para os exercícios
        VBox exerciciosContainer = new VBox(15);
        exerciciosContainer.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-background-radius: 10;");
        
        for (Treino.Exercicio exercicio : treino.getExercicios()) {
            HBox exercicioBox = criarExercicioBox(exercicio, exerciciosBoxes, exerciciosContainer);
            exerciciosBoxes.add(exercicioBox);
            exerciciosContainer.getChildren().add(exercicioBox);
        }
        
        content.getChildren().add(exerciciosContainer);
        
        // Botão de adicionar exercício
        Button btnAdicionar = new Button("Adicionar Novo Exercício");
        btnAdicionar.setStyle(
            "-fx-background-color: #310667;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        );
        btnAdicionar.setGraphic(new Text("➕"));
        btnAdicionar.setOnAction(e -> {
            HBox exercicioBox = criarExercicioBox(null, exerciciosBoxes, exerciciosContainer);
            exerciciosBoxes.add(exercicioBox);
            exerciciosContainer.getChildren().add(exercicioBox);
        });
        
        // Container para o botão adicionar
        HBox addButtonContainer = new HBox(btnAdicionar);
        addButtonContainer.setAlignment(javafx.geometry.Pos.CENTER);
        addButtonContainer.setPadding(new javafx.geometry.Insets(20, 0, 0, 0));
        content.getChildren().add(addButtonContainer);
        
        scrollPane.setContent(content);
        dialog.getDialogPane().setContent(scrollPane);
        
        // Estilizar botões do diálogo
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        
        okButton.setStyle(
            "-fx-background-color: #310667;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 20;"
        );
        
        cancelButton.setStyle(
            "-fx-background-color: white;" +
            "-fx-text-fill: #310667;" +
            "-fx-font-size: 14;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: #310667;" +
            "-fx-border-radius: 20;"
        );
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                treino.setNome(nomeTreinoField.getText().trim());
                treino.getExercicios().clear();
                
                for (HBox exercicioBox : exerciciosBoxes) {
                    VBox nomeContainer = (VBox) exercicioBox.getChildren().get(0);
                    VBox seriesContainer = (VBox) exercicioBox.getChildren().get(1);
                    VBox repeticoesContainer = (VBox) exercicioBox.getChildren().get(2);
                    
                    TextField nomeField = (TextField) nomeContainer.getChildren().get(1);
                    Spinner<Integer> seriesSpinner = (Spinner<Integer>) seriesContainer.getChildren().get(1);
                    Spinner<Integer> repeticoesSpinner = (Spinner<Integer>) repeticoesContainer.getChildren().get(1);
                    
                    if (!nomeField.getText().trim().isEmpty()) {
                        treino.adicionarExercicio(
                            nomeField.getText().trim(),
                            seriesSpinner.getValue(),
                            repeticoesSpinner.getValue()
                        );
                    }
                }
                
                TreinoRepository.salvarTreinos(emailUsuario, new ArrayList<>(treinos));
                tabelaTreinos.refresh();
            }
            return null;
        });
        
        dialog.showAndWait();
    }
    
    private HBox criarExercicioBox(Treino.Exercicio exercicio, List<HBox> exerciciosBoxes, VBox container) {
        // Container principal do exercício
        HBox exercicioBox = new HBox(15);
        exercicioBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        exercicioBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 15;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #dee2e6;" +
            "-fx-border-radius: 10;"
        );
        
        // Campo nome do exercício
        VBox nomeContainer = new VBox(5);
        Label nomeLabel = new Label("Nome do Exercício");
        nomeLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12;");
        TextField nomeField = new TextField(exercicio != null ? exercicio.getNome() : "");
        nomeField.setPrefWidth(300);
        nomeField.setPromptText("Digite o nome do exercício");
        nomeField.setStyle(
            "-fx-padding: 8;" +
            "-fx-font-size: 14;" +
            "-fx-background-radius: 5;"
        );
        nomeContainer.getChildren().addAll(nomeLabel, nomeField);
        
        // Campo séries
        VBox seriesContainer = new VBox(5);
        Label seriesLabel = new Label("Séries");
        seriesLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12;");
        Spinner<Integer> seriesSpinner = new Spinner<>(1, 10, exercicio != null ? exercicio.getSeries() : 3);
        seriesSpinner.setEditable(true);
        seriesSpinner.setPrefWidth(100);
        seriesSpinner.setStyle("-fx-font-size: 14;");
        seriesContainer.getChildren().addAll(seriesLabel, seriesSpinner);
        
        // Campo repetições
        VBox repeticoesContainer = new VBox(5);
        Label repeticoesLabel = new Label("Repetições");
        repeticoesLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12;");
        Spinner<Integer> repeticoesSpinner = new Spinner<>(1, 50, exercicio != null ? exercicio.getRepeticoes() : 12);
        repeticoesSpinner.setEditable(true);
        repeticoesSpinner.setPrefWidth(100);
        repeticoesSpinner.setStyle("-fx-font-size: 14;");
        repeticoesContainer.getChildren().addAll(repeticoesLabel, repeticoesSpinner);
        
        // Botão remover
        Button btnRemover = new Button();
        btnRemover.setGraphic(new Text("🗑️"));
        btnRemover.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #dc3545;" +
            "-fx-font-size: 16;" +
            "-fx-cursor: hand;"
        );
        btnRemover.setOnAction(e -> {
            container.getChildren().remove(exercicioBox);
            exerciciosBoxes.remove(exercicioBox);
        });
        
        // Adicionar todos os elementos ao container principal
        exercicioBox.getChildren().addAll(nomeContainer, seriesContainer, repeticoesContainer, btnRemover);
        
        return exercicioBox;
    }
    
    private void editarTreino(Treino treino) {
        TextInputDialog dialog = new TextInputDialog(treino.getNome());
        dialog.setTitle("Editar Treino");
        dialog.setHeaderText("Editar Nome do Treino");
        dialog.setContentText("Novo nome:");
        
        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(novoNome -> {
            if (!novoNome.trim().isEmpty()) {
                treino.setNome(novoNome);
                TreinoRepository.salvarTreinos(emailUsuario, new ArrayList<>(treinos));
                tabelaTreinos.refresh();
            }
        });
    }
    
    @FXML
    private void abrirContatoInstrutor() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ContatoInstrutor.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) tabelaTreinos.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fazerLogout() {
        try {
            LoginController.setUsuarioLogado(null);
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Main.class.getResource("/style/style.css").toExternalForm());
            Stage stage = (Stage) tabelaTreinos.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Aluno getAlunoAtual() {
        return alunoAtual;
    }
} 