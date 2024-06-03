package se.mindlab;
import java.util.List;
import java.util.Scanner;

public class TextGameUI implements GameUpdateListener {
    private final ColourMemoryGame game;
    private final Scanner scanner;
    private final List<String> commands;
    private int commandIndex;

    public TextGameUI() {
        this.game = new ColourMemoryGame(this);
        this.scanner = new Scanner(System.in);
        this.commands = null; // Default mode, no commands injected
        this.commandIndex = 0;
        runGameLoop();
    }

    public TextGameUI(ColourMemoryGame game, Scanner scanner, List<String> commands) {
        this.game = game;
        this.scanner = scanner;
        this.commands = commands;
        this.commandIndex = 0;
        runGameLoop(); // Run game loop during initialization for testing
    }

    void runGameLoop() {
        while (!game.isGameOver()) {
            System.out.println("Current Board:");
            printBoard();
            System.out.println("Enter your move (row column, for example: 0 0):");

            int row, col;

            if (commands != null && commandIndex < commands.size()) {
                String input = commands.get(commandIndex++);
                System.out.println(input); // For test visibility
                Scanner inputScanner = new Scanner(input);
                row = inputScanner.nextInt();
                col = inputScanner.nextInt();
            } else {
                if (scanner.hasNextInt()) {
                    row = scanner.nextInt();
                    col = scanner.nextInt();
                } else {
                    break; // Exit condition for interactive mode
                }
            }

            game.playTurn(row, col);
        }

        System.out.println("Game Over! Your score: " + game.getScore());
        if (commands == null) { // Only ask to play again in interactive mode
            System.out.println("Play Again? (yes/no)");
            String response = scanner.next();
            if (response.equalsIgnoreCase("yes")) {
                game.reset();
                runGameLoop();
            }
        }
    }

    @Override
    public void updateGameUI() {
        printBoard();
        System.out.println("Score: " + game.getScore());
    }

    private void printBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Card card = game.getBoard().getCard(i, j);
                if (card.isFaceUp()) {
                    System.out.print(card.getColor().substring(0, 2) + " ");
                } else {
                    System.out.print("XX ");
                }
            }
            System.out.println();
        }
    }

    public String getBoardAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Card card = game.getBoard().getCard(i, j);
                if (card.isFaceUp()) {
                    sb.append(card.getColor().substring(0, 2)).append(" ");
                } else {
                    sb.append("XX ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        new TextGameUI();
    }
}
