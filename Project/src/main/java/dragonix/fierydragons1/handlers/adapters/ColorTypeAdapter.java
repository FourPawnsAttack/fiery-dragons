package dragonix.fierydragons1.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.paint.Color;
import java.io.IOException;
/**
 * The ColorTypeAdapter class is a Gson TypeAdapter for serializing and deserializing Color objects.
 * It serializes a Color object to its red, green, blue, and opacity components,
 * and deserializes these components to construct a Color object.
 */
public class ColorTypeAdapter extends TypeAdapter<Color> {

    /**
     * Writes the Color components to JSON.
     *
     * @param out The JsonWriter object to write JSON data to.
     * @param color The Color object to be serialized.
     * @throws IOException If an I/O error occurs during writing.
     */
    @Override
    public void write(JsonWriter out, Color color) throws IOException {
        out.beginObject();
        out.name("red").value(color.getRed());
        out.name("green").value(color.getGreen());
        out.name("blue").value(color.getBlue());
        out.name("opacity").value(color.getOpacity());
        out.endObject();
    }
    /**
     * Reads Color components from JSON and constructs a Color object.
     *
     * @param in The JsonReader object to read JSON data from.
     * @return The Color object constructed from the JSON components.
     * @throws IOException If an I/O error occurs during reading.
     */
    @Override
    public Color read(JsonReader in) throws IOException {
        double red = 0;
        double green = 0;
        double blue = 0;
        double opacity = 1;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "red":
                    red = in.nextDouble();
                    break;
                case "green":
                    green = in.nextDouble();
                    break;
                case "blue":
                    blue = in.nextDouble();
                    break;
                case "opacity":
                    opacity = in.nextDouble();
                    break;
            }
        }
        in.endObject();

        return new Color(red, green, blue, opacity);
    }
}
