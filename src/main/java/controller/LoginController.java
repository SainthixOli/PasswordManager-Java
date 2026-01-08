package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.List;

import model.Usuario;
import login.Login;
import storage.StorageService;
import main.Main;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private StorageService storageService;
    private Login loginService;
    private List<Usuario> usuariosDoSistema;

    public void initialize() {
        System.out.println("LOGIN_CONTROLLER: initialize() executing.");
        storageService = new StorageService();
        loginService = new Login();
        try {
            System.out.println("LOGIN_CONTROLLER: Loading users from storage...");
            usuariosDoSistema = storageService.carregarUsuarios();
            System.out.println("LOGIN_CONTROLLER: Users loaded. Count: "
                    + (usuariosDoSistema != null ? usuariosDoSistema.size() : "null"));
        } catch (IOException e) {
            System.err.println("LOGIN_CONTROLLER: Failed to load users:");
            e.printStackTrace();
            statusLabel.setText("Erro ao carregar usuários: " + e.getMessage());
            usuariosDoSistema = new java.util.ArrayList<>();
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Por favor, preencha todos os campos.");
            return;
        }

        Usuario usuarioLogado = loginService.autenticar(username, password, usuariosDoSistema);

        if (usuarioLogado != null) {
            // Derivar a chave de sessão usando a senha mestra e o sal armazenado
            try {
                String salBase64 = usuarioLogado.getSenhaDaConta().getSalBase64();
                byte[] sal = java.util.Base64.getDecoder().decode(salBase64);
                javax.crypto.SecretKey chave = util.Criptografia.derivarChave(password, sal);
                usuarioLogado.setChaveSessao(chave);
            } catch (Exception e) {
                statusLabel.setText("Erro de segurança: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            // Sucesso! Abrir a tela principal
            abrirTelaPrincipal(usuarioLogado);
        } else {
            statusLabel.setText("Usuário ou senha inválidos.");
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Para criar conta, preencha usuário e senha.");
            return;
        }

        boolean usuarioJaExiste = usuariosDoSistema.stream()
                .anyMatch(u -> u.getNomeDeUsuario().equalsIgnoreCase(username));

        if (usuarioJaExiste) {
            statusLabel.setText("Usuário já existe.");
            return;
        }

        Usuario novoUsuario = new Usuario(username, password);
        usuariosDoSistema.add(novoUsuario);

        try {
            storageService.salvarUsuarios(usuariosDoSistema);
            statusLabel.setText("Conta criada! Clique em Entrar.");
            statusLabel.setStyle("-fx-text-fill: #a6e3a1;"); // Green
        } catch (IOException e) {
            statusLabel.setText("Erro ao salvar conta: " + e.getMessage());
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

    private void abrirTelaPrincipal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
            Parent root = loader.load();

            // Passar o usuário para o MainController
            MainController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Password Manager - " + usuario.getNomeDeUsuario());
            stage.centerOnScreen();
        } catch (IOException e) {
            statusLabel.setText("Erro ao carregar tela principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
