package se.mindlab;
import javax.swing.Timer;

/**
 * Handles the game's rules and logic. This class is responsible for managing the player's moves,
 * keeping track of the score, and determining if the game is over. ColourMemoryGame uses a
 * GameUpdateListener to update the user interface after each move, creating a loose coupling
 * between the game logic and the user interface.
 */
public class ColourMemoryGame {
    private final GameBoard board;
    private int score;
    private Card currentSelected;
    private int matchedPairs = 0; // Counter for matched pairs
    private final GameUpdateListener listener;

    public ColourMemoryGame(GameUpdateListener listener) {
        this(listener, null);
    }

    public ColourMemoryGame(GameUpdateListener listener, String[] predefinedColors) {
        this.board = new GameBoard();
        this.score = 0;
        this.currentSelected = null;
        this.listener = listener;
        if (predefinedColors != null) {
            board.initializeCards(predefinedColors);
        } else {
            board.initializeCards();
        }
    }

    public void playTurn(int x, int y) {
        if (board.getCard(x, y).isFaceUp() || !board.isAllowClicks()) return; // No action if the card is already face up or clicks are disabled

        Card selectedCard = board.getCard(x, y);
        selectedCard.setFaceUp(true); // Show the card immediately
        selectedCard.setSelected(true); // Set the card as selected
        listener.updateGameUI(); // Update UI to reflect the flipped card immediately

        if (currentSelected == null) {
            currentSelected = selectedCard;
        } else {
            if (currentSelected.getColor().equals(selectedCard.getColor())) {
                incrementScore();
                matchedPairs++;
                currentSelected.setSelected(false); // Unmark both cards as selected
                selectedCard.setSelected(false);
                currentSelected = null;
                if (isGameOver()) {
                    listener.updateGameUI();
                }
            } else {
                decrementScore();
                board.setAllowClicks(false); // Disable further clicks until cards are reset
                // Temporarily show the cards and then hide them
                Timer timer = getTimer(selectedCard);
                timer.start();
            }
        }
    }

    private Timer getTimer(Card selectedCard) {
        Timer timer = new Timer(2000, e -> {
            currentSelected.setFaceUp(false);
            selectedCard.setFaceUp(false);
            currentSelected.setSelected(false);
            selectedCard.setSelected(false);
            currentSelected = null; // Reset for the next turn
            listener.updateGameUI(); // Update UI after cards are flipped back
            board.setAllowClicks(true); // Re-enable clicks after cards are flipped back
        });
        timer.setRepeats(false);
        return timer;
    }

    public boolean isGameOver() {
        // Assuming 16 cards, so 8 pairs
        int totalPairs = 8;
        return matchedPairs == totalPairs;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void incrementScore() {
        score++;
    }

    public void decrementScore() {
        score--;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
        matchedPairs = 0;
        currentSelected = null;
        board.initializeCards();
    }
}
