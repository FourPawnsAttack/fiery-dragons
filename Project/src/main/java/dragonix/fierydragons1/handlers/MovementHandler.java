package dragonix.fierydragons1.handlers;

import dragonix.fierydragons1.Game;
import dragonix.fierydragons1.Player;
import dragonix.fierydragons1.cards.Cave;
import dragonix.fierydragons1.cards.CreatureName;
import dragonix.fierydragons1.cards.DragonCards;
import javafx.scene.control.Button;

import java.util.List;
/**
 * The MovementHandler class manages the movement logic in the game.
 * It handles the processing of clicked buttons, checks for matching cards,
 * and executes the corresponding actions based on the game state.
 */
public class MovementHandler {

    private final Game game;
    /**
     * Constructs a MovementHandler object with the given Game object.
     *
     * @param game The Game object representing the current game state.
     */
    public MovementHandler(Game game){
        this.game = game;
    }
    /**
     * Processes the clicked button and performs the corresponding action.
     *
     * @param clickedButton The button that was clicked.
     */
    public void processClickedButton(Button clickedButton) {
        // Add the logic here to handle the clicked button
        // This could include moving the player, updating the game state, etc.
        System.out.println("Button clicked: " + clickedButton.getText());
        System.out.println(game.getOccupiedPositions());
        checkMatchingCard(clickedButton);
    }
    /**
     * Checks if the clicked button matches the current game state and performs the corresponding action.
     *
     * @param clickedButton The button that was clicked.
     */
    private void checkMatchingCard(Button clickedButton) {
        Player currentPlayer = game.getCurrentPlayer();
        int currentPlayerPosition = currentPlayer.getCurrentPosition();
        CreatureName currentPositionCreature = game.getVolcanoCardsHashMap().get(currentPlayerPosition);
        DragonCards clickedButtonDragonCard = game.getButtonDragonCardsHashMap().get(clickedButton);
        CreatureName clickedButtonCreature = clickedButtonDragonCard.getCreatureName();
        int dragonCardQuantity = clickedButtonDragonCard.getQuantity();

        if (currentPlayer.isGotMoveBack()) {
            handleCaveMove(clickedButton, currentPlayer, clickedButtonCreature, dragonCardQuantity);
        }
        else if (!currentPlayer.isHasMoved()) {
            handleInitialMove(clickedButton, currentPlayer, clickedButtonCreature, dragonCardQuantity);
        } else {
            handleSubsequentMove(clickedButton, currentPositionCreature, clickedButtonCreature, dragonCardQuantity);
        }
    }
    /**
     * Handles the initial move of the player.
     *
     * @param clickedButton      The button that was clicked.
     * @param currentPlayer      The current player.
     * @param clickedButtonCreature The creature associated with the clicked button.
     * @param dragonCardQuantity The quantity of dragon cards associated with the clicked button.
     */
    private void handleInitialMove(Button clickedButton, Player currentPlayer, CreatureName clickedButtonCreature, int dragonCardQuantity) {
        if (clickedButtonCreature == currentPlayer.getStartingCave().getCaveCreature() && clickedButtonCreature != CreatureName.DRAGON_PIRATE && clickedButtonCreature != CreatureName.MOVE_BACK) {
            System.out.println("Matching card found!");
            if (!game.moveCurrentPlayerBy(dragonCardQuantity)) {
                // Cannot move, there is player
                game.getDragonCardHandler().flipCardForDuration(clickedButton, 1);
                game.nextPlayer();
            } else {
                // Can move, no player
                clickedButton.setDisable(true);
                currentPlayer.setHasMoved(true);
                currentPlayer.getStartingCave().setOccupied(false);
            }
        } else if (clickedButtonCreature == CreatureName.MOVE_BACK) {
            clickedButton.setDisable(true);
        } else {
            handleNoMatch(clickedButton);
        }
    }
    /**
     * Handles the move when the player got moved back to a cave.
     *
     * @param clickedButton      The button that was clicked.
     * @param currentPlayer      The current player.
     * @param clickedButtonCreature The creature associated with the clicked button.
     * @param dragonCardQuantity The quantity of dragon cards associated with the clicked button.
     */
    private void handleCaveMove(Button clickedButton, Player currentPlayer, CreatureName clickedButtonCreature, int dragonCardQuantity) {
        System.out.println(clickedButtonCreature);
        System.out.println(currentPlayer.getMoveBackCaveObject().getCaveCreature());
        if (clickedButtonCreature == currentPlayer.getMoveBackCaveObject().getCaveCreature() && clickedButtonCreature != CreatureName.DRAGON_PIRATE) {
            System.out.println("Matching card found!");
            dragonCardQuantity--;
            if (!game.moveCurrentPlayerBy(dragonCardQuantity)) {
                game.getDragonCardHandler().flipCardForDuration(clickedButton, 1);
                game.nextPlayer();
            } else {
                game.getOccupiedPositions().add(currentPlayer.getCurrentPosition());
                clickedButton.setDisable(true);
                currentPlayer.getMoveBackCaveObject().setOccupied(false);
                currentPlayer.setGotMoveBack(false);
            }
        } else if (clickedButtonCreature == CreatureName.MOVE_BACK){
            clickedButton.setDisable(true);
        } else {
            handleNoMatch(clickedButton);
        }
    }
    /**
     * Handles the subsequent move of the player.
     *
     * @param clickedButton      The button that was clicked.
     * @param currentPositionCreature The creature associated with the current player's position.
     * @param clickedButtonCreature The creature associated with the clicked button.
     * @param dragonCardQuantity The quantity of dragon cards associated with the clicked button.
     */
    private void handleSubsequentMove(Button clickedButton, CreatureName currentPositionCreature, CreatureName clickedButtonCreature, int dragonCardQuantity) {
        System.out.println(clickedButtonCreature);
        System.out.println(currentPositionCreature);

        switch (clickedButtonCreature) {
            case DRAGON_PIRATE:
                handleDragonPirateMove(clickedButton, dragonCardQuantity);
                break;
            case MOVE_BACK:
                handleMoveBack(clickedButton);
                break;
            default:
                handleStandardOrNoMatchMove(clickedButton, currentPositionCreature, clickedButtonCreature, dragonCardQuantity);
                break;
        }
    }
    /**
     * Handles the move when the player encounters a Dragon Pirate.
     *
     * @param clickedButton The button that was clicked.
     * @param dragonCardQuantity The quantity of dragon cards associated with the clicked button.
     */
    private void handleDragonPirateMove(Button clickedButton, int dragonCardQuantity) {
        if (!game.moveCurrentPlayerBy(dragonCardQuantity)) {
            handleMoveFailure(clickedButton);
            clickedButton.setText("");
        } else {
            handleMoveSuccess(clickedButton);
            game.nextPlayer();
        }
    }
    /**
     * Handles the move when the player encounters a Move Back card.
     *
     * @param clickedButton The button that was clicked.
     */
    private void handleMoveBack(Button clickedButton) {
        System.out.println("Matching card found!");
        if (game.moveCurrentPlayerToNearestCave()) {
            System.out.println("Player moved back to nearest cave!");
        } else {
            System.out.println("No unoccupied cave was found");
        }
        clickedButton.setDisable(true);
    }
    /**
     * Handles the move when the player encounters a standard or no-match card.
     *
     * @param clickedButton The button that was clicked.
     * @param currentPositionCreature The creature associated with the current player's position.
     * @param clickedButtonCreature The creature associated with the clicked button.
     * @param dragonCardQuantity The quantity of dragon cards associated with the clicked button.
     */
    private void handleStandardOrNoMatchMove(Button clickedButton, CreatureName currentPositionCreature, CreatureName clickedButtonCreature, int dragonCardQuantity) {
        if (currentPositionCreature == clickedButtonCreature) {
            System.out.println("Matching card found!");
            if (!game.moveCurrentPlayerBy(dragonCardQuantity)) {
                handleMoveFailure(clickedButton);
                clickedButton.setText("");
            } else {
                clickedButton.setDisable(true);
            }
        } else {
            handleNoMatch(clickedButton);
        }
    }
    /**
     * Handles the move failure.
     *
     * @param clickedButton The button that was clicked.
     */
    private void handleMoveFailure(Button clickedButton) {
        game.getDragonCardHandler().flipCardForDuration(clickedButton, 1);
        game.nextPlayer();
    }
    /**
     * Handles the move success.
     *
     * @param clickedButton The button that was clicked.
     */
    private void handleMoveSuccess(Button clickedButton) {
        game.getDragonCardHandler().flipCardForDuration(clickedButton, 1);
        clickedButton.setDisable(true);
    }

    /**
     * Handles the case when no matching card is found.
     *
     * @param clickedButton The button that was clicked.
     */
    private void handleNoMatch(Button clickedButton) {
        System.out.println("No matching card found!");
        game.getDragonCardHandler().flipCardForDuration(clickedButton, 1);
        game.nextPlayer();
    }

}


