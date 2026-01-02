package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("MAIN: start() called. Loading LoginView.fxml...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();
            System.out.println("MAIN: FXML loaded successfully.");

            primaryStage.setTitle("Password Manager - Login");
            primaryStage.setScene(new Scene(root, 400, 350));
            primaryStage.setResizable(false);
            primaryStage.show();
            System.out.println("MAIN: Stage shown.");
        } catch (Exception e) {
            System.err.println("MAIN: FATAL ERROR during start():");
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        System.out.println("MAIN: main() called. Launching JavaFX application...");
        launch(args);
        System.out.println("MAIN: JavaFX application launched.");
    }
}
