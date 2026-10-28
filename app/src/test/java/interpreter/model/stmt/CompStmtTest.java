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

class CompStmtTest {

    private PrgState state;
    private MyStack<IStmt> exeStack;

    @BeforeEach
    void setUp() throws MyException {
        exeStack = new MyStack<>();
        IDictionary<String, interpreter.model.value.IValue> symTable = new MyDictionary<>();
        state = new PrgState(exeStack, symTable, new MyList<>(), new NopStmt());
    }

    @Test
    void testComStmtExecutionRuleS1() throws MyException {
        exeStack.pop();
        IStmt stmt1 = new NopStmt();
        IStmt stmt2 = new AssignStmt("x", new interpreter.model.exp.ValueExp(new interpreter.model.value.IntValue(5)));

        IStmt comStmt = new CompStmt(stmt1, stmt2);

        assertTrue(exeStack.isEmpty(), "Stack should be empty before execution");

        PrgState resultState = comStmt.execute(state);

        assertNotNull(resultState);

        assertFalse(exeStack.isEmpty(), "Stack should not be empty after compound statement execution");

        assertEquals(stmt1, exeStack.pop(), "First popped statement from execution stack should be 'first' (stmt1)");
        assertEquals(stmt2, exeStack.pop(), "Second popped statement from execution stack should be 'second' (stmt2)");
        assertTrue(exeStack.isEmpty(), "Stack should be empty again after popping both statements");
    }

    @Test
    void testToString() {
        IStmt stmt1 = new NopStmt();
        IStmt stmt2 = new NopStmt();
        IStmt comStmt = new CompStmt(stmt1, stmt2);

        assertEquals("(" + stmt1 + "; " + stmt2 + ")", comStmt.toString());
    }
}