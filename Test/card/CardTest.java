package card;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test of class Card
 */
public class CardTest {

    private Card card;

    @Before
    public void setUp() throws Exception {
        card = new Card(CardCategory.BASIC, "Test", "TestDescription", "basic_apple.JPG");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getName() throws Exception {
        assertEquals(card.getName(), "Test");
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals(card.getDescription(), "TestDescription");
    }

    @Test
    public void getCategory() throws Exception {
        assertEquals(card.getCategory().value(), CardCategory.BASIC.value());
    }

    @Test
    public void getFilename() throws Exception {
        System.out.println(card.getFilename());
    }


}
