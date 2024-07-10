package dragonix.fierydragons1.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.shape.Circle;
import java.io.IOException;
/**
 * The CircleTypeAdapter class is a Gson TypeAdapter for serializing and deserializing Circle objects.
 * It makes the Circle object transient during serialization and skips the Circle object during deserialization.
 */
public class CircleTypeAdapter extends TypeAdapter<Circle> {

    /**
     * Writes a null value for the Circle object during serialization.
     *
     * @param out The JsonWriter object to write JSON data to.
     * @param circle The Circle object to be serialized.
     * @throws IOException If an I/O error occurs during writing.
     */
    @Override
    public void write(JsonWriter out, Circle circle) throws IOException {
        // Do nothing to make the Circle object transient during serialization
        out.nullValue();
    }
    /**
     * Skips the Circle object during deserialization.
     *
     * @param in The JsonReader object to read JSON data from.
     * @return Always returns null since the Circle object is skipped.
     * @throws IOException If an I/O error occurs during reading.
     */
    @Override
    public Circle read(JsonReader in) throws IOException {
        // Skip the Circle object during deserialization
        in.skipValue();
        return null; // or return a default value if needed
    }
}
