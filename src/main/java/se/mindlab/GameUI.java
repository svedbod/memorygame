package se.mindlab;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A graphical user interface based on Swing. This class handles the visual representation of
 * the game board and enables user interaction via mouse clicks. GameUI demonstrates how the
 * game classes can be integrated with a GUI framework.
 */
public class GameUI implements GameUpdateListener {

    private class CardMouseListener extends MouseAdapter {
        private final int row;
        private final int col;
        private final Card card;

        public CardMouseListener(Card card, int row, int col) {
            this.row = row;
            this.col = col;
            this.card = card;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!card.isFaceUp() && game.getBoard().isAllowClicks()) {
                game.playTurn(row, col);
                updateBoard();
            }
        }
    }
    private final ColourMemoryGame game;
    private JFrame frame;
    private JPanel boardPanel;
    private JLabel scoreLabel;
    private JLabel logoLabel;
    private ImageIcon cardBackIcon;
    private ImageIcon logoIcon;
    private ImageIcon overlayIcon;
    private Boolean gameOverHandled = false;

    // enable colorBlindPreference to support colour blind
    private Boolean colorBlindPreferenceOn = false;

    private Color defaultBackgroundColor;

    public GameUI() {
        game = new ColourMemoryGame(this);
        prepareGUI();
    }

    private void loadImages() {
        cardBackIcon = loadImageAsIcon("card_bg.gif");
        logoIcon = loadImageAsIcon("logo.png");
        overlayIcon = loadImageAsIcon("overlay_icon.png");
    }
    private void initializeComponents() {
        scoreLabel = new JLabel("Poäng just nu: 0", JLabel.CENTER);
        logoLabel = new JLabel("", JLabel.CENTER);
        boardPanel = new JPanel(new GridLayout(4, 4));
        defaultBackgroundColor = UIManager.getColor("Panel.background");
        loadImages();
        logoLabel.setIcon(logoIcon);
    }
    private void toggleColorBlindMode(boolean enabled) {
        colorBlindPreferenceOn = enabled;
        updateGameUI(); // Update the UI to reflect this change
    }
    private void setupMenus() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = setupGameMenu();
        JMenu optionsMenu = setupOptionsMenu();
        menuBar.add(gameMenu);
        menuBar.add(optionsMenu);
        frame.setJMenuBar(menuBar);
    }

    private JMenu setupGameMenu() {
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");
        restartItem.addActionListener(e -> restartGame());
        gameMenu.add(restartItem);
        return gameMenu;
    }

    private JMenu setupOptionsMenu() {
        JMenu optionsMenu = new JMenu("Options");
        JCheckBoxMenuItem colorBlindOption = new JCheckBoxMenuItem("Text Mode");
        colorBlindOption.addActionListener(e -> toggleColorBlindMode(colorBlindOption.isSelected()));
        optionsMenu.add(colorBlindOption);
        return optionsMenu;
    }

    private void layoutComponents() {
        frame.add(logoLabel, BorderLayout.NORTH);
        frame.add(scoreLabel, BorderLayout.SOUTH);
        frame.add(boardPanel, BorderLayout.CENTER);
    }

    private void prepareGUI() {
        frame = new JFrame("Colour Memory Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(320, 480);
        frame.setLayout(new BorderLayout());
        setupMenus();
        initializeComponents();
        layoutComponents();
        prepareBoard();
        frame.setVisible(true);
    }

    private JLabel createCardLabel(ImageIcon icon) {
        JLabel label = new JLabel(icon);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(80, 100));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return label;
    }

    private void prepareBoard() {
        boardPanel.removeAll();  // Clear any existing components including listeners from the board panel
        int componentCounter = 0;  // Initialize a counter to track the number of components processed
        for (int row = 0; row < 4; row++) {  // Iterate over each row
            for (int col = 0; col < 4; col++) {  // Iterate over each column
                Card card = game.getBoard().getCard(row, col);  // Retrieve the card at the current row and column
                JLabel cardView;  // Declare the JLabel to hold the card's visual representation
                if (componentCounter < boardPanel.getComponentCount()) {
                    // If a component already exists at this position, reuse it
                    cardView = (JLabel) boardPanel.getComponent(componentCounter);
                } else {
                    // If no component exists at row, col, create a new one and add it to the board
                    cardView = createCardLabel(cardBackIcon);
                    boardPanel.add(cardView);
                    addCardListener(cardView, card, row, col);
                }
                updateCardInUI(cardView, card);  // Update the appearance of the card based on its state
                componentCounter++;  // Increment the counter after processing each component
            }
        }
        boardPanel.revalidate();  // Revalidate the layout of the board panel
        boardPanel.repaint();  // Repaint the board panel to update the UI
    }


    private void addCardListener(JLabel cardLabel, Card card, int row, int col) {
        cardLabel.addMouseListener(new CardMouseListener(card, row, col));
    }

    private ImageIcon loadImageAsIcon(String fileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            Image img = ImageIO.read(is);
            return new ImageIcon(img);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void updateGameUI() {
        SwingUtilities.invokeLater(() -> {
            updateBoard();
            scoreLabel.setText((game.isGameOver()?"Spelet är över! Din poäng blev: ":"Poäng just nu: ") + game.getScore());
            if (game.isGameOver() && !gameOverHandled) {
                gameOverHandled = true; // Ensure dialog is only shown once
                showGameOverDialog();
            }
        });
    }

    private void updateBoard() {
        Component[] components = boardPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            JLabel label = (JLabel) components[i];
            Card card = game.getBoard().getCard(i / 4, i % 4);
            updateCardInUI(label, card);
        }
    }

    private void updateCardInUI(JLabel cardView, Card card) {
        if (card.isFaceUp()) {
            cardView.setIcon(null); // Visual representation for non-colorblind mode
            cardView.setBackground(getColorFromString(card.getColor()));
            if(colorBlindPreferenceOn) cardView.setText(card.getColor()); // Accessibility text for colorblind users
        } else {
            cardView.setIcon(cardBackIcon);
            cardView.setBackground(defaultBackgroundColor); // make sure we hide colours if face down
            cardView.setText(""); // Clear text when card is not visible
        }
        cardView.setEnabled(true);

        // Add or remove the overlay icon based on selection
        if (card.isSelected()) {
            cardView.setIcon(overlayIcon); // Set the overlay icon
        } else {
            if (!card.isFaceUp()) {
                cardView.setIcon(cardBackIcon); // Reset to card back if the card is not face up
            } else {
                cardView.setIcon(null); // Clear the icon if the card is face up and not selected
            }
        }
    }

    private void showGameOverDialog() {
        int response = JOptionPane.showConfirmDialog(frame,
                "Spelet är över! Din poäng: " + game.getScore() + ". Vill du spela igen?",
                "Spelet är över",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            frame.dispose(); // Close the frame and exit the application
        }
    }
    private Color getColorFromString(String colorStr) {
        return switch (colorStr.toLowerCase()) {
            case "cyan" -> Color.CYAN;
            case "magenta" -> Color.MAGENTA;
            case "red" -> Color.RED;
            case "blue" -> Color.BLUE;
            case "green" -> Color.GREEN;
            case "yellow" -> Color.YELLOW;
            case "orange" -> Color.ORANGE;
            case "purple" -> new Color(128, 0, 128); // Java doesn't have a predefined purple
            case "black" -> Color.BLACK;
            case "gray" -> Color.GRAY;
            default -> Color.BLACK; // Default case to handle unexpected colors
        };
    }

    private void restartGame() {
        game.reset(); // Reset the game logic
        gameOverHandled=false;
        scoreLabel.setText("Poäng just nu: " + game.getScore());
        boardPanel.removeAll(); // Remove all components from the board panel
        boardPanel.revalidate(); // Revalidate the panel layout
        boardPanel.repaint(); // Repaint the panel to clear old drawings
        prepareBoard(); // Prepare the board again with new cards
        frame.revalidate(); // Revalidate the frame to ensure layout updates correctly
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameUI();
            }
        });
    }
}
