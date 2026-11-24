package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() { heap = new MyHeap(); }

    @Test
    void allocateReturns() throws ADTException {
        heap.allocate(new IntValue(1));
        heap.allocate(new IntValue(2));
        heap.allocate(new IntValue(3));

        assertTrue(heap.isDefined(1));
        assertTrue(heap.isDefined(2));
        assertTrue(heap.isDefined(3));

        assertEquals(new IntValue(1), heap.get(1));
        assertEquals(new IntValue(2),heap.get(2));
        assertEquals(new IntValue(3),heap.get(3));
    }

    @Test
    void getThrows() throws ADTException {
        assertThrows(ADTException.class, () -> heap.get(1));

        heap.allocate(new IntValue(1));
        assertEquals(new IntValue(1), heap.get(1));
    }

    @Test
    void updateThrows() throws ADTException {
        assertThrows(ADTException.class, () -> heap.update(1,new BoolValue(true)));

        heap.allocate(new BoolValue(true));
        assertDoesNotThrow(() -> heap.update(1,new BoolValue(false)));
    }
}
