package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteHeapStmtTest {

    private IDictionary<String, IValue> symTable;
    private IHeap heap;
    private PrgState state;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
        state = new PrgState(
                new MyStack<>(),
                symTable,
                new MyList<>(),
                new MyDictionary<>(),
                heap,
                new NopStmt()
        );
    }

    @Test
    void updatesHeapValueWhenValid() throws MyException {
        int addr = heap.allocate(new IntValue(10));
        symTable.put("v", new RefValue(addr, new IntType()));

        IStmt stmt = new WriteHeapStmt("v", new ValueExp(new IntValue(99)));
        PrgState result = stmt.execute(state);

        assertSame(state, result);
        assertEquals(new IntValue(99), heap.get(addr));
        assertEquals(new RefValue(addr, new IntType()), symTable.lookup("v"));
    }

    @Test
    void throwsIfVariableNotDeclared() {
        IStmt stmt = new WriteHeapStmt("missingVar", new ValueExp(new IntValue(5)));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void throwsIfVariableIsNotRefValue() throws MyException {
        symTable.put("x", new IntValue(10));

        IStmt stmt = new WriteHeapStmt("x", new ValueExp(new IntValue(20)));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void throwsIfExpressionTypeDoesNotMatchLocationType() throws MyException {
        int addr = heap.allocate(new IntValue(10));
        symTable.put("v", new RefValue(addr, new IntType()));

        IStmt stmt = new WriteHeapStmt("v", new ValueExp(new BoolValue(true)));

        assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals(new IntValue(10), heap.get(addr));
    }

    @Test
    void throwsIfHeapAddressIsNotAllocated() throws MyException {
        int danglingAddress = 999;
        symTable.put("v", new RefValue(danglingAddress, new IntType()));

        IStmt stmt = new WriteHeapStmt("v", new ValueExp(new IntValue(50)));

        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void supportsNestedRefValue() throws MyException {
        int innerAddr = heap.allocate(new IntValue(1));
        int outerAddr = heap.allocate(new RefValue(innerAddr, new RefType(new IntType())));
        symTable.put("p", new RefValue(outerAddr, new RefType(new RefType(new IntType()))));

        RefValue newInnerRef = new RefValue(innerAddr, new RefType(new IntType()));
        IStmt stmt = new WriteHeapStmt("p", new ValueExp(newInnerRef));
        stmt.execute(state);

        assertEquals(newInnerRef, heap.get(outerAddr));
    }

    @Test
    void toStringFormatsAsExpected() {
        IStmt stmt = new WriteHeapStmt("v", new ValueExp(new IntValue(7)));
        assertEquals("wH( v, 7)", stmt.toString());
    }
}