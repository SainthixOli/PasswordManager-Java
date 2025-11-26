package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import model.Usuario;
import model.Senha;
import service.Generate;
import storage.StorageService;

public class MainController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Senha> passwordsTable;

    @FXML
    private TableColumn<Senha, String> serviceColumn;

    @FXML
    private TableColumn<Senha, String> passwordColumn;

    @FXML
    private TableColumn<Senha, String> dateColumn;

    private Usuario usuarioLogado;
    private StorageService storageService;
    private Generate geradorDeSenha;

    public void initialize() {
        storageService = new StorageService();
        geradorDeSenha = new Generate();

        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("nomeServico"));

        // Mostrar senha mascarada ou real? Vamos mostrar mascarada por padrão
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty("••••••••"));

        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDataCriacao() != null) {
                return new SimpleStringProperty(
                        cellData.getValue().getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            return new SimpleStringProperty("");
        });
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
        welcomeLabel.setText("Bem-vindo, " + usuario.getNomeDeUsuario());
        atualizarTabela();
    }

    private void atualizarTabela() {
        if (usuarioLogado != null) {
            ObservableList<Senha> senhas = FXCollections.observableArrayList(usuarioLogado.getSenhasDeServicos());
            passwordsTable.setItems(senhas);
        }
    }

    @FXML
    private void handleAddPassword() {
        // Diálogo simples para nome do serviço
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nova Senha");
        dialog.setHeaderText("Gerar nova senha segura");
        dialog.setContentText("Nome do Serviço:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nomeServico -> {
            if (nomeServico.trim().isEmpty())
                return;

            // Gera senha de 16 caracteres por padrão
            Senha novaSenha = usuarioLogado.gerarNovoPinParaServico(nomeServico, 16, geradorDeSenha);

            if (novaSenha != null) {
                salvarDados();
                atualizarTabela();
                mostrarAlerta("Sucesso", "Senha gerada para " + nomeServico);
            } else {
                mostrarAlerta("Erro", "Falha ao gerar senha.");
            }
        });
    }

    @FXML
    private void handleCopyPassword() {
        Senha selecionada = passwordsTable.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                if (usuarioLogado.getChaveSessao() == null) {
                    mostrarAlerta("Erro", "Sessão inválida. Faça login novamente.");
                    return;
                }
                String senhaDecifrada = util.Criptografia.decifrar(selecionada.getValor(),
                        usuarioLogado.getChaveSessao());

                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(senhaDecifrada);
                clipboard.setContent(content);
                mostrarAlerta("Copiado", "Senha copiada para a área de transferência!");
            } catch (Exception e) {
                mostrarAlerta("Erro", "Falha ao descriptografar senha: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Aviso", "Selecione uma senha na tabela.");
        }
    }

    @FXML
    private void handleDeletePassword() {
        Senha selecionada = passwordsTable.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Deleção");
            alert.setHeaderText("Deletar senha de " + selecionada.getNomeServico() + "?");
            alert.setContentText("Esta ação não pode ser desfeita.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                usuarioLogado.deletarSenhaDeServico(selecionada.getNomeServico());
                salvarDados();
                atualizarTabela();
            }
        } else {
            mostrarAlerta("Aviso", "Selecione uma senha na tabela.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 350)); // Tamanho menor para login
            stage.setTitle("Password Manager - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenGitHub() {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://github.com/SainthixOli"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void salvarDados() {
        try {
            // Precisamos carregar todos os usuários, atualizar o atual e salvar tudo
            // Isso é ineficiente, mas mantém a lógica original do StorageService
            // O ideal seria o StorageService gerenciar isso melhor.
            // Como o usuarioLogado é uma referência ao objeto na lista carregada no
            // LoginController (se passarmos certo),
            // precisamos garantir que estamos salvando a lista completa.

            // HACK: Para simplificar, vamos recarregar, substituir e salvar.
            // Ou melhor, vamos assumir que o LoginController passou a referência correta e
            // o StorageService tem um método para salvar a lista.
            // Mas o MainController não tem a lista completa de usuários.
            // Vamos mudar o setUsuario para receber a lista completa também ou recarregar.

            // Solução rápida: Recarregar tudo, achar o usuário, atualizar e salvar.
            java.util.List<Usuario> todosUsuarios = storageService.carregarUsuarios();
            for (int i = 0; i < todosUsuarios.size(); i++) {
                if (todosUsuarios.get(i).getNomeDeUsuario().equals(usuarioLogado.getNomeDeUsuario())) {
                    todosUsuarios.set(i, usuarioLogado);
                    break;
                }
            }
            storageService.salvarUsuarios(todosUsuarios);

        } catch (IOException e) {
            mostrarAlerta("Erro Crítico", "Falha ao salvar dados: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
