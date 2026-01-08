package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

import model.Usuario;

public class MainController {

    @FXML
    private Label userLabel;

    @FXML
    private StackPane contentArea;

    private Usuario usuarioLogado;

    public void initialize() {
        // Anything needed on startup
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioLogado = usuario;
        if (usuarioLogado != null) {
            userLabel.setText(usuario.getNomeDeUsuario());
            showDashboard(); // Default View
        }
    }

    @FXML
    private void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardView.fxml"));
            Parent view = loader.load();

            DashboardController controller = loader.getController();
            controller.initData(usuarioLogado);

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showVault() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VaultView.fxml"));
            Parent view = loader.load();

            VaultController controller = loader.getController();
            controller.initData(usuarioLogado);

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showGenerator() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GeneratorView.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 350));
            stage.setTitle("Password Manager - Login");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
