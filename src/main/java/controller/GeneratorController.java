package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.security.SecureRandom;

public class GeneratorController {

    @FXML
    private TextField passwordDisplay;
    @FXML
    private Slider lengthSlider;
    @FXML
    private Label lengthLabel;
    @FXML
    private CheckBox upperCaseBox;
    @FXML
    private CheckBox lowerCaseBox;
    @FXML
    private CheckBox numbersBox;
    @FXML
    private CheckBox specialsBox;
    @FXML
    private Label strengthLabel;

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    private SecureRandom random = new SecureRandom();

    public void initialize() {
        lengthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            lengthLabel.setText(String.valueOf(newVal.intValue()));
            handleGenerate();
        });

        upperCaseBox.selectedProperty().addListener(e -> handleGenerate());
        numbersBox.selectedProperty().addListener(e -> handleGenerate());
        specialsBox.selectedProperty().addListener(e -> handleGenerate());

        handleGenerate(); // Initial generate
    }

    @FXML
    private void handleGenerate() {
        int length = (int) lengthSlider.getValue();
        StringBuilder charPool = new StringBuilder(LOWER); // Always include lowercase
        if (upperCaseBox.isSelected())
            charPool.append(UPPER);
        if (numbersBox.isSelected())
            charPool.append(DIGITS);
        if (specialsBox.isSelected())
            charPool.append(SPECIAL);

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(charPool.charAt(random.nextInt(charPool.length())));
        }

        passwordDisplay.setText(password.toString());
        updateStrength(length);
    }

    private void updateStrength(int length) {
        if (length < 12) {
            strengthLabel.setText("Força: Fraca");
            strengthLabel.setStyle("-fx-text-fill: #f38ba8;");
        } else if (length < 16) {
            strengthLabel.setText("Força: Média");
            strengthLabel.setStyle("-fx-text-fill: #fab387;");
        } else {
            strengthLabel.setText("Força: INQUEBRÁVEL BRUTAL 💀");
            strengthLabel.setStyle("-fx-text-fill: #a6e3a1; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void handleCopy() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(passwordDisplay.getText());
        clipboard.setContent(content);
        strengthLabel.setText("Copiado com sucesso!");
    }
}
