package dragonix.fierydragons1;

import dragonix.fierydragons1.cards.Cave;
import dragonix.fierydragons1.cards.CreatureName;
import dragonix.fierydragons1.cards.DragonCards;
import dragonix.fierydragons1.handlers.VictoryHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The MainAppController class manages the main gameplay functionality and UI of the Fiery Dragons game.
 * It handles player actions, game setup, and state transitions within the game.
 */
public class MainAppController implements PlayerChangeListener, TimerChangeListener {
    @FXML
    private Button DragonCard1, DragonCard2, DragonCard3, DragonCard4, DragonCard5, DragonCard6, DragonCard7, DragonCard8, DragonCard9, DragonCard10, DragonCard11, DragonCard12, DragonCard13, DragonCard14, DragonCard15, DragonCard16;
    @FXML
    private Rectangle VolcanoRectangle0, VolcanoRectangle1, VolcanoRectangle2, VolcanoRectangle3, VolcanoRectangle4, VolcanoRectangle5, VolcanoRectangle6, VolcanoRectangle7, VolcanoRectangle8, VolcanoRectangle9, VolcanoRectangle10, VolcanoRectangle11, VolcanoRectangle12, VolcanoRectangle13, VolcanoRectangle14, VolcanoRectangle15, VolcanoRectangle16, VolcanoRectangle17, VolcanoRectangle18, VolcanoRectangle19, VolcanoRectangle20, VolcanoRectangle21, VolcanoRectangle22, VolcanoRectangle23;
    @FXML
    private ImageView DragonTokenGreen, DragonTokenRed, DragonTokenBlue, DragonTokenYellow;
    @FXML
    private Circle CaveBAT, CaveBABYDRAGON, CaveSALAMANDER, CaveSPIDER;
    @FXML
    private Text PlayerText;
    @FXML
    private Rectangle PlayerBox;

    @FXML
    private Button debugNextPlayer;
    @FXML
    private Button debugTest;
    @FXML
    private Button debugMoveFront;
    @FXML
    private Button debugMoveBack;
    @FXML
    private Text TimerText;
    @FXML
    private AnchorPane gameboardPane;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private List<Rectangle> volcanoRectangles = new ArrayList<>();
    List<ImageView> dragonTokens = new ArrayList<>();
    List<Circle> caves = new ArrayList<>();
    Deque<Player> playersQueue = new ArrayDeque<>();
    private List<Cave> caveList = new ArrayList<>();
    private Player currentPlayer;
    private List<Button> dragonButtons = new ArrayList<>();
    private Game game;
    private VictoryHandler victoryHandler;

    // Modes:
    private boolean debugMode = false;
    private boolean tutorialMode = false;
    private boolean customMode = false;
    private boolean errorOccurred = false;

    /**
     * Initializes the main application controller and sets up the UI components.
     */
    @FXML
    private void initialize() {
        // initialize all rectangles into a list
        volcanoRectangles = Arrays.asList(
                VolcanoRectangle0, VolcanoRectangle1, VolcanoRectangle2,
                VolcanoRectangle3, VolcanoRectangle4, VolcanoRectangle5,
                VolcanoRectangle6, VolcanoRectangle7, VolcanoRectangle8,
                VolcanoRectangle9, VolcanoRectangle10, VolcanoRectangle11,
                VolcanoRectangle12, VolcanoRectangle13, VolcanoRectangle14,
                VolcanoRectangle15, VolcanoRectangle16, VolcanoRectangle17,
                VolcanoRectangle18, VolcanoRectangle19, VolcanoRectangle20,
                VolcanoRectangle21, VolcanoRectangle22, VolcanoRectangle23
        );

        dragonButtons = Arrays.asList(
                DragonCard1, DragonCard2, DragonCard3, DragonCard4, DragonCard5, DragonCard6, DragonCard7, DragonCard8, DragonCard9, DragonCard10, DragonCard11, DragonCard12, DragonCard13, DragonCard14, DragonCard15, DragonCard16
        );
        // initialize all Dragon Tokens into a list
        dragonTokens = Arrays.asList(DragonTokenGreen, DragonTokenRed, DragonTokenBlue, DragonTokenYellow);
        // initialize all caves into a list
        caves = Arrays.asList(CaveBAT, CaveBABYDRAGON, CaveSALAMANDER, CaveBABYDRAGON);

        updateDebugModeUI();
    }

