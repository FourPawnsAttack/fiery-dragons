package dragonix.fierydragons1.handlers;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VolcanoCardHandlerTest {

    @Test
    void assignCreaturesToCards() {
        VolcanoCardHandler handler = new VolcanoCardHandler();
        List<Rectangle> rectangles = Arrays.asList(new Rectangle(), new Rectangle(), new Rectangle());
        assertDoesNotThrow(() -> handler.assignCreaturesToCards(rectangles));
        assertEquals(3, ((List<?>) rectangles).size());
        assertNotNull(rectangles.getFirst().getFill());
    }

    @Test
    void buttonAssignCreaturesToCards() {
        List<Rectangle> rectangles = Arrays.asList(new Rectangle(), new Rectangle(), new Rectangle());
        assertDoesNotThrow(() -> VolcanoCardHandler.buttonAssignCreaturesToCards(rectangles));
        assertEquals(3, rectangles.size());
        assertNotNull(rectangles.getFirst().getFill());
    }

    @Test
    void getSequences() {
        VolcanoCardHandler handler = new VolcanoCardHandler();
        assertNotNull(handler.getSequences());
        assertFalse(handler.getSequences().isEmpty());
    }

}