package dragonix.fierydragons1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The GUI class is responsible for launching the graphical user interface (GUI) of the application.
 * It extends the JavaFX Application class and overrides the start method to initialize the primary stage
 * and load the main menu FXML file.
 */
public class GUI extends Application {

    /**
     * The start method initializes the primary stage of the application and loads the main menu FXML file.
     * It sets the title of the primary stage to "Main Menu" and displays the scene.
     *
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Main Menu");
            primaryStage.setScene(scene);
            primaryStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
