package dragonix.fierydragons1;

import dragonix.fierydragons1.cards.Cave;
import dragonix.fierydragons1.cards.CreatureName;
import dragonix.fierydragons1.cards.DragonCards;
import dragonix.fierydragons1.handlers.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
/**
 * The Game class represents the core logic of the Fiery Dragons game.
 * It manages player turns, movement, game state, timers, and interactions with the game board.
 */
public class Game {
    private HashMap<Integer, ImageView> imageViewMap = new HashMap<>();
    private final AnchorPane gameBoardPane;
    private Deque<Player> playersQueue;
    private Player currentPlayer;
    private Timeline timeline;
    private int timeRemaining;

    /**
     * Retrieves the occupied positions on the game board.
     *
     * @return The set of occupied positions.
     */
    public Set<Integer> getOccupiedPositions() {
        return occupiedPositions;
    }

    private final Set<Integer> occupiedPositions = new HashSet<>();
    private List<Rectangle> volcanoRectangles;

    /**
     * Retrieves the list dragon tokens.
     *
     * @return The list of dragon tokens.
     */
    public List<ImageView> getDragonTokens() {
        return dragonTokens;
    }

    /**
     * Sets the list of dragon tokens.
     *
     * @param dragonTokens The list of dragon tokens to set.
     */
    public void setDragonTokens(List<ImageView> dragonTokens) {
        this.dragonTokens = dragonTokens;
    }

    private List<ImageView> dragonTokens;
    private VolcanoCardHandler volcanoCardHandler;
    private DragonCardHandler dragonCardHandler;
    private MovementHandler movementHandler;
    private SavingHandler savingHandler;
    private LoadingHandler loadingHandler;
    private HashMap<Button, DragonCards> buttonDragonCardsHashMap = new HashMap<>();
    private HashMap<Integer, CreatureName> volcanoCardsHashMap = new HashMap<>();
    private PlayerChangeListener playerChangeListener;
    private TimerChangeListener timerChangeListener;
    private List<Cave> caveList;
    /**
     * Constructs a new Game object with the specified game board pane.
     *
     * @param gameBoardPane The AnchorPane representing the game board.
     */
    public Game(AnchorPane gameBoardPane) {
        this.gameBoardPane = gameBoardPane;
        this.dragonCardHandler = new DragonCardHandler(this);
        this.movementHandler = new MovementHandler(this);
        this.savingHandler = new SavingHandler(buttonDragonCardsHashMap, volcanoCardsHashMap, "saves");
        this.loadingHandler = new LoadingHandler("saves");
    }
    /**
     * Initializes a new game with the provided parameters.
     *
     * @param playersQueue       The queue of players participating in the game.
     * @param volcanoRectangles  The list of rectangles representing volcanoes on the game board.
     * @param dragonTokens       The list of image views representing dragon tokens.
     * @param dragonButtons      The list of buttons representing dragon cards.
     * @param caveList           The list of caves on the game board.
     */
    public void initializeNewGame(Deque<Player> playersQueue, List<Rectangle> volcanoRectangles, List<ImageView> dragonTokens, List<Button> dragonButtons, List<Cave> caveList) {
        this.playersQueue = playersQueue;
        this.volcanoRectangles = volcanoRectangles;
        this.dragonTokens = dragonTokens;
        this.caveList = caveList;
        this.volcanoCardHandler = new VolcanoCardHandler();
        this.dragonCardHandler = new DragonCardHandler(this);
        this.movementHandler = new MovementHandler(this);

        dragonCardHandler.assignCreaturesToDragonButtons(dragonButtons);
        buttonDragonCardsHashMap = dragonCardHandler.getButtonDragonCardsHashMap();
        volcanoCardsHashMap = volcanoCardHandler.getVolcanoCardsHashMap();
        assignTokensToCaves();
        volcanoCardHandler.assignCreaturesToCards(volcanoRectangles);

        setCurrentPlayer();

        for (ImageView token : dragonTokens) {
            int uniqueId = System.identityHashCode(token);
            imageViewMap.put(uniqueId, token);
        }

        savingHandler.setButtonDragonCardsHashMap(buttonDragonCardsHashMap);
        savingHandler.setVolcanoCardsHashMap(volcanoCardsHashMap);
        initializeGameTimer(); // Ensure timer is initialized
    }

