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

class IfStmtTest {

    private PrgState state;
    private MyStack<IStmt> exeStack;
    private IDictionary<String, interpreter.model.value.IValue> symTable;

    @BeforeEach
    void setUp() throws MyException {
        exeStack = new MyStack<>();
        symTable = new MyDictionary<>();
        state = new PrgState(exeStack, symTable, new MyList<>(), new NopStmt());
    }

    @Test
    void testIfStmtTrueBranch() throws MyException {
        exeStack.pop();
        IExp cond = new ValueExp(new BoolValue(true));
        IStmt thenStmt = new NopStmt();
        IStmt elseStmt = new AssignStmt("x", new ValueExp(new IntValue(10)));

        IStmt ifStmt = new IfStmt(cond, thenStmt, elseStmt);

        PrgState resultState = ifStmt.execute(state);

        assertNotNull(resultState);
        assertFalse(exeStack.isEmpty(), "Execution stack should have the then-branch pushed");
        assertEquals(thenStmt, exeStack.pop(), "Popped statement should be the then-branch");
        assertTrue(exeStack.isEmpty(), "Stack should be empty now");
    }

    @Test
    void testIfStmtFalseBranch() throws MyException {
        exeStack.pop();
        IExp cond = new ValueExp(new BoolValue(false));
        IStmt thenStmt = new NopStmt();
        IStmt elseStmt = new AssignStmt("x", new ValueExp(new IntValue(10)));

        IStmt ifStmt = new IfStmt(cond, thenStmt, elseStmt);

        PrgState resultState = ifStmt.execute(state);

        assertNotNull(resultState);
        assertFalse(exeStack.isEmpty(), "Execution stack should have the else-branch pushed");
        assertEquals(elseStmt, exeStack.pop(), "Popped statement should be the else-branch");
        assertTrue(exeStack.isEmpty(), "Stack should be empty now");
    }

    @Test
    void testIfStmtNonBooleanConditionThrows() {
        IExp cond = new ValueExp(new IntValue(1));
        IStmt thenStmt = new NopStmt();
        IStmt elseStmt = new NopStmt();

        IStmt ifStmt = new IfStmt(cond, thenStmt, elseStmt);

        var ex = assertThrows(StatementException.class, () -> ifStmt.execute(state));
        assertTrue(ex.getMessage().contains("is not boolean!"));
    }

    @Test
    void testToString() {
        IExp cond = new ValueExp(new BoolValue(true));
        IStmt thenStmt = new NopStmt();
        IStmt elseStmt = new NopStmt();

        IStmt ifStmt = new IfStmt(cond, thenStmt, elseStmt);

        assertEquals("If true then " + thenStmt + " else " + elseStmt, ifStmt.toString());
    }
}