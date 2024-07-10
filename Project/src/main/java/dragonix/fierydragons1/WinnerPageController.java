package dragonix.fierydragons1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The WinnerPageController class manages the winner page in the game, providing functionality
 * to set the winner, navigate back to the main menu, and start a new game.
 */
public class WinnerPageController {
    @FXML
    private Label winnerLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean debugMode = false;  // Track debug mode state


    /**
     * Sets the winner's name to be displayed on the winner page.
     *
     * @param winnerName the name of the winner
     */
    public void setWinner(String winnerName) {
        System.out.println("Setting winner: " + winnerName);
        winnerLabel.setText(winnerName);
    }

    /**
     * Navigates back to the main menu by loading the main menu FXML.
     */
    public void backToMainMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/dragonix/fierydragons1/main-menu.fxml"));
            Stage stage = (Stage) winnerLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a new game by loading the game FXML.
     */
    @FXML
    public void playAgain() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/dragonix/fierydragons1/game.fxml"));
            Stage stage = (Stage) winnerLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the game, initializing the main application controller and setting the debug mode.
     *
     * @param e the action event that triggered the method
     * @throws IOException if the main application FXML cannot be loaded
     */
    public void startGame(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-app.fxml"));
        root = loader.load();
        MainAppController appController = loader.getController();
        appController.setDebugMode(debugMode);

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        System.out.println("Changing to Game");
    }
}
