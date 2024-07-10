package dragonix.fierydragons1.handlers.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
/**
 * The ImageViewTypeAdapter class is a Gson TypeAdapter for serializing and deserializing ImageView objects.
 */
public class ImageViewTypeAdapter extends TypeAdapter<ImageView> {
    /**
     * Writes the ImageView object to JSON.
     *
     * @param out The JsonWriter object to write JSON data to.
     * @param imageView The ImageView object to be serialized.
     * @throws IOException If an I/O error occurs during writing.
     */
    @Override
    public void write(JsonWriter out, ImageView imageView) throws IOException {
        out.beginObject();
        Image image = imageView.getImage();
        if (image != null) {
            out.name("url").value(image.getUrl());
        }
        out.name("fitWidth").value(imageView.getFitWidth());
        out.name("fitHeight").value(imageView.getFitHeight());
        out.endObject();
    }
    /**
     * Reads JSON and constructs an ImageView object.
     *
     * @param in The JsonReader object to read JSON data from.
     * @return The ImageView object constructed from JSON.
     * @throws IOException If an I/O error occurs during reading.
     */
    @Override
    public ImageView read(JsonReader in) throws IOException {
        String url = null;
        double fitWidth = 0;
        double fitHeight = 0;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "url":
                    url = in.nextString();
                    break;
                case "fitWidth":
                    fitWidth = in.nextDouble();
                    break;
                case "fitHeight":
                    fitHeight = in.nextDouble();
                    break;
            }
        }
        in.endObject();

        ImageView imageView = new ImageView();
        if (url != null) {
            imageView.setImage(new Image(url));
        }
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        return imageView;
    }
}
