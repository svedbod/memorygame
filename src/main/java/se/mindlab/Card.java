package se.mindlab;

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