    /**
     * Assigns dragon tokens to their starting caves.
     */
    private void assignTokensToCaves() {
        for (Player player : playersQueue) {
            ImageView token = player.getDragonToken();
            Cave cave = player.getStartingCave();
            setTokenPosition(token, cave.getCaveArc().getLayoutX() - 30, cave.getCaveArc().getLayoutY() - 30);
        }
    }
    /**
     * Sets the position of a token on the game board.
     *
     * @param token The image view representing the token.
     * @param x     The x-coordinate of the token's position.
     * @param y     The y-coordinate of the token's position.
     */
    private void setTokenPosition(ImageView token, double x, double y) {
        token.setLayoutX(x);
        token.setLayoutY(y);
    }
    /**
     * Sets the current player for the game.
     */
    private void setCurrentPlayer() {
        if (!playersQueue.isEmpty()) {
            this.currentPlayer = playersQueue.poll();
            playersQueue.offer(this.currentPlayer);
        }
    }
    /**
     * Starts the turn timer for the current player.
     */
    public void startTurn() {
        if (timeline != null) {
            timeline.stop();
        }

        timeRemaining = 30; // Set initial time for the turn
        notifyTimerChangeListener(); // Notify initial time

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--;
            notifyTimerChangeListener();

            if (timeRemaining <= 0) {
                nextPlayer();
            }
        }));
        timeline.setCycleCount(10); // 10 seconds countdown
        timeline.play();
    }

    /**
     * Advances the game to the next player in the queue.
     * This method polls the current player from the queue and offers them back to the end.
     */
    public void nextPlayer() {
        rotatePlayerQueue();
        notifyPlayerChangeListener();
        dragonCardHandler.unflipCards();
        startTurn(); // Reset the timer for the next player
    }
    /**
     * Rotates the player queue by moving the current player to the end of the queue.
     * Notifies the player change listener after rotation.
     */
    private void rotatePlayerQueue() {
        currentPlayer = playersQueue.poll();
        playersQueue.offer(currentPlayer);
        System.out.println("Switched to next player: " + currentPlayer.getPlayerName() + " at position " + currentPlayer.getCurrentPosition());
    }
    /**
     * Notifies the player change listener about the current player change.
     */
    private void notifyPlayerChangeListener() {
        if (playerChangeListener != null) {
            playerChangeListener.onPlayerChange(currentPlayer);
        }
    }
    /**
     * Notifies the timer change listener about the remaining time.
     */
    private void notifyTimerChangeListener() {
        if (timerChangeListener != null) {
            timerChangeListener.onTimerChange(timeRemaining);
        }
    }
    /**
     * Moves the current player by the specified offset.
     *
     * @param offset The number of steps to move the player.
     * @return True if the move was successful, false otherwise.
     */
    public boolean moveCurrentPlayerBy(int offset) {
        System.out.println("Moving player by " + offset + " steps.");
        int newPosition = calculateNewPosition(currentPlayer.getCurrentPosition(), offset);

        currentPlayer.incrementStepsTaken(offset);
        if (isWinningMove(newPosition)) {
            displayWinnerPage();
        }

        boolean moveSuccessful = movePlayerToken(currentPlayer, newPosition);

        if (moveSuccessful) {
            startTurn(); // Reset the timer after a successful move
        }

        return moveSuccessful;
    }
    /**
     * Calculates the new position based on the current position and the offset.
     *
     * @param currentPosition The current position of the player.
     * @param offset           The offset to be applied to the current position.
     * @return The new position after applying the offset.
     */
    private int calculateNewPosition(int currentPosition, int offset) {
        int newPosition = (currentPosition + offset) % volcanoRectangles.size();
        return newPosition < 0 ? newPosition + volcanoRectangles.size() : newPosition;
    }
    /**
     * Checks if moving to the new position would result in a winning move.
     *
     * @param newPosition The new position to check.
     * @return True if the move is a winning move, false otherwise.
     */
    private boolean isWinningMove(int newPosition) {
        System.out.println("Steps taken: " + currentPlayer.getStepsTaken());
        return newPosition == currentPlayer.getCavePosition() && currentPlayer.getStepsTaken() > 24 && !currentPlayer.getStartingCave().getOccupied();
    }
    /**
     * Displays the winner page when a player wins the game.
     */
    private void displayWinnerPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dragonix/fierydragons1/winner-page.fxml"));
            Parent root = loader.load();
            WinnerPageController controller = loader.getController();
            controller.setWinner(currentPlayer.getPlayerName());
            Stage stage = (Stage) currentPlayer.getDragonToken().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Moves the player token to the specified new position.
     *
     * @param player      The player whose token is to be moved.
     * @param newPosition The new position for the player token.
     * @return True if the move was successful, false otherwise.
     */
    boolean movePlayerToken(Player player, int newPosition) {
        if (isPositionUnoccupied(newPosition, player.getCurrentPosition())) {
            updatePlayerPosition(player, newPosition);
            return true;
        } else {
            System.out.println("Position " + newPosition + " is already occupied.");
            return false;
        }
    }
    /**
     * Moves the player's token to occupy the specified cave if it's unoccupied.
     *
     * @param player       The player whose token is to be moved.
     * @param caveToOccupy The cave to be occupied by the player.
     * @return True if the player successfully occupies the cave, false otherwise.
     */
    private boolean movePlayerTokenToCave(Player player, Cave caveToOccupy) {
        if (!caveToOccupy.getOccupied()) {
//            updatePlayerPosition(player, newPosition);
            int newPosition = caveToOccupy.getAttachedPosition()-1;
//            player.setCurrentPosition(newPosition);
            int offset;
            int currentPosition = player.getCurrentPosition();
            // Calculate the offset for moving backward in a circular list
            if (newPosition <= currentPosition) {
                offset = newPosition - currentPosition;
            } else {
                offset = (newPosition - 24) - currentPosition;
            }
            System.out.println("offset: " + offset);
            player.incrementStepsTaken(offset);
            player.setCurrentPosition(newPosition);
            updateTokenPosition(player.getDragonToken(), newPosition);
            if (player.getStepsTaken()==1){
                player.setStepsTaken(0);
            }
            System.out.println("Steps taken: " + player.getStepsTaken());
            occupiedPositions.remove(player.getCurrentPosition());
            return true;
        } else {
            System.out.println("Cave " + caveToOccupy.getAttachedPosition() + " is already occupied.");
            return false;
        }
    }
    /**
     * Checks if a position on the game board is unoccupied.
     *
     * @param newPosition    The new position to check.
     * @param currentPosition The current position of the player.
     * @return True if the position is unoccupied or the player is already there, false otherwise.
     */
    private boolean isPositionUnoccupied(int newPosition, int currentPosition) {
        return !occupiedPositions.contains(newPosition) || currentPosition == newPosition;
    }
    /**
     * Updates the position of the player on the game board.
     *
     * @param player      The player whose position is to be updated.
     * @param newPosition The new position of the player.
     */
    private void updatePlayerPosition(Player player, int newPosition) {
        occupiedPositions.remove(player.getCurrentPosition());
        occupiedPositions.add(newPosition);
        player.setCurrentPosition(newPosition);
        updateTokenPosition(player.getDragonToken(), newPosition);
    }
    /**
     * Updates the position of the player's token on the game board.
     *
     * @param token   The token to be moved.
     * @param position The position to move the token to.
     */
    private void updateTokenPosition(ImageView token, int position) {
        Rectangle rectangle = volcanoRectangles.get(position);
        double centerX = rectangle.getLayoutX() + rectangle.getWidth() / 2;
        double centerY = rectangle.getLayoutY() + rectangle.getHeight() / 2;
        setTokenPosition(token, centerX - token.getFitWidth() / 2, centerY - token.getFitHeight() / 2);
    }
    /**
     * Retrieves the current player.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * Sets the listener for player change events.
     *
     * @param listener The listener for player change events.
     */
    public void setPlayerChangeListener(PlayerChangeListener listener) {
        this.playerChangeListener = listener;
    }
    /**
     * Retrieves the hashmap of volcano cards and their corresponding creature names.
     *
     * @return The hashmap of volcano cards.
     */
    public HashMap<Integer, CreatureName> getVolcanoCardsHashMap() {
        return volcanoCardsHashMap;
    }
    /**
     * Handles the button click event by processing the clicked button.
     *
     * @param clickedButton The button that was clicked.
     */
    public void handleClickedButton(Button clickedButton) {
        if (movementHandler != null) {
            movementHandler.processClickedButton(clickedButton);
        }
    }
    /**
     * Retrieves the hashmap of button-DragonCards pairs.
     *
     * @return The hashmap of button-DragonCards pairs.
     */
    public HashMap<Button, DragonCards> getButtonDragonCardsHashMap() {
        return buttonDragonCardsHashMap;
    }
    /**
     * Retrieves the DragonCardHandler instance.
     *
     * @return The DragonCardHandler instance.
     */
    public DragonCardHandler getDragonCardHandler() {
        return dragonCardHandler;
    }
    /**
     * Retrieves the list of caves on the game board.
     *
     * @return The list of caves.
     */
    public List<Cave> getCaveList() {
        return caveList;
    }
    /**
     * Moves the player token to the nearest unoccupied cave.
     *
     * @return True if the move was successful, false otherwise.
     */
    public boolean moveCurrentPlayerToNearestCave() {
        Player currentPlayer = getCurrentPlayer();
        Cave nearestCave = findNearestUnoccupiedCave(currentPlayer.getCurrentPosition());
        occupiedPositions.remove(currentPlayer.getCurrentPosition());

        if (nearestCave == null) {
            System.out.println("No nearest Cave");
            return false;
        }

        return moveToCave(currentPlayer, nearestCave);
    }
    /**
     * Finds the nearest unoccupied cave to the current player's position.
     *
     * @param currentPosition The current position of the player.
     * @return The nearest unoccupied cave, or null if no suitable cave is found.
     */
    private Cave findNearestUnoccupiedCave(int currentPosition) {
        // Print all the caves' occupied status
        for (Cave cave : caveList) {
            System.out.println(cave.getCaveCreature() + " " + cave.getAttachedPosition() + " " + cave.getOccupied());
        }

        // Check if the current position is exactly one step before any cave position
        for (Cave cave : caveList) {
            int cavePosition = cave.getAttachedPosition();
            if (!cave.getOccupied() && (currentPosition + 1 == cavePosition || (currentPosition == 24 && cavePosition == 1))) {
                return cave;
            }
        }

        // Create a list of caves sorted by their positions in descending order
        List<Cave> sortedCaves = caveList.stream()
                .sorted(Comparator.comparingInt(Cave::getAttachedPosition).reversed())
                .collect(Collectors.toList());

        // Iterate through the sorted caves to find the nearest unoccupied cave directly behind
        for (Cave cave : sortedCaves) {
            int cavePosition = cave.getAttachedPosition();

            // If cave position is less than the current position or wraps around to the end
            if (!cave.getOccupied() && (cavePosition < currentPosition || cavePosition == currentPosition)) {
                return cave;
            }
        }

        // If no suitable cave is found in the first pass, check the remaining positions considering wrap-around
        for (Cave cave : sortedCaves) {
            int cavePosition = cave.getAttachedPosition();

            // If cave position is greater than the current position and wrap-around is considered
            if (!cave.getOccupied() && cavePosition > currentPosition) {
                return cave;
            }
        }
        return null;
    }
    /**
     * Saves the current game state to a file.
     *
     * @throws IOException If an I/O error occurs while saving the game.
     */
    public void saveGame() throws IOException {
        HashMap<String, Object> gameState = new HashMap<>();
        gameState.put("playerQueue", playersQueue);
        gameState.put("caveList", caveList);
        gameState.put("occupiedPositions", occupiedPositions);
        savingHandler.saveGameToFile(gameState);
    }

    /**
     * Sets the list of caves in the game.
     *
     * @param loadedCaveList The list of caves to set.
     */
    private void setCaveList(List<Cave> loadedCaveList) {
        this.caveList = loadedCaveList;
    }
    /**
     * Updates the references to player tokens after loading a saved game.
     */
    private void updateTokenReferences() {
        for (Player player : playersQueue) {
            ImageView token = player.getDragonToken();
            int uniqueId = System.identityHashCode(token);
            ImageView currentToken = getImageViewById(uniqueId);
            player.setDragonToken(currentToken);
        }
    }
    /**
     * Adds an ImageView to the map of image views.
     *
     * @param imageView The ImageView to add.
     * @param uniqueId  The unique identifier for the ImageView.
     */
    public void addImageView(ImageView imageView, int uniqueId) {
        imageViewMap.put(uniqueId, imageView);
    }
    /**
     * Retrieves an ImageView from the map of image views based on its unique identifier.
     *
     * @param id The unique identifier of the ImageView.
     * @return The ImageView associated with the provided unique identifier, or null if not found.
     */
    public ImageView getImageViewById(int id) {
        return imageViewMap.get(id);
    }
    /**
     * Restores player positions on the game board.
     */
    void restorePlayerPositions() {
        for (Player player : playersQueue) {
            ImageView token = player.getDragonToken();
            updateTokenPosition(token, player.getCurrentPosition());
        }
    }
    /**
     * Initializes the game timer.
     */
    public void initializeGameTimer() {
        // Create and start the game timer for the first player's turn
        startTurn();
    }
    /**
     * Updates the mapping of button IDs to DragonCards after loading a saved game.
     *
     * @param loadedDragonCards The mapping of button IDs to DragonCards.
     * @param dragonButtons     The list of dragon buttons.
     */
    private void updateButtonDragonCardsHashMap(HashMap<String, DragonCards> loadedDragonCards, List<Button> dragonButtons) {
        buttonDragonCardsHashMap.clear();
        for (Button button : dragonButtons) {
            String buttonId = button.getId();
            if (loadedDragonCards.containsKey(buttonId)) {
                buttonDragonCardsHashMap.put(button, loadedDragonCards.get(buttonId));
            }
        }
    }
    /**
     * Sets the players queue.
     *
     * @param playersQueue The players queue to set.
     */
    public void setPlayersQueue(Deque<Player> playersQueue) {
        this.playersQueue = playersQueue;
        if (!playersQueue.isEmpty()) {
            currentPlayer = playersQueue.peek();
        }
    }
    /**
     * Sets the mapping of button IDs to DragonCards.
     *
     * @param buttonDragonCardsHashMap The mapping of button IDs to DragonCards.
     */
    public void setButtonDragonCardsHashMap(HashMap<Button, DragonCards> buttonDragonCardsHashMap) {
        this.buttonDragonCardsHashMap = buttonDragonCardsHashMap;
        savingHandler.setButtonDragonCardsHashMap(buttonDragonCardsHashMap);
    }
    /**
     * Sets the list of volcano rectangles on the game board.
     *
     * @param volcanoRectangles The list of volcano rectangles to set.
     */
    public void setVolcanoRectangles(List<Rectangle> volcanoRectangles){
        this.volcanoRectangles = volcanoRectangles;
    }
    /**
     * Sets the mapping of volcano card positions to creature names.
     *
     * @param volcanoCardsHashMap The mapping of volcano card positions to creature names.
     */
    public void setVolcanoCardsHashMap(HashMap<Integer, CreatureName> volcanoCardsHashMap) {
        this.volcanoCardsHashMap = volcanoCardsHashMap;
        savingHandler.setVolcanoCardsHashMap(volcanoCardsHashMap);

        // Loop through all volcano rectangles and set their color based on the volcanoCardsHashMap
        for (Map.Entry<Integer, CreatureName> entry : volcanoCardsHashMap.entrySet()) {
            Integer position = entry.getKey();
            CreatureName creatureName = entry.getValue();

            // Ensure the position is within the bounds of the volcanoRectangles list
            if (position >= 0 && position < volcanoRectangles.size()) {
                Rectangle volcanoRectangle = volcanoRectangles.get(position);
                Color color = creatureName.getColor();
                volcanoRectangle.setFill(color);
            }
        }
    }
    /**
     * Moves the current player to the nearest unoccupied cave if possible.
     *
     * @param player      The player to move.
     * @param nearestCave The nearest unoccupied cave.
     * @return True if the player successfully moved to the cave, false otherwise.
     */
    private boolean moveToCave(Player player, Cave nearestCave) {
        if (movePlayerTokenToCave(player, nearestCave)) {
            occupyCave(player, nearestCave);
            occupiedPositions.remove(currentPlayer.getCurrentPosition());

            return true;
        }
        System.out.println("Cannot move to Cave");
        return false;
    }

    /**
     * Occupies the specified cave with the given player.
     *
     * @param player The player occupying the cave.
     * @param cave   The cave to be occupied.
     */
    private void occupyCave(Player player, Cave cave) {
        cave.setOccupiedPlayer(player);
        cave.setOccupied(true);
        player.setMoveBackCaveObject(cave);
        player.setMoveBackCave(cave.getCaveCreature());
        player.setGotMoveBack(true);
        setTokenPosition(player.getDragonToken(), cave.getCaveArc().getLayoutX() - 30, cave.getCaveArc().getLayoutY() - 30);
    }
    /**
     * Sets the timer change listener for this game.
     *
     * @param listener The timer change listener to be set.
     */
    public void setTimerChangeListener(TimerChangeListener listener) {
        this.timerChangeListener = listener;
    }
    /**
     * Disposes of resources used by the game.
     */
    public void dispose() {
        // Stop the timeline if it is running
        if (timeline != null) {
            timeline.stop();
        }
        // Clear the players queue
        if (playersQueue != null) {
            playersQueue.clear();
        }
        // Clear any other resources if necessary
        occupiedPositions.clear();
        imageViewMap.clear();
        buttonDragonCardsHashMap.clear();
        volcanoCardsHashMap.clear();

        // Convert to ArrayList if necessary and clear
        if (dragonTokens != null) {
            dragonTokens = new ArrayList<>(dragonTokens);
            dragonTokens.clear();
        }
        if (caveList != null) {
            caveList = new ArrayList<>(caveList);
            caveList.clear();
        }
        if (volcanoRectangles != null) {
            volcanoRectangles = new ArrayList<>(volcanoRectangles);
            volcanoRectangles.clear();
        }
        System.out.println("Game resources have been disposed.");
    }

}
