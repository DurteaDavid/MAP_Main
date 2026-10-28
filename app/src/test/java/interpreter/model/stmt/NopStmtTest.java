package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NopStmtTest {

    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        IDictionary<String, interpreter.model.value.IValue> symTable = new MyDictionary<>();
        state = new PrgState(new MyStack<>(), symTable, new MyList<>(), new NopStmt());
    }

    @Test
    void testNopStmtExecutionRuleS6() throws MyException {
        IStmt nop = new NopStmt();

        PrgState resultState = nop.execute(state);

        assertNotNull(resultState, "Result state should not be null");
        assertEquals(state, resultState, "NopStmt should return the exact same program state object");
    }

    @Test
    void testToString() {
        IStmt nop = new NopStmt();
        assertNotNull(nop.toString());
    }
}