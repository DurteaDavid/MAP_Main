package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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

    @Test
    void testRemove() throws ADTException {
        dictionary.put("key", 2);
        dictionary.remove("key");
        assertFalse(dictionary.isDefined("key"));
    }

    @Test
    void testRemoveThrows() throws ADTException {
        assertThrows(ADTException.class, () -> dictionary.remove("key"));
    }

    @Test
    void testToMap() throws ADTException {
        dictionary.put("key1",1);
        dictionary.put("key2",2);

        Map<String, Integer> result = dictionary.toMap();
        assertEquals(2, result.size());
        assertEquals(1, result.get("key1"));
        assertEquals(2, result.get("key2"));
    }

    @Test
    void testToMapImmutable() throws ADTException {
        dictionary.put("key1",1);

        Map<String, Integer> result = dictionary.toMap();
        assertThrows(UnsupportedOperationException.class, () -> result.put("key2",2));
    }
}