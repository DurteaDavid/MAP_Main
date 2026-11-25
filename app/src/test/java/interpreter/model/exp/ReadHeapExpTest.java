package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReadHeapExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();
    private final IHeap heap = new MyHeap();

    @Test
    void throwsIfNotRefValue() throws MyException {
        IExp nonRef = new ValueExp(new IntValue(1));
        ReadHeapExp readExp = new ReadHeapExp(nonRef);

        assertThrows(ExpressionException.class, () -> readExp.eval(symTable, heap));
    }

    @Test
    void checkWorksNormally() throws MyException {
        int addr = heap.allocate(new IntValue(42));
        IExp refExp = new ValueExp(new RefValue(addr, new IntType()));
        ReadHeapExp readExp = new ReadHeapExp(refExp);

        assertEquals(new IntValue(42), readExp.eval(symTable, heap));
    }

    @Test
    void throwsIfNotAllocatedAddress() {
        IExp refExp = new ValueExp(new RefValue(5, new IntType()));
        ReadHeapExp readHeap = new ReadHeapExp(refExp);

        assertThrows(ExpressionException.class,() -> readHeap.eval(symTable, heap));
    }
}
