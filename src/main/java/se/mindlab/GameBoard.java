package se.mindlab;

public class GameBoard {
    private final Card[][] cards = new Card[4][4];
    private boolean allowClicks = true; // Variable to att check if click is allowed

    public GameBoard() {
        initializeCards();
    }

    public boolean isAllowClicks() {
        return allowClicks;
    }

    public void setAllowClicks(boolean allowClicks) {
        this.allowClicks = allowClicks;
    }

    public void initializeCards() {
        initializeCards(null); // Call the overloaded method with null to use random positions
    }

    public void initializeCards(String[] predefinedColors) {
        boolean isPredefined = predefinedColors != null && predefinedColors.length == 16;
        if (!isPredefined) {
            predefinedColors = new String[]{
                    "Red", "Red", "Blue", "Blue",
                    "Green", "Green", "Yellow", "Yellow",
                    "Purple", "Purple", "Cyan", "Cyan",
                    "Orange", "Orange", "Magenta", "Magenta"
            };
        }

        int index = 0;
        for (String color : predefinedColors) {
            cards[index / 4][index % 4] = new Card(color);
            index++;
        }

        // Shuffle cards only if predefinedColors were not provided
        if (!isPredefined) {
            shuffleCards();
        }
    }

    /**
     * Using the Fisher-Yates Shuffle algorithm,
     * Counting down to ensure each permutation has equal likelihood
     * See <a href="https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">...</a>
     * and for example
     * <a href="https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/">...</a>
     */
    void shuffleCards() {
        // Iterate through all rows of the cards array
        for (int row = cards.length - 1; row > 0; row--) {
            // Iterate through all columns of the current row
            for (int col = cards[row].length - 1; col > 0; col--) {
                // Generate random row index to swap with
                int swapRow = (int) (Math.random() * (row + 1));
                // Generate random column index to swap with
                int swapCol = (int) (Math.random() * (cards[swapRow].length));

                // Perform the swap
                Card temp = cards[row][col];
                cards[row][col] = cards[swapRow][swapCol];
                cards[swapRow][swapCol] = temp;
            }
        }
    }


    public Card getCard(int x, int y) {
        return cards[x][y];
    }
}