    /**
     * Sets up the players and game data.
     *
     * @param gameData The game data to set up.
     * @return true if the game setup is valid, false otherwise.
     */
    boolean setupPlayersAndGame(Map<String, Object> gameData) {
        boolean validGame = false;
        if (!customMode) {
            setupDefaultGame();
            validGame = true;
        } else {
            validGame = setupCustomGame(gameData);
        }
        return validGame;
    }

    /**
     * Sets up the default game with predefined players and caves.
     */
    private void setupDefaultGame() {
        Cave cave1 = new Cave(CaveSPIDER, 2, CreatureName.SPIDER);
        Cave cave2 = new Cave(CaveBAT, 8, CreatureName.BAT);
        Cave cave3 = new Cave(CaveBABYDRAGON, 14, CreatureName.BABY_DRAGON);
        Cave cave4 = new Cave(CaveSALAMANDER, 20, CreatureName.SALAMANDER);

        Player player1 = new Player("Player 1", Color.LIME, 0, DragonTokenGreen, cave1);
        Player player2 = new Player("Player 2", Color.BLUE, 6, DragonTokenBlue, cave2);
        Player player3 = new Player("Player 3", Color.YELLOW, 12, DragonTokenYellow, cave3);
        Player player4 = new Player("Player 4", Color.RED, 18, DragonTokenRed, cave4);

        playersQueue.addAll(Arrays.asList(player1, player2, player3, player4));
        caveList.addAll(Arrays.asList(cave1, cave2, cave3, cave4));

        initializeGame(null);
    }

    // Method to find a cave in the caveList based on the caveCreature attribute
    /**
     * Finds a cave in the caveList based on the specified caveCreature.
     *
     * @param caveList     The list of caves to search.
     * @param caveCreature The creature name of the cave to find.
     * @return The cave object if found, otherwise null.
     */
    private Cave findCaveByCreature(List<Cave> caveList, String caveCreature) {
        for (Cave cave : caveList) {
            if (cave.getCaveCreature().toString().equals(caveCreature)) {
                return cave;
            }
        }
        return null; // Return null if no matching cave is found
    }

