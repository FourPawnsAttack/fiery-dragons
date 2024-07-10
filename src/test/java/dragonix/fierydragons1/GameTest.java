package dragonix.fierydragons1;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Player player1, player2;
    private List<Rectangle> rectangles;
    private List<Arc> tokens;
    private Deque<Player> playersQueue;
    private List<Circle> caves;
    private List<Button> cards;

    @BeforeAll
    public static void initJavaFX() {
        Platform.startup(() -> {
            // No need to do anything here, just start the JavaFX toolkit
        });
    }
    @BeforeEach
    void setUp() {
            // Set up players
            player1 = new Player("Alice", Color.WHITE, 0,new Arc());
            player2 = new Player("Bob", Color.RED, 0,new Arc());
            playersQueue = new ArrayDeque<>(Arrays.asList(player1, player2));

            // Set up game board
            rectangles = Arrays.asList(new Rectangle(), new Rectangle(), new Rectangle());
            tokens = Arrays.asList(new Arc(), new Arc());
            caves = Arrays.asList(new Circle(), new Circle());
            cards = Arrays.asList(new Button(), new Button());

            // Initialize the game
            game = new Game(playersQueue, rectangles, tokens, caves, cards);
    }


    @Test
    void nextPlayer() {
        game.nextPlayer();
        assertEquals(player1, game.getCurrentPlayer());
        game.nextPlayer();
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
    void movePlayerTokenRight() {
        assertTrue(game.movePlayerTokenRight());
        assertEquals(1, player1.getCurrentPosition());
        // Test wrap around
        assertTrue(game.movePlayerTokenRight());
        assertTrue(game.movePlayerTokenRight());
        assertEquals(0, player1.getCurrentPosition()); // Should wrap around to position 0
    }

    @Test
    void movePlayerTokenLeft() {
        assertTrue(game.movePlayerTokenLeft());
        assertEquals(2, player1.getCurrentPosition()); // Should wrap to the last position
        assertTrue(game.movePlayerTokenLeft());
        assertEquals(1, player1.getCurrentPosition());
    }
    @Test
    void testPlayerWinning() {
        // Move player1 24 steps ahead to simulate a complete round
        for (int i = 0; i < 24; i++) {
            game.movePlayerTokenRight();
        }

        // Check if player1 has won
        assertTrue(player1.hasPlayerWon(), "Player should have won after completing a round");

        // Check if the game's current player is still player1
        assertEquals(player1, game.getCurrentPlayer(), "Current player should still be player1 after winning");
    }


}