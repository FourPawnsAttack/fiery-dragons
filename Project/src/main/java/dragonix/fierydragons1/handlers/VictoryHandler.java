package dragonix.fierydragons1.handlers;
import dragonix.fierydragons1.Player;

/**
 * The VictoryHandler class is responsible for handling victory conditions in the game.
 * It provides a method to check if a player has achieved victory.
 */
public class VictoryHandler {
    /**
     * Checks if the specified player has achieved victory.
     * Victory is achieved if the player has returned to their starting position
     * and has taken at least 24 steps.
     *
     * @param player The player to check for victory.
     * @return true if the player has achieved victory, false otherwise.
     */
    public boolean checkVictory(Player player) {
        return player.getCurrentPosition().equals(player.getStartingPosition()) && player.getStepsTaken() >= 24;
    }
}