    /**
     * Sets up a custom game using the provided game data.
     *
     * @param gameData The game data to use for setup.
     * @return true if the custom game setup is valid, false otherwise.
     */
    private boolean setupCustomGame(Map<String, Object> gameData) {
        boolean validGame = false;
        try {
            ArrayList<Map<String, Object>> tempList = (ArrayList<Map<String, Object>>) gameData.get("playerQueue");
            ArrayList<Map<String, Object>> caveTemp = (ArrayList<Map<String, Object>>) gameData.get("caveList");

            Map<String, ImageView> tokenMap = new HashMap<>();
            tokenMap.put("Player 1", DragonTokenGreen);
            tokenMap.put("Player 2", DragonTokenBlue);
            tokenMap.put("Player 3", DragonTokenYellow);
            tokenMap.put("Player 4", DragonTokenRed);

            Map<String, Cave> caveMap = new HashMap<>();
            caveMap.put("Player 1", new Cave(CaveSPIDER, 2, CreatureName.SPIDER));
            caveMap.put("Player 2", new Cave(CaveBAT, 8, CreatureName.BAT));
            caveMap.put("Player 3", new Cave(CaveBABYDRAGON, 14, CreatureName.BABY_DRAGON));
            caveMap.put("Player 4", new Cave(CaveSALAMANDER, 20, CreatureName.SALAMANDER));


//            caveList.addAll(caveMap.values());

            // Create a map to hold cave arcs based on their IDs
            Map<String, Circle> caveArcs = new HashMap<>();
            caveArcs.put("CaveSPIDER", CaveSPIDER);
            caveArcs.put("CaveBAT", CaveBAT);
            caveArcs.put("CaveBABYDRAGON", CaveBABYDRAGON);
            caveArcs.put("CaveSALAMANDER", CaveSALAMANDER);

            for (Map<String, Object> caveData : caveTemp) {
                Cave cave = Cave.fromMap(caveData, caveArcs);
                boolean isOccupied = (boolean) caveData.get("isOccupied");
                cave.setOccupied(isOccupied);
                caveList.add(cave);
            }

            for (Map<String, Object> playerData : tempList) {
                Player player = Player.fromMap(playerData, caveList, tokenMap);

                if (playerData.containsKey("moveBackCave")) {
                    String moveBackCave = (String) playerData.get("moveBackCave");
                    Cave moveBackCaveObject = findCaveByCreature(caveList, moveBackCave);
                    System.out.println(moveBackCaveObject);

                    if (moveBackCaveObject != null) {
                        player.setMoveBackCaveObject(moveBackCaveObject);
                    } else {
                        // Handle case where the specified moveBackCave is not found
                    }
                }
                playersQueue.add(player);
            }

            // Shift the playersQueue to the left
            Player lastPlayer = playersQueue.pollLast();
            playersQueue.offerFirst(lastPlayer);
            // Convert LinkedTreeMap to HashMap
            Map<Integer, CreatureName> volcanoCardsHashMap = new HashMap<>();
            Map<String, Object> tempVolcanoCardsMap = (Map<String, Object>) gameData.get("volcanoCardSequence");
            for (Map.Entry<String, Object> entry : tempVolcanoCardsMap.entrySet()) {
                volcanoCardsHashMap.put(Integer.parseInt(entry.getKey()), CreatureName.valueOf(entry.getValue().toString()));
            }

            // Load dragon button data
            Map<String, Map<String, Object>> dragonCardsInfo = (Map<String, Map<String, Object>>) gameData.get("dragonCardsInfo");
            HashMap<Button, DragonCards> buttonDragonCardsHashMap = new HashMap<>();
            for (Button dragonButton : dragonButtons) {
                String buttonId = dragonButton.getId();
                if (dragonCardsInfo.containsKey(buttonId)) {
                    Map<String, Object> cardInfo = dragonCardsInfo.get(buttonId);
                    CreatureName creatureName = CreatureName.valueOf((String) cardInfo.get("creatureName"));
                    int quantity = ((Number) cardInfo.get("quantity")).intValue();
                    DragonCards dragonCard = new DragonCards(creatureName, quantity);
                    buttonDragonCardsHashMap.put(dragonButton, dragonCard);
                }
            }
            if (dragonCardsInfo.size() == 16 && tempVolcanoCardsMap.size() == 24){
                validGame = true;
            }

            if (validGame){
                initializeGame(volcanoCardsHashMap, buttonDragonCardsHashMap);
            }

        } catch (NullPointerException e) {
            System.err.println(e);
            errorOccurred = true; // Set error flag
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Choose a valid save file"); // Optional, can be set to a specific header
            alert.setContentText("Save file must be a .json and data must be first created using the game's save feature.");
            alert.showAndWait();
            validGame = false;
            return validGame;
        }
        return validGame;
    }

    /**
     * Initializes the game with the provided volcano card sequence and dragon card information.
     *
     * @param volcanoCardsHashMap    A map containing the volcano card sequence.
     * @param buttonDragonCardsHashMap A map containing the dragon card information for each button.
     */
    private void initializeGame(Map<Integer, CreatureName> volcanoCardsHashMap, HashMap<Button, DragonCards> buttonDragonCardsHashMap) {
        if (errorOccurred) {
            return; // Skip initialization if an error occurred
        }

        game = new Game(gameboardPane);
        game.initializeNewGame(playersQueue, volcanoRectangles, dragonTokens, dragonButtons, caveList);
        game.setPlayerChangeListener(this);
        game.setTimerChangeListener(this);
        for (Player player : playersQueue) {
            if (player.isHasMoved()){
                game.movePlayerToken(player, player.getCurrentPosition());
            }
        }

        currentPlayer = game.getCurrentPlayer();
        PlayerBox.setFill(currentPlayer.getColor());
        PlayerText.setText(currentPlayer.getPlayerName());
        victoryHandler = new VictoryHandler();
        if (customMode) {
            game.setVolcanoCardsHashMap((HashMap<Integer, CreatureName>) volcanoCardsHashMap);
            game.setButtonDragonCardsHashMap(buttonDragonCardsHashMap);
        }
    }

