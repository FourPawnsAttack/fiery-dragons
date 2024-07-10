package dragonix.fierydragons1;

/**
 * The MainApp class serves as the entry point for launching the application.
 * It contains the main method that initializes the application and launches the GUI.
 */

public class MainApp{

    /**
     * The main method is the entry point of the application.
     * it launches the GUI.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        GUI.launch(GUI.class, args);
    }

}
