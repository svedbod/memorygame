package se.mindlab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextGameUITest {

    private ColourMemoryGame game;
    private TextGameUI textGameUI;
    private List<String> commands;

    @BeforeEach
    public void setUp() {
        // Predefined colors to control the game board for testing
        String[] predefinedColors = {
                "Red", "Red", "Blue", "Blue",
                "Green", "Green", "Yellow", "Yellow",
                "Purple", "Purple", "Cyan", "Cyan",
                "Orange", "Orange", "Magenta", "Magenta"
        };

        game = new ColourMemoryGame(new GameUpdateListener() {
            @Override
            public void updateGameUI() {
                // Dummy listener for testing purposes
            }
        }, predefinedColors);

        commands = new ArrayList<>();
        textGameUI = new TextGameUI(game, new Scanner(System.in), commands);
    }

    @Test
    public void testPrintBoard() {
        // Setting the state of the board
        game.getBoard().initializeCards(new String[]{
                "Red", "Red", "Blue", "Blue",
                "Green", "Green", "Yellow", "Yellow",
                "Purple", "Purple", "Cyan", "Cyan",
                "Orange", "Orange", "Magenta", "Magenta"
        });
        game.getBoard().getCard(0, 0).setFaceUp(true);
        game.getBoard().getCard(1, 1).setFaceUp(true);

        String expectedOutput = "Re XX XX XX \n" +
                "XX Gr XX XX \n" +
                "XX XX XX XX \n" +
                "XX XX XX XX \n";
        String actualOutput = textGameUI.getBoardAsString();

        // Debugging output to help diagnose the problem
        System.out.println("Expected Board State:\n" + expectedOutput);
        System.out.println("Actual Board State:\n" + actualOutput);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGameLoop() {
        // Simulate user input for a sequence of moves to match all pairs
        commands.add("0 0"); commands.add("0 1"); // Match Red
        commands.add("1 0"); commands.add("1 1"); // Match Green
        commands.add("2 0"); commands.add("2 1"); // Match Purple
        commands.add("3 0"); commands.add("3 1"); // Match Orange
        commands.add("0 2"); commands.add("0 3"); // Match Blue
        commands.add("1 2"); commands.add("1 3"); // Match Yellow
        commands.add("2 2"); commands.add("2 3"); // Match Cyan
        commands.add("3 2"); commands.add("3 3"); // Match Magenta

        textGameUI = new TextGameUI(game, new Scanner(System.in), commands);
        textGameUI.runGameLoop();

        // Check if the game is over
        assertEquals(8, game.getScore()); // Assuming all pairs are matched correctly
        assertEquals(true, game.isGameOver());
    }


    @Test
    public void testGameOver() {
        // Simulate matching all pairs
        commands.add("0 0");
        commands.add("0 1");
        commands.add("1 0");
        commands.add("1 1");
        commands.add("2 0");
        commands.add("2 1");
        commands.add("3 0");
        commands.add("3 1");
        commands.add("0 2");
        commands.add("0 3");
        commands.add("1 2");
        commands.add("1 3");
        commands.add("2 2");
        commands.add("2 3");
        commands.add("3 2");
        commands.add("3 3");

        textGameUI = new TextGameUI(game, new Scanner(System.in), commands);
        textGameUI.runGameLoop();

        // Verify that the game is over and the score is correct
        assertEquals(8, game.getScore());
        assertEquals(true, game.isGameOver());
    }
}
