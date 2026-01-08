package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Usuario;
import model.Senha;

public class DashboardController {

    @FXML
    private Label scoreLabel;
    @FXML
    private Label totalPasswordsLabel;
    @FXML
    private Label weakPasswordsLabel;
    @FXML
    private Label reusedPasswordsLabel;

    private Usuario usuarioLogado;

    public void initData(Usuario usuario) {
        this.usuarioLogado = usuario;
        calcularEstatisticas();
    }

    private void calcularEstatisticas() {
        if (usuarioLogado == null)
            return;

        int total = usuarioLogado.getSenhasDeServicos().size();
        int weaks = 0;
        int reused = 0; // Simplified logic for demo

        // Simple mock logic for "Weak" passwords (e.g. length < 8) - since we can't
        // easily decrypt without key here unless we pass it,
        // wait, we DO pass the user which has the key. BUT, the passwords in memory are
        // encrypted strings in 'Senha' object?

        // Checking 'Senha' model: getValor() returns encrypted string.
        // We cannot check strength without decrypting.
        // For performance/security, let's just count total for now, or decrypt if we
        // have the session key.

        boolean canDecrypt = (usuarioLogado.getChaveSessao() != null);
        java.util.Set<String> uniquePasswords = new java.util.HashSet<>();

        for (Senha s : usuarioLogado.getSenhasDeServicos()) {
            if (canDecrypt) {
                try {
                    String decrypted = util.Criptografia.decifrar(s.getValor(), usuarioLogado.getChaveSessao());
                    if (decrypted.length() < 8) {
                        weaks++;
                    }
                    if (!uniquePasswords.add(decrypted)) {
                        reused++;
                    }
                } catch (Exception e) {
                    // Ignore decryption errors for stats - key may be invalid or changed
                    System.err.println("DASHBOARD: Failed to decrypt password for stats: " + s.getNomeServico());
                }
            }
        }

        // Score Calculation (Mock Logic)
        int score = 100;
        if (total > 0) {
            score -= (weaks * 10);
            score -= (reused * 15);
        } else {
            score = 0; // No passwords = 0 score
        }
        if (score < 0)
            score = 0;

        totalPasswordsLabel.setText(String.valueOf(total));
        weakPasswordsLabel.setText(String.valueOf(weaks));
        reusedPasswordsLabel.setText(String.valueOf(reused));
        scoreLabel.setText(score + "%");

        if (score > 80)
            scoreLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: #a6e3a1;"); // Green
        else if (score > 50)
            scoreLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: #fab387;"); // Orange
        else
            scoreLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: #f38ba8;"); // Red
    }
}
