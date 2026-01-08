package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import model.Usuario;
import model.Senha;
import service.Generate;
import storage.StorageService;

public class VaultController {

    @FXML
    private javafx.scene.control.TextField searchField;

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
    private ObservableList<Senha> masterData = FXCollections.observableArrayList();

    public void initialize() {
        storageService = new StorageService();
        geradorDeSenha = new Generate();

        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("nomeServico"));
        // Show masked passwords by default
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty("••••••••"));

        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDataCriacao() != null) {
                return new SimpleStringProperty(
                        cellData.getValue().getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            return new SimpleStringProperty("");
        });

        // Add Listener for Search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList(newValue);
        });
    }

    // Called by MainController to pass the user
    public void initData(Usuario usuario) {
        this.usuarioLogado = usuario;
        if (usuarioLogado != null) {
            masterData.setAll(usuarioLogado.getSenhasDeServicos());
            passwordsTable.setItems(masterData);
        }
    }

    private void filterList(String query) {
        if (query == null || query.isEmpty()) {
            passwordsTable.setItems(masterData);
        } else {
            ObservableList<Senha> filtered = FXCollections.observableArrayList();
            for (Senha s : masterData) {
                if (s.getNomeServico().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(s);
                }
            }
            passwordsTable.setItems(filtered);
        }
    }

    private void atualizarTabela() {
        if (usuarioLogado != null) {
            masterData.setAll(usuarioLogado.getSenhasDeServicos());
            filterList(searchField.getText()); // Re-apply filter if any
        }
    }

    @FXML
    private void handleAddPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nova Senha");
        dialog.setHeaderText("Gerar nova senha segura");
        dialog.setContentText("Nome do Serviço:");

        // Style the dialog manually or via CSS if possible, but standard alerts are
        // fine for now

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nomeServico -> {
            if (nomeServico.trim().isEmpty())
                return;

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
            String valorParaCopiar;
            String mensagem;
            String titulo;

            try {
                if (usuarioLogado.getChaveSessao() == null) {
                    mostrarAlerta("Erro", "Sessão inválida. Faça login novamente.");
                    return;
                }
                valorParaCopiar = util.Criptografia.decifrar(selecionada.getValor(), usuarioLogado.getChaveSessao());
                mensagem = "Senha descriptografada copiada com sucesso!";
                titulo = "Sucesso";
            } catch (Exception e) {
                System.err.println("VAULT: Decryption failed (New Key). Trying Legacy...");
                try {
                    // Try Legacy Decryption
                    valorParaCopiar = util.Criptografia.decifrarLegado(selecionada.getValor());
                    mensagem = "⚠️ Senha recuperada do formato antigo. \nEla foi atualizada para o novo formato seguro.";
                    titulo = "Recuperação de Sucesso";

                    // Auto-migrate: Re-encrypt with new key and update memory
                    String novoEnc = util.Criptografia.cifrar(valorParaCopiar, usuarioLogado.getChaveSessao());
                    selecionada.setValor(novoEnc);
                    salvarDados(); // Persist changes to disk

                    System.out.println(
                            "VAULT: Password for " + selecionada.getNomeServico() + " migrated to new format.");

                } catch (Exception ex) {
                    // Fallback: Copy raw encrypted value
                    valorParaCopiar = selecionada.getValor();
                    mensagem = "⚠️ Falha total na decifragem. \nO valor CRIPTOGRAFADO foi copiado.";
                    titulo = "Falha de Decifragem";
                    System.err.println("VAULT: Legacy Decryption also failed: " + ex.getMessage());
                }
            }

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(valorParaCopiar);
            clipboard.setContent(content);
            mostrarAlerta(titulo, mensagem);
        } else {
            mostrarAlerta("Aviso", "Selecione uma senha na tabela.");
        }
    }

    @FXML
    private void handleEditPassword() {
        Senha selecionada = passwordsTable.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Editar Senha");
            dialog.setHeaderText("Redefinir senha para: " + selecionada.getNomeServico());
            dialog.setContentText("Nova Senha (deixe em branco para cancelar):");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(novaSenhaTexto -> {
                if (novaSenhaTexto.trim().isEmpty())
                    return;

                try {
                    if (usuarioLogado.getChaveSessao() == null) {
                        mostrarAlerta("Erro", "Sessão inválida. Faça login novamente.");
                        return;
                    }
                    // Encrypt new password
                    String novoEnc = util.Criptografia.cifrar(novaSenhaTexto, usuarioLogado.getChaveSessao());
                    selecionada.setValor(novoEnc); // Update object
                    salvarDados(); // Persist
                    atualizarTabela();
                    mostrarAlerta("Sucesso", "Senha atualizada com sucesso!");
                } catch (Exception e) {
                    mostrarAlerta("Erro", "Falha ao criptografar nova senha: " + e.getMessage());
                }
            });
        } else {
            mostrarAlerta("Aviso", "Selecione uma senha para editar.");
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

    private void salvarDados() {
        try {
            java.util.List<Usuario> todosUsuarios = storageService.carregarUsuarios();
            boolean found = false;
            for (int i = 0; i < todosUsuarios.size(); i++) {
                if (todosUsuarios.get(i).getNomeDeUsuario().equals(usuarioLogado.getNomeDeUsuario())) {
                    todosUsuarios.set(i, usuarioLogado);
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Should not happen, but safe fallback
                todosUsuarios.add(usuarioLogado);
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
        alert.show();
    }
}
