package dragonix.fierydragons1;

import dragonix.fierydragons1.handlers.LoadingHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

/**
 * The MainMenuController class handles the actions performed in the main menu of the game.
 * It allows the user to start a new game, load an existing game, toggle debug and custom modes,
 * start the tutorial, and exit the game.
 */
public class MainMenuController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean debugMode = false; // Track debug mode state
    private boolean customMode = false;

    /**
     * Starts a new game.
     *
     * @param e The action event triggered by the user.
     * @throws IOException If an I/O error occurs during loading the main application scene.
     */
    public void startNewGame(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-app.fxml"));
        root = loader.load();
        MainAppController appController = loader.getController();
        appController.setDebugMode(debugMode);

        // Initialize new game data here
        appController.setupPlayersAndGame(null);

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        System.out.println("Changing to Game");
    }

    /**
     * Loads a previously saved game.
     *
     * @param e The action event triggered by the user.
     * @throws IOException If an I/O error occurs during loading the main application scene.
     */
    public void loadGame(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-app.fxml"));
        root = loader.load();
        MainAppController appController = loader.getController();
        appController.setDebugMode(debugMode);
        appController.setCustomGame(customMode);

        // Initialize new game data here

        LoadingHandler loadingHandler = new LoadingHandler("saves");
        Map<String, Object> gameData = loadingHandler.loadGameFromFile();

        boolean validGame = appController.setupPlayersAndGame(gameData);
        if(!validGame){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Save Data");
            alert.setHeaderText("Invalid Save Data"); // No header text
            alert.setContentText("Save Data is invalid, please check the format. E.g Invalid Number of Dragon/Volcano Cards");
            alert.showAndWait();
        }

        if(gameData != null && validGame){
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
            System.out.println("Loading Game");
        } else{
            System.out.println("No game data loaded");
        }
    }

    /**
     * Toggles the debug mode state and starts a new game.
     *
     * @param e The action event triggered by the user.
     * @throws IOException If an I/O error occurs during loading the main application scene.
     */
    public void toggleDebugMode(ActionEvent e) throws IOException {
        debugMode = !debugMode;
        System.out.println("Debug Mode: " + (debugMode ? "Enabled" : "Disabled"));
        startNewGame(e);
    }

    /**
     * Toggles the custom mode state and loads a game.
     *
     * @param e The action event triggered by the user.
     * @throws IOException If an I/O error occurs during loading the main application scene.
     */
    public void toggleCustomMode(ActionEvent e) throws IOException {
        customMode = !customMode;
        loadGame(e);
    }

    /**
     * Exits the game.
     *
     * @param e The action event triggered by the user.
     */
    public void exitGame(ActionEvent e) {
        System.out.println("Game exited!");
        Platform.exit();
    }

    /**
     * Starts the tutorial.
     *
     * @param e The action event triggered by the user.
     * @throws IOException If an I/O error occurs during loading the tutorial scene.
     */
    public void startTutorial(ActionEvent e) throws IOException {
        // Load the tutorial scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tutorial.fxml"));
        root = loader.load();

        // Switch to the tutorial scene
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Changing to Tutorial");
    }

}
