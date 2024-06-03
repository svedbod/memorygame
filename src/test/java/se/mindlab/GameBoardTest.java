package se.mindlab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private GameBoard board;

    @BeforeEach
    public void setUp() {
        board = new GameBoard();
    }

    @Test
    public void testInitialBoardSetup() {
        assertNotNull(board.getCard(0, 0));
        assertNotNull(board.getCard(3, 3));
    }

    @Test
    public void testCardPairs() {
        boolean allPairsMatched = true;
        outerLoop:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Card firstCard = board.getCard(i, j);
                boolean pairFound = false;
                for (int m = 0; m < 4; m++) {
                    for (int n = 0; n < 4; n++) {
                        if (i == m && j == n) continue;
                        Card secondCard = board.getCard(m, n);
                        if (firstCard.getColor().equals(secondCard.getColor())) {
                            pairFound = true;
                            break;
                        }
                    }
                    if (pairFound) break;
                }
                if (!pairFound) {
                    allPairsMatched = false;
                    break outerLoop;
                }
            }
        }
        assertTrue(allPairsMatched);
    }

    @Test
    public void testShuffleBoard() {
        Card[][] initialCards = new Card[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                initialCards[i][j] = board.getCard(i, j);
            }
        }
        board.shuffleCards();
        boolean boardsAreDifferent = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!initialCards[i][j].equals(board.getCard(i, j))) {
                    boardsAreDifferent = true;
                    break;
                }
            }
            if (boardsAreDifferent) break;
        }
        assertTrue(boardsAreDifferent);
    }

    @Test
    public void testAllowClicks() {
        assertTrue(board.isAllowClicks());
        board.setAllowClicks(false);
        assertFalse(board.isAllowClicks());
    }
}