    /**
     * Initializes the game with the provided volcano card sequence.
     *
     * @param volcanoCardsHashMap A map containing the volcano card sequence.
     */
    private void initializeGame(Map<Integer, CreatureName> volcanoCardsHashMap) {
        if (errorOccurred) {
            return; // Skip initialization if an error occurred
        }

        game = new Game(gameboardPane);
        game.initializeNewGame(playersQueue, volcanoRectangles, dragonTokens, dragonButtons, caveList);
        game.setPlayerChangeListener(this);
        game.setTimerChangeListener(this);
        for (Player player : playersQueue) {
            if (player.isHasMoved()){
                game.movePlayerToken(player, player.getCurrentPosition());
            }
        }

        currentPlayer = game.getCurrentPlayer();
        PlayerBox.setFill(currentPlayer.getColor());
        PlayerText.setText(currentPlayer.getPlayerName());
        victoryHandler = new VictoryHandler();
        if (customMode) {
            game.setVolcanoCardsHashMap((HashMap<Integer, CreatureName>) volcanoCardsHashMap);
        }
    }

    /**
     * Handles the logic for advancing to the next player's turn.
     *
     * @param event The action event triggering the method.
     */
    public void nextPlayer(ActionEvent event) {
        game.nextPlayer();
        currentPlayer = game.getCurrentPlayer();
        PlayerText.setText(currentPlayer.getPlayerName());
        PlayerBox.setFill(currentPlayer.getColor());

        if (currentPlayer.hasPlayerWon()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dragonix/fierydragons1/winner-page.fxml"));
                Parent root = loader.load();
                WinnerPageController controller = loader.getController();
                controller.setWinner(currentPlayer.getPlayerName());
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
                System.out.println("Player " + currentPlayer.getPlayerName() + " has won the game!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes the scene to the main menu.
     *
     * @param event The action event triggering the method.
     * @throws IOException If an I/O error occurs during loading the main menu scene.
     */
    public void changeToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        System.out.println("Changing to Main Menu");
        if(game != null){
            game.dispose();
        }
    }
    /**
     * Handles the logic for changing the displayed player information.
     *
     * @param newPlayer The new player whose information is to be displayed.
     */
    @Override
    public void onPlayerChange(Player newPlayer) {
        PlayerText.setText(newPlayer.getPlayerName());
        PlayerBox.setFill(newPlayer.getColor());
    }
    /**
     * Moves the current player's token forward by one position for debugging purposes.
     */
    public void debugMoveFront() {
        game.moveCurrentPlayerBy(1);
    }
    /**
     * Moves the current player's token backward by one position for debugging purposes.
     */
    public void debugMoveBack() {
        game.moveCurrentPlayerBy(-1);
    }
    /**
     * Sets the debug mode for the game.
     *
     * @param mode True to enable debug mode, false otherwise.
     */
    public void setDebugMode(boolean mode) {
        debugMode = mode;
        updateDebugModeUI();
    }
    /**
     * Updates the UI components related to the debug mode.
     */
    private void updateDebugModeUI() {
        debugNextPlayer.setVisible(debugMode);
        debugTest.setVisible(debugMode);
        debugMoveFront.setVisible(debugMode);
        debugMoveBack.setVisible(debugMode);
    }
    /**
     * Saves the current game state.
     *
     * @throws IOException If an I/O error occurs during the save operation.
     */
    public void save() throws IOException {
        System.out.println("Game Saved");
        game.saveGame();
    }
    /**
     * Sets the game instance.
     *
     * @param game The game instance to set.
     */
    public void setGame(Game game) {
        this.game = game;
        game.setPlayerChangeListener(this);
        currentPlayer = game.getCurrentPlayer();
        PlayerBox.setFill(currentPlayer.getColor());
        PlayerText.setText(currentPlayer.getPlayerName());
        game.restorePlayerPositions();
    }
    /**
     * Sets the custom game mode.
     *
     * @param customMode True to enable custom game mode, false otherwise.
     */
    public void setCustomGame(boolean customMode) {
        this.customMode = customMode;
    }
    /**
     * Handles the change in the timer display.
     *
     * @param time The time remaining to be displayed.
     */
    @Override
    public void onTimerChange(int time) {
        TimerText.setText(Integer.toString(time));
    }
}
