package dragonix.fierydragons1.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dragonix.fierydragons1.Player;

import dragonix.fierydragons1.handlers.adapters.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.Map;
/**
 * The LoadingHandler class handles loading game data from a JSON file.
 * It provides methods to deserialize JSON data and retrieve the game state information.
 */
public class LoadingHandler {
    private Gson gson;
    private File initialDirectory;
    private Map<String, Object> gameData;
    /**
     * Constructs a LoadingHandler object with the specified initial directory path.
     *
     * @param initialDirectoryPath The initial directory path where the file chooser dialog opens.
     */
    public LoadingHandler(String initialDirectoryPath) {
        this.initialDirectory = new File(initialDirectoryPath);
        if (!this.initialDirectory.exists()) {
            this.initialDirectory.mkdirs();
        }
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Button.class, new ButtonTypeAdapter())
                .registerTypeAdapter(Circle.class, new CircleTypeAdapter())
                .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                .registerTypeAdapter(ImageView.class, new ImageViewTypeAdapter())
                .registerTypeAdapter(Player.class, new PlayerDeserializer())
                .setPrettyPrinting()
                .create();
    }
    /**
     * Loads game data from a JSON file selected by the user.
     *
     * @return The loaded game data as a Map.
     * @throws IOException If an I/O error occurs while loading the file.
     */
    public Map<String, Object> loadGameFromFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Save File");
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null); // Pass null to center on the screen
        if (selectedFile != null) {
            try (FileReader reader = new FileReader(selectedFile)) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                gameData = gson.fromJson(reader, type);
            } catch (JsonSyntaxException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Json Format Error");
                alert.setHeaderText("Json Format Error"); // No header text
                alert.setContentText("Please check if the json format is correct.");
                alert.showAndWait();
            }
        }
        return gameData;
    }

}
