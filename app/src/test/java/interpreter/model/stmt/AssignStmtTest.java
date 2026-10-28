package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssignStmtTest {

    private PrgState state;
    private IDictionary<String, interpreter.model.value.IValue> symTable;

    @BeforeEach
    void setUp() throws MyException {
        symTable = new MyDictionary<>();
        state = new PrgState(new MyStack<>(), symTable, new MyList<>(), new NopStmt());
    }

    @Test
    void testAssignSuccess() throws MyException {
        symTable.put("x", new IntValue(0));

        IExp expr = new ValueExp(new IntValue(10));
        IStmt assign = new AssignStmt("x", expr);

        PrgState resultState = assign.execute(state);

        assertNotNull(resultState);
        assertEquals(new IntValue(10), symTable.lookup("x"));
    }

    @Test
    void testAssignUndeclaredVariableThrows() {
        IExp expr = new ValueExp(new IntValue(5));
        IStmt assign = new AssignStmt("y", expr);

        var ex = assertThrows(StatementException.class, () -> assign.execute(state));
        assertTrue(ex.getMessage().contains("was not declared"));
    }

    @Test
    void testAssignTypeMismatchThrows() throws MyException {
        symTable.put("flag", new IntValue(1));

        IExp expr = new ValueExp(new BoolValue(true));
        IStmt assign = new AssignStmt("flag", expr);

        var ex = assertThrows(StatementException.class, () -> assign.execute(state));
        assertTrue(ex.getMessage().contains("do not match"));
    }

    @Test
    void testToString() {
        IExp expr = new ValueExp(new IntValue(42));
        IStmt assign = new AssignStmt("x", expr);

        assertEquals("x = 42", assign.toString());
    }
}