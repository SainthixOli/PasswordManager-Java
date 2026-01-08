package main;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("LAUNCHER: Starting application via Launcher...");
        System.out.println("LAUNCHER: Invoking Main.main()...");
        Main.main(args);
        System.out.println("LAUNCHER: Main.main() returned (unexpected if JavaFX starts).");
    }
}
