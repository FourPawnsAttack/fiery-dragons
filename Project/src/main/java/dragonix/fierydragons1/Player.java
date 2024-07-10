package dragonix.fierydragons1;
import dragonix.fierydragons1.cards.Cave;
import dragonix.fierydragons1.cards.CreatureName;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Player class represents a player in the game, holding information about their position,
 * token, and various game states.
 */
public class Player {
    private Cave startingCave;
    private String playerName;
    private final Color color;
    private Integer currentPosition;
    private ImageView dragonToken;
    private final Integer startingPosition;
    private int totalStepsTaken = 0;
    private boolean hasMoved = false;
    private int stepsTaken = 0;
    private boolean gotMoveBack;
    private CreatureName moveBackCave;

    /**
     * Retrieves the cave to which the player must move back.
     *
     * @return The cave to which the player must move back.
     */
    public Cave getMoveBackCaveObject() {
        return moveBackCaveObject;
    }

    /**
     * Sets the cave to which the player must move back.
     *
     * @param moveBackCaveObject The new cave to which the player must move back.
     */
    public void setMoveBackCaveObject(Cave moveBackCaveObject) {
        this.moveBackCaveObject = moveBackCaveObject;
    }

    private Cave moveBackCaveObject;

    /**
     * Constructs a new Player with the specified name, color, position, dragon token, and starting cave.
     *
     * @param playerName       The name of the player.
     * @param color            The color representing the player in the UI.
     * @param currentPosition  The current position of the player on the game board.
     * @param dragonToken      The graphical representation of the player's token.
     * @param startingCave     The starting cave of the player.
     */
    public Player(String playerName, Color color, Integer currentPosition, ImageView dragonToken, Cave startingCave){
        this.playerName = playerName;
        this.color = color;
        this.currentPosition = currentPosition;
        this.dragonToken = dragonToken;
        this.startingPosition = currentPosition;
        this.startingCave = startingCave;
        this.gotMoveBack = false;
    }

    // Helper method to get the starting cave creature based on the player's name
    /**
     * Returns the starting creature associated with a player.
     *
     * @param playerName The name of the player.
     * @return The starting creature for the player.
     */
    private static CreatureName getStartingCaveCreature(String playerName) {
        switch (playerName) {
            case "Player 1":
                return CreatureName.SPIDER;
            case "Player 2":
                return CreatureName.BAT;
            case "Player 3":
                return CreatureName.BABY_DRAGON;
            case "Player 4":
                return CreatureName.SALAMANDER;
            default:
                return null; // Return null if the player name is invalid
        }
    }

    // Helper method to find a cave in the caveList based on the cave creature
    /**
     * Finds a cave in the caveList based on the specified creature.
     *
     * @param caveList    The list of caves to search.
     * @param creatureName The creature associated with the cave.
     * @return The cave containing the specified creature, or null if no cave is found.
     */
    private static Cave findCaveByCreature(List<Cave> caveList, CreatureName creatureName) {
        for (Cave cave : caveList) {
            if (cave.getCaveCreature() == creatureName) {
                return cave;
            }
        }
        return null; // Return null if no cave is found for the specified creature
    }

    /**
     * Creates a Player object from a map of player data.
     *
     * @param playerData The map containing player data.
     * @param caveList    The map of cave objects.
     * @param tokenMap   The map of ImageView tokens.
     * @return A new Player object.
     */

    public static Player fromMap(Map<String, Object> playerData, List<Cave> caveList, Map<String, ImageView> tokenMap) {
        // Extract data from map
        String playerName = (String) playerData.get("playerName");
        Color color = parseColor((Map<String, Object>) playerData.get("color"));
        int currentPosition = ((Double) playerData.get("currentPosition")).intValue();
        ImageView dragonToken = tokenMap.get(playerName);
        CreatureName startingCaveCreature = getStartingCaveCreature(playerName);
        Cave startingCave = findCaveByCreature(caveList, startingCaveCreature);

        // Create new player
        Player player = new Player(playerName, color, currentPosition, dragonToken, startingCave);

        // Set additional fields
        player.setTotalStepsTaken(((Double) playerData.get("totalStepsTaken")).intValue());
        player.setHasMoved((Boolean) playerData.get("hasMoved"));
        player.setStepsTaken(((Double) playerData.get("stepsTaken")).intValue());
        player.setGotMoveBack((Boolean) playerData.get("gotMoveBack"));

        // Optionally set moveBackCave if it's present
        if (playerData.containsKey("moveBackCave")) {
            player.setMoveBackCave(CreatureName.valueOf((String) playerData.get("moveBackCave")));
        }

        return player;
    }

    /**
     * Parses color data from a map.
     *
     * @param colorData The map containing color data.
     * @return A Color object.
     */
    private static Color parseColor(Map<String, Object> colorData) {
        double red = (Double) colorData.get("red");
        double green = (Double) colorData.get("green");
        double blue = (Double) colorData.get("blue");
        double opacity = (Double) colorData.get("opacity");
        return new Color(red, green, blue, opacity);
    }

