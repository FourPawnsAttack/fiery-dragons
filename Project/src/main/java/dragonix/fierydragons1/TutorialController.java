package dragonix.fierydragons1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The TutorialController class manages the tutorial view in the application, providing functionality
 * to display tutorial images and navigate back to the main menu.
 */
public class TutorialController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ImageView imageView;

    @FXML
    private Button nextButton;
    @FXML
    private AnchorPane anchorPane;

    private int currentIndex = 0;
    private String[] imagePaths = {
            "/dragonix/fierydragons1/images/1.png",
            "/dragonix/fierydragons1/images/2.png",
            "/dragonix/fierydragons1/images/3.png",
            "/dragonix/fierydragons1/images/4.png",
            "/dragonix/fierydragons1/images/5.png",
            "/dragonix/fierydragons1/images/6.png",
            "/dragonix/fierydragons1/images/7.png",
            "/dragonix/fierydragons1/images/8.png",
            "/dragonix/fierydragons1/images/9.png",
            "/dragonix/fierydragons1/images/10.png",
            "/dragonix/fierydragons1/images/11.png",
            "/dragonix/fierydragons1/images/12.png",
            "/dragonix/fierydragons1/images/13.png",
            "/dragonix/fierydragons1/images/14.png"
    };

    /**
     * Initializes the tutorial view by setting the image view properties and displaying the first image.
     */
    @FXML
    private void initialize() {
        imageView.fitWidthProperty().bind(anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(anchorPane.heightProperty());
        // Set the first image
        imageView.setImage(new Image(getClass().getResource(imagePaths[currentIndex]).toExternalForm()));
        nextButton.setOnAction(event -> showNextImage());
    }

    /**
     * Displays the next image in the tutorial. Cycles back to the first image after the last one.
     */
    private void showNextImage() {
        currentIndex = (currentIndex + 1) % imagePaths.length;  // Cycle through the images
        imageView.setImage(new Image(getClass().getResource(imagePaths[currentIndex]).toExternalForm()));
    }

    /**
     * Changes the scene to the main menu when the main menu button is clicked.
     *
     * @param event the action event that triggered the method
     * @throws IOException if the main menu FXML cannot be loaded
     */
    public void changeToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Changing to Main Menu");
    }
}
