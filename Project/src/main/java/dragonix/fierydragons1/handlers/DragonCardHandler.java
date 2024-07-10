package dragonix.fierydragons1.handlers;

import dragonix.fierydragons1.Game;
import dragonix.fierydragons1.cards.CardFactory;
import dragonix.fierydragons1.cards.DragonCards;
import javafx.scene.control.Button;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
/**
 * Handles the logic for assigning creatures to dragon card buttons in the game.
 */
public class DragonCardHandler {
    private final CardFactory cardFactory = new CardFactory();
    private final Game game;
    // Update the HashMap to store DragonCards instead of String
    private HashMap<Button, DragonCards> buttonDragonCardsHashMap = new HashMap<>();
    private List<Button> dragonButtons;

    /**
     * Constructor for DragonCardHandler.
     * @param game The game instance to which this handler belongs.
     */
    public DragonCardHandler(Game game) {
        this.game = game;
    }

    /**
     * Assigns a random creature to each dragon card button.
     * The method repeats each creature a set number of times, shuffles the collection,
     * and then assigns each creature to a button from the list of dragon buttons.
     *
     * @param dragonButtons A list of buttons representing dragon cards.
     * @throws IllegalArgumentException If there are not enough creature entries to assign to buttons.
     */
    public void assignCreaturesToDragonButtons(List<Button> dragonButtons) {
        this.dragonButtons = dragonButtons;
        Collections.shuffle(dragonButtons); // Shuffle buttons to assign cards randomly

        // Check if we have at least as many dragon cards as buttons
        if (dragonButtons.size() > 3 * cardFactory.getCardTypes()) {
            throw new IllegalArgumentException("Not enough DragonCards can be assigned to buttons");
        }

        // Assign each dragon card to a button
        for (Button button : dragonButtons) {
            DragonCards card;
            try {
                card = cardFactory.createRandomDragonCard(); // Create a new DragonCard
            } catch (IllegalStateException e) {
                throw new IllegalArgumentException("Not enough unique DragonCards can be assigned to buttons");
            }

            // Store the DragonCards object directly in the HashMap
            buttonDragonCardsHashMap.put(button, card);

            // Set event handler to flip the card when clicked
            button.setOnAction(e -> {
                flipCard(button);
                game.handleClickedButton(button);
            });
        }
    }
    /**
     * Flips the card associated with the specified button for a specified duration.
     *
     * @param button The button whose associated card will be flipped.
     * @param seconds The duration for which the card will remain flipped (in seconds).
     */
    void flipCardForDuration(Button button, int seconds) {
        flipCard(button); // Flip the card immediately on button press
        button.setDisable(true); // Disable the button to prevent further clicks

        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L); // Sleep in a separate thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle interrupted exception
                System.err.println("Sleep interrupted: " + e.getMessage());
            }

            Platform.runLater(() -> { // Update UI on JavaFX thread after sleep
                unflipCard(button);
                button.setDisable(false);
            });
        }).start(); // Start the thread
    }
    /**
     * Flips the card associated with the specified button.
     *
     * @param button The button whose associated card will be flipped.
     */
    private void flipCard(Button button) {
        DragonCards card = game.getButtonDragonCardsHashMap().get(button);
        if (card != null) {
            String buttonText = card.getCreatureName().getName() + " (" + card.getQuantity() + ")";
            Platform.runLater(() -> button.setText(buttonText)); // Make sure UI update is done on the JavaFX thread
        } else {
            System.err.println("No card associated with this button.");
        }
    }
    /**
     * Unflips the card associated with the specified button.
     *
     * @param button The button whose associated card will be unflipped.
     */
    public void unflipCard(Button button) {
        Platform.runLater(() -> {
            button.setText("");
            button.setDisable(false);
        }
        );
    }

    /**
     * Unflips all cards.
     */
    public void unflipCards() {
        for (Button button : buttonDragonCardsHashMap.keySet()) {
            button.setText("");
            button.setDisable(false);
        }
    }
    /**
     * Gets the HashMap containing the association between buttons and dragon cards.
     *
     * @return The HashMap containing the association between buttons and dragon cards.
     */
    public HashMap<Button, DragonCards> getButtonDragonCardsHashMap() {
        return buttonDragonCardsHashMap;
    }

    /**
     * Sets the HashMap containing the association between buttons and dragon cards.
     *
     * @param buttonDragonCardsHashMap The HashMap containing the association between buttons and dragon cards.
     */
    public void setButtonDragonCardsHashMap(HashMap<Button, DragonCards> buttonDragonCardsHashMap) {
        this.buttonDragonCardsHashMap = buttonDragonCardsHashMap;
    }
    /**
     * Gets the list of buttons representing dragon cards.
     *
     * @return The list of buttons representing dragon cards.
     */
    public List<Button> getButtons() {
        return dragonButtons;
    }
}
