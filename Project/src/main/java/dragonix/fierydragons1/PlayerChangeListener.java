package dragonix.fierydragons1;

/**
 * The PlayerChangeListener interface should be implemented by classes that wish to receive
 * notifications when the player changes in the game.
 */
public interface PlayerChangeListener {
    /**
     * This method is called whenever the player changes.
     *
     * @param player the new player
     */
    void onPlayerChange(Player player);
}
