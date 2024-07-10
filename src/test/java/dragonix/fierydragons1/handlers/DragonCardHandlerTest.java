package dragonix.fierydragons1.handlers;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javafx.scene.control.Button;
import dragonix.fierydragons1.cards.CreatureName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DragonCardHandlerTest {

    @Test
    void assignCreaturesToDragonButtons() {
        DragonCardHandler handler = new DragonCardHandler();
        List<Button> buttons = new ArrayList<>();

        // Test empty button list
        assertDoesNotThrow(() -> handler.assignCreaturesToDragonButtons(new ArrayList<>()));

        // Normal functionality test
        buttons = new ArrayList<>(Collections.nCopies(16, new Button()));
        List<Button> finalButtons = buttons;
        assertDoesNotThrow(() -> handler.assignCreaturesToDragonButtons(finalButtons));
        assertFalse(buttons.stream().anyMatch(button -> button.getText() == null || button.getText().isEmpty()));

    }

    @Test
    void buttonAssignCreaturesToDragonButtons() {
        List<Button> buttons = new ArrayList<>();

        // Test empty button list
        assertDoesNotThrow(() -> DragonCardHandler.buttonAssignCreaturesToDragonButtons(new ArrayList<>()));

        // Normal functionality test
        buttons = new ArrayList<>(Collections.nCopies(16, new Button())); // Assuming 39 creatures can be assigned
        List<Button> finalButtons = buttons;
        assertDoesNotThrow(() -> DragonCardHandler.buttonAssignCreaturesToDragonButtons(finalButtons));
        assertFalse(buttons.stream().anyMatch(button -> button.getText() == null || button.getText().isEmpty()));

        // Insufficient creatures test
        buttons = new ArrayList<>(Collections.nCopies(40, new Button())); // Assuming only 39 available creatures
        List<Button> finalButtons1 = buttons;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> DragonCardHandler.buttonAssignCreaturesToDragonButtons(finalButtons1));
        assertEquals("Not enough creature entries to assign to buttons", exception.getMessage());

        // Null button list test
        assertThrows(NullPointerException.class, () -> DragonCardHandler.buttonAssignCreaturesToDragonButtons(null));
    }
}
