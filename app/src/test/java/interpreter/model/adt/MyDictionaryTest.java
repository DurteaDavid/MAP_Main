package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {

    private IDictionary<String, Integer> dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new MyDictionary<>();
    }

    @Test
    void testIsDefined() throws ADTException {
        assertFalse(dictionary.isDefined("key1"), "Key should not be defined initially");
        dictionary.put("key1", 10);
        assertTrue(dictionary.isDefined("key1"), "Key should be defined after put");
    }

    @Test
    void testLookupSuccess() throws ADTException {
        dictionary.put("key1", 100);
        assertEquals(100, dictionary.lookup("key1"), "Lookup should return the correct value");
    }

    @Test
    void testLookupNotFound() {
        assertThrows(ADTException.class, () -> dictionary.lookup("nonexistent"),
                "Lookup on a non-existent key should throw ADTException");
    }

    @Test
    void testPutSuccess() throws ADTException {
        dictionary.put("key1", 50);
        assertTrue(dictionary.isDefined("key1"));
        assertEquals(50, dictionary.lookup("key1"));
    }

    @Test
    void testPutAlreadyExists() throws ADTException {
        dictionary.put("key1", 50);
        assertThrows(ADTException.class, () -> dictionary.put("key1", 100),
                "Putting an existing key should throw ADTException");
    }

    @Test
    void testUpdateSuccess() throws ADTException {
        dictionary.put("key1", 50);
        dictionary.update("key1", 75);
        assertEquals(75, dictionary.lookup("key1"), "Value should be updated successfully");
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(ADTException.class, () -> dictionary.update("nonexistent", 75),
                "Updating a non-existent key should throw ADTException");
    }
}