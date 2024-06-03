package se.mindlab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class ColourMemoryGameTest {

    private ColourMemoryGame game;
    private MockGameUpdateListener listener;

    private static class MockGameUpdateListener implements GameUpdateListener {
        boolean uiUpdated = false;

        @Override
        public void updateGameUI() {
            uiUpdated = true;
        }
    }

    @BeforeEach
    public void setUp() {
        listener = new MockGameUpdateListener();
        game = new ColourMemoryGame(listener);
    }

    @Test
    public void testInitialScoreIsZero() {
        assertEquals(0, game.getScore());
    }

    @Test
    public void testInitialGameIsNotOver() {
        assertFalse(game.isGameOver());
    }

    @Test
    public void testPlayTurnFlipsCard() {
        game.playTurn(0, 0);
        assertTrue(game.getBoard().getCard(0, 0).isFaceUp());
    }

    @Test
    public void testMatchingPairIncrementsScore() {
        Card firstCard = game.getBoard().getCard(0, 0);
        Card secondCard = null;

        // Find the matching card
        outerLoop:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) continue; // Skip the first card
                Card card = game.getBoard().getCard(i, j);
                if (card.getColor().equals(firstCard.getColor())) {
                    secondCard = card;
                    game.playTurn(i, j);
                    break outerLoop;
                }
            }
        }

        assertNotNull(secondCard); // Ensure we found a matching card
        game.playTurn(0, 0);

        assertEquals(1, game.getScore());
    }

    @Test
    public void testNonMatchingPairDecrementsScore() {
        Card firstCard = game.getBoard().getCard(0, 0);
        Card secondCard = null;

        // Find a non-matching card
        outerLoop:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) continue; // Skip the first card
                Card card = game.getBoard().getCard(i, j);
                if (!card.getColor().equals(firstCard.getColor())) {
                    secondCard = card;
                    game.playTurn(i, j);
                    break outerLoop;
                }
            }
        }

        assertNotNull(secondCard); // Ensure we found a non-matching card
        game.playTurn(0, 0);

        assertEquals(-1, game.getScore());
    }

    @Test
    public void testGameOverCondition() {
        ColourMemoryGame game = new ColourMemoryGame(new GameUpdateListener() {
            @Override
            public void updateGameUI() {
                // Dummy listener for testing purposes
            }
        });

        // Simulate matching all pairs
        String[] colors = {"Red", "Blue", "Green", "Yellow", "Purple", "Cyan", "Orange", "Magenta"};
        for (String color : colors) {
            // Find the two cards of the same color and match them
            boolean firstFound = false;
            int firstRow = -1, firstCol = -1;
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    Card card = game.getBoard().getCard(row, col);
                    if (card.getColor().equals(color)) {
                        if (!firstFound) {
                            firstFound = true;
                            firstRow = row;
                            firstCol = col;
                        } else {
                            game.playTurn(firstRow, firstCol);
                            game.playTurn(row, col);
                        }
                    }
                }
            }
        }

        // Now the game should be over
        assertTrue(game.isGameOver());
    }


    @Test
    public void testResetGame() {
        game.playTurn(0, 0);
        game.reset();

        assertEquals(0, game.getScore());
        assertFalse(game.isGameOver());
        assertFalse(game.getBoard().getCard(0, 0).isFaceUp());
    }
}