    /**
     * Creates a list of Player objects from data.
     *
     * @param tempList The list of player data maps.
     * @param caveMap  The map of cave objects.
     * @param tokenMap The map of ImageView tokens.
     * @return An ArrayList of Player objects.
     */
//    public static ArrayList<Player> createPlayersFromData(ArrayList<Map<String, Object>> tempList, Map<String, Cave> caveMap, Map<String, ImageView> tokenMap) {
//        ArrayList<Player> players = new ArrayList<>();
//        for (Map<String, Object> playerData : tempList) {
//            Player player = Player.fromMap(playerData, caveMap, tokenMap);
//            players.add(player);
//        }
//        return players;
//    }
    // ... Getters and setters below
    /**
     * Retrieves the player's name.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets whether the player has obtained a "move back" card.
     *
     * @param b True if the player has obtained a "move back" card, false otherwise.
     */
    public void setGotMoveBack(boolean b) {
        gotMoveBack = b;
    }

    /**
     * Sets the cave to which the player must move back.
     *
     * @param moveBackCave The cave to which the player must move back.
     */
    public void setMoveBackCave(CreatureName moveBackCave) {
        this.moveBackCave = moveBackCave;
    }


    /**
     * Retrieves the cave to which the player must move back.
     *
     * @return The cave to which the player must move back.
     */
    public CreatureName getMoveBackCave() {
        return moveBackCave;
    }

    /**
     * Retrieves the color representing the player.
     *
     * @return The color representing the player.
     */
    public Color getColor(){
        return color;
    }

    /**
     * Retrieves the player's current position on the game board.
     *
     * @return The player's current position.
     */
    public Integer getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Sets the player's current position on the game board.
     *
     * @param currentPosition The player's new position.
     */
    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * Retrieves the graphical representation of the player's token.
     *
     * @return The player's token.
     */
    public ImageView getDragonToken() {
        return dragonToken;
    }

    /**
     * Retrieves the position of the player's starting cave.
     *
     * @return The position of the starting cave.
     */
    public int getCavePosition() {
        return this.startingCave.getAttachedPosition();
    }

    /**
     * Retrieves the player's starting position on the game board.
     *
     * @return The player's starting position.
     */
    public int getStartingPosition() {
        return startingPosition;
    }

    /**
     * Retrieves the total number of steps taken by the player.
     *
     * @return The total number of steps taken.
     */
    public int getTotalStepsTaken() {
        return totalStepsTaken;
    }

    /**
     * Checks if the player has won the game.
     *
     * @return True if the player has won, false otherwise.
     */
    public boolean hasPlayerWon() {
        return currentPosition == (startingCave.getAttachedPosition() + 1)%24 && !startingCave.getOccupied();
    }

    /**
     * Checks if the player has moved.
     *
     * @return True if the player has moved, false otherwise.
     */
    public boolean isHasMoved() {
        return hasMoved;
    }

    /**
     * Sets whether the player has moved.
     *
     * @param hasMoved True if the player has moved, false otherwise.
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Retrieves the player's starting cave.
     *
     * @return The starting cave.
     */
    public Cave getStartingCave() {
        return this.startingCave;
    }

    /**
     * Sets the player's starting cave.
     *
     * @param cave The new starting cave.
     * @return The new starting cave.
     */
    public Cave setStartingCave(Cave cave) {
        this.startingCave = cave;
        return cave;
    }

    /**
     * Sets the player's current position on the game board.
     *
     * @param position The player's new position.
     * @return The player's new position.
     */
    public int setCurrenPosition(int position) {
        this.currentPosition = position;
        return position;
    }

    /**
     * Increments the number of steps taken by the player.
     *
     * @param steps The number of steps to increment by.
     */
    public void incrementStepsTaken(int steps) {
        this.stepsTaken += steps;
    }

    /**
     * Retrieves the number of steps taken by the player.
     *
     * @return The number of steps taken.
     */
    public int getStepsTaken() {
        return this.stepsTaken;
    }

    /**
     * Retrieves the player's current X coordinate.
     *
     * @return The current X coordinate.
     */
    public double getCurrentX() {
        return this.dragonToken.getX();
    }

    /**
     * Retrieves the player's current Y coordinate.
     *
     * @return The current Y coordinate.
     */
    public double getCurrentY() {
        return this.dragonToken.getY();
    }

    /**
     * Sets the player's current X coordinate.
     *
     * @param newX The new X coordinate.
     */
    public void setCurrentX(double newX) {
        this.dragonToken.setX(newX);
    }

    /**
     * Sets the player's current Y coordinate.
     *
     * @param newY The new Y coordinate.
     */
    public void setCurrentY(double newY) {
        this.dragonToken.setY(newY);
    }

    /**
     * Checks if the player has obtained a "move back" card.
     *
     * @return True if the player has obtained a "move back" card, false otherwise.
     */
    public boolean isGotMoveBack() {
        return gotMoveBack;
    }

    /**
     * Sets the graphical representation of the player's token.
     *
     * @param currentToken The new token.
     */
    public void setDragonToken(ImageView currentToken) {
        this.dragonToken = currentToken;
    }

    /**
     * Sets the total number of steps taken by the player.
     *
     * @param totalStepsTaken The new total number of steps taken.
     */
    public void setTotalStepsTaken(int totalStepsTaken) {
    }

    /**
     * Sets the number of steps taken by the player.
     *
     * @param stepsTaken The new number of steps taken.
     */
    public void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }
}
