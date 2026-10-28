package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest {

    private IStack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new MyStack<>();
    }

    @Test
    void testIsEmpty() throws ADTException {
        assertTrue(stack.isEmpty(), "Stack should be empty initially");

        stack.push("item");
        assertFalse(stack.isEmpty(), "Stack should not be empty after push");

        stack.pop();
        assertTrue(stack.isEmpty(), "Stack should be empty again after popping the element");
    }

    @Test
    void testPushAndPopLIFO() throws ADTException {
        stack.push("first");
        stack.push("second");

        // LIFO order: second in should be first out
        assertEquals("second", stack.pop(), "First pop should return the most recently pushed element");
        assertEquals("first", stack.pop(), "Second pop should return the earlier pushed element");
    }

    @Test
    void testPopEmptyStackException() {
        assertThrows(ADTException.class, () -> stack.pop(),
                "Popping from an empty stack should throw ADTException");
    }

    @Test
    void testToString() {
        stack.push("A");
        stack.push("B");

        String result = stack.toString();
        assertNotNull(result, "toString() should not return null");
        assertFalse(result.isEmpty(), "toString() should return a non-empty string representation");
    }
}