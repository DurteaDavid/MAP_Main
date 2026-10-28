package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {

    private IList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void testAddAndSize() throws ADTException {
        assertEquals(0, list.size(), "Initial list size should be 0");

        list.add("apple");
        assertEquals(1, list.size(), "Size should be 1 after adding one element");

        list.add("banana");
        assertEquals(2, list.size(), "Size should be 2 after adding a second element");
    }

    @Test
    void testGetSuccess() throws ADTException {
        list.add("first");
        list.add("second");

        assertEquals("first", list.get(0), "Element at index 0 should be 'first'");
        assertEquals("second", list.get(1), "Element at index 1 should be 'second'");
    }

    @Test
    void testGetEmptyListException() {
        assertThrows(ADTException.class, () -> list.get(0),
                "Getting from an empty list should throw ADTException");
    }

    @Test
    void testGetOutOfBoundsNegativeIndex() throws ADTException {
        list.add("item");
        assertThrows(ADTException.class, () -> list.get(-1),
                "Getting with a negative index should throw ADTException");
    }

    @Test
    void testGetOutOfBoundsTooLargeIndex() throws ADTException {
        list.add("item");
        assertThrows(ADTException.class, () -> list.get(1),
                "Getting with an index equal to size should throw ADTException (out of bounds)");
        assertThrows(ADTException.class, () -> list.get(5),
                "Getting with an index greater than size should throw ADTException");
    }
}