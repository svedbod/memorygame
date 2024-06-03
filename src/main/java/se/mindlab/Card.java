package se.mindlab;

/**
 * Represents a single game card with a color and a status indicating whether the card is
 * face up or face down. The Card class and its attributes provide a simple and clear
 * representation of each game card, making the code easy to read and maintain.
 */
public class Card {
    private final String color;
    private boolean isFaceUp;

    private boolean selected;

    public Card(String color) {
        this.color = color;
        this.isFaceUp = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getColor() {
        return color;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }
}
