package dragonix.fierydragons1.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dragonix.fierydragons1.cards.CreatureName;
import dragonix.fierydragons1.cards.DragonCards;
import dragonix.fierydragons1.handlers.adapters.ButtonTypeAdapter;
import dragonix.fierydragons1.handlers.adapters.CircleTypeAdapter;
import dragonix.fierydragons1.handlers.adapters.ColorTypeAdapter;
import dragonix.fierydragons1.handlers.adapters.ImageViewTypeAdapter;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * The SavingHandler class handles the saving of game data to a JSON file.
 * It provides methods to serialize game state information, such as volcano card sequence,
 * dragon card information, and any additional game state provided, into a JSON format
 * and save it to a specified directory.
 */
public class SavingHandler {
    private HashMap<Integer, CreatureName> volcanoCardsHashMap;
    private HashMap<Button, DragonCards> buttonDragonCardsHashMap;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Button.class, new ButtonTypeAdapter())
            .registerTypeAdapter(Circle.class, new CircleTypeAdapter())
            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
            .registerTypeAdapter(ImageView.class, new ImageViewTypeAdapter())
            .setPrettyPrinting()
            .create();
    private String saveDirectory;
    /**
     * Constructs a SavingHandler object with the given button-to-dragon-cards mapping,
     * volcano card sequence, and save directory.
     *
     * @param buttonDragonCardsHashMap HashMap containing button to dragon card mapping.
     * @param volcanoCardsHashMap      HashMap containing volcano card sequence.
     * @param saveDirectory            Directory where the game data will be saved.
     */
    public SavingHandler(HashMap<Button, DragonCards> buttonDragonCardsHashMap, HashMap<Integer, CreatureName> volcanoCardsHashMap, String saveDirectory) {
        this.buttonDragonCardsHashMap = buttonDragonCardsHashMap;
        this.volcanoCardsHashMap = volcanoCardsHashMap;
        this.saveDirectory = saveDirectory;
    }
    /**
     * Serializes the game state into a JSON string.
     *
     * @param gameState Additional game state information.
     * @return JSON string representing the serialized game state.
     */
    public String saveGame(HashMap<String, Object> gameState) {
        HashMap<String, Object> gameData = new HashMap<>();
        gameData.put("volcanoCardSequence", volcanoCardsHashMap);

        // Convert Button objects to their IDs for serialization
        HashMap<String, DragonCards> buttonIdsToDragonCards = new HashMap<>();
        for (Map.Entry<Button, DragonCards> entry : buttonDragonCardsHashMap.entrySet()) {
            buttonIdsToDragonCards.put(entry.getKey().getId(), entry.getValue());
        }
        gameData.put("dragonCardsInfo", buttonIdsToDragonCards);

        // Merge gameState into gameData if gameState is not null
        if (gameState != null) {
            gameData.putAll(gameState);
        }

        return gson.toJson(gameData);
    }
    /**
     * Generates a unique file name based on the current date and time.
     *
     * @return A unique file name for saving the game data.
     */
    private String generateFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmssSSS");
        String date = dateFormat.format(new Date());
        String fileName = String.format("%s-save-file.json", date);
        return saveDirectory + "/" + fileName;
    }
    /**
     * Saves the game data to a JSON file.
     *
     * @param gameState Additional game state information.
     * @throws IOException If an I/O error occurs while saving the file.
     */
    public void saveGameToFile(HashMap<String, Object> gameState) throws IOException {
        // Ensure the save directory exists
        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = generateFileName();
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(saveGame(gameState));
        }
        System.out.println("Game data saved to " + fileName);
    }
    /**
     * Sets the button-to-dragon-cards mapping.
     *
     * @param buttonDragonCardsHashMap New HashMap containing button to dragon card mapping.
     */
    public void setButtonDragonCardsHashMap(HashMap<Button, DragonCards> buttonDragonCardsHashMap) {
        this.buttonDragonCardsHashMap = buttonDragonCardsHashMap;
    }
    /**
     * Sets the volcano card sequence.
     *
     * @param volcanoCardsHashMap New HashMap containing volcano card sequence.
     */
    public void setVolcanoCardsHashMap(HashMap<Integer, CreatureName> volcanoCardsHashMap) {
        this.volcanoCardsHashMap = volcanoCardsHashMap;
    }
}

