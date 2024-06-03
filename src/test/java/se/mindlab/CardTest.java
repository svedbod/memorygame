package se.mindlab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    public void testCardCreation() {
        Card card = new Card("Red");
        assertEquals("Red", card.getColor());
        assertFalse(card.isFaceUp());
    }

    @Test
    public void testFlipCard() {
        Card card = new Card("Blue");
        card.setFaceUp(true);
        assertTrue(card.isFaceUp());
        card.setFaceUp(false);
        assertFalse(card.isFaceUp());
    }
}
