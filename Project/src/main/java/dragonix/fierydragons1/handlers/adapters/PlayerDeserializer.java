package dragonix.fierydragons1.handlers.adapters;

import com.google.gson.*;
import dragonix.fierydragons1.Player;
import dragonix.fierydragons1.cards.Cave;
import dragonix.fierydragons1.cards.CreatureName;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.lang.reflect.Type;
/**
 * The PlayerDeserializer class deserializes JSON into a Player object.
 * It implements the JsonDeserializer interface and overrides the deserialize method.
 */
public class PlayerDeserializer implements JsonDeserializer<Player> {
    /**
     * Deserializes JSON into a Player object.
     *
     * @param json The JSON element being deserialized.
     * @param typeOfT The type of the Object to deserialize to.
     * @param context Context for deserialization that is passed to a custom deserializer during invocation of its {@code deserialize()} method.
     * @return The deserialized Player object.
     * @throws JsonParseException If JSON parsing fails.
     */
    @Override
    public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String playerName = jsonObject.get("playerName").getAsString();

        JsonObject colorObj = jsonObject.get("color").getAsJsonObject();
        Color color = new Color(
                colorObj.get("red").getAsDouble(),
                colorObj.get("green").getAsDouble(),
                colorObj.get("blue").getAsDouble(),
                colorObj.get("opacity").getAsDouble()
        );

        Integer currentPosition = jsonObject.get("currentPosition").getAsInt();

        JsonObject dragonTokenObj = jsonObject.get("dragonToken").getAsJsonObject();
        ImageView dragonToken = new ImageView(new Image(dragonTokenObj.get("url").getAsString()));
        dragonToken.setFitWidth(dragonTokenObj.get("fitWidth").getAsDouble());
        dragonToken.setFitHeight(dragonTokenObj.get("fitHeight").getAsDouble());

        JsonObject startingCaveObj = jsonObject.get("startingCave").getAsJsonObject();
        CreatureName creatureName = CreatureName.valueOf(startingCaveObj.get("caveCreature").getAsString());
        int attachedPosition = startingCaveObj.get("attachedPosition").getAsInt();
        Cave startingCave = new Cave(null, attachedPosition, creatureName);
        startingCave.setOccupied(startingCaveObj.get("isOccupied").getAsBoolean());

        Player player = new Player(playerName, color, currentPosition, dragonToken, startingCave);

        if (jsonObject.has("moveBackCave")) {
            player.setMoveBackCave(CreatureName.valueOf(jsonObject.get("moveBackCave").getAsString()));
        }

        if (jsonObject.has("totalStepsTaken")) {
            player.setTotalStepsTaken(jsonObject.get("totalStepsTaken").getAsInt());
        }

        if (jsonObject.has("hasMoved")) {
            player.setHasMoved(jsonObject.get("hasMoved").getAsBoolean());
        }

        if (jsonObject.has("stepsTaken")) {
            player.setStepsTaken(jsonObject.get("stepsTaken").getAsInt());
        }

        if (jsonObject.has("gotMoveBack")) {
            player.setGotMoveBack(jsonObject.get("gotMoveBack").getAsBoolean());
        }

        return player;
    }
}
