package dragonix.fierydragons1.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.control.Button;

import java.io.IOException;
/**
 * The DragonClassAdapter class is a Gson TypeAdapter for serializing and deserializing Button objects.
 * It serializes a Button object to its ID and deserializes a String ID to a Button object.
 */
public class DragonClassAdapter extends TypeAdapter<Button> {

    /**
     * Writes the Button ID to JSON.
     *
     * @param out The JsonWriter object to write JSON data to.
     * @param button The Button object to be serialized.
     * @throws IOException If an I/O error occurs during writing.
     */
    @Override
    public void write(JsonWriter out, Button button) throws IOException {
        out.value(button.getId());
    }
    /**
     * Reads a String ID from JSON and constructs a Button object.
     *
     * @param in The JsonReader object to read JSON data from.
     * @return The Button object constructed from the ID.
     * @throws IOException If an I/O error occurs during reading.
     */
    @Override
    public Button read(JsonReader in) throws IOException {
        String id = in.nextString();
        Button button = new Button();
        button.setId(id);
        return button;
    }
}
