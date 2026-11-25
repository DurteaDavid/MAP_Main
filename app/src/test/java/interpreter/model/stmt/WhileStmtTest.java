package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTest {

    private MyDictionary<String, interpreter.model.value.IValue> symTable;
    private MyHeap heap;
    private MyStack<IStmt> exeStack;
    private PrgState state;

    @BeforeEach
    void setUp() throws ADTException {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
        exeStack = new MyStack<>();
        state = new PrgState(
                exeStack,
                symTable,
                new MyList<>(),
                new MyDictionary<>(),
                heap,
                new NopStmt()
        );
        exeStack.pop();
    }

    @Test
    void pushesBodyThenSelfWhenConditionIsTrue() throws MyException {
        IExp cond = new ValueExp(new BoolValue(true));
        IStmt body = new NopStmt();
        WhileStmt whileStmt = new WhileStmt(cond, body);

        PrgState result = whileStmt.execute(state);

        assertSame(state, result);
        assertEquals(body, exeStack.pop());
        assertEquals(whileStmt, exeStack.pop());
        assertTrue(exeStack.isEmpty());
    }

    @Test
    void doesNothingWhenConditionIsFalse() throws MyException {
        IExp cond = new ValueExp(new BoolValue(false));
        IStmt body = new NopStmt();
        WhileStmt whileStmt = new WhileStmt(cond, body);

        PrgState result = whileStmt.execute(state);

        assertSame(state, result);
        assertTrue(exeStack.isEmpty());
    }

    @Test
    void throwsWhenConditionIsNotBoolValue() {
        IExp nonBoolCond = new ValueExp(new IntValue(1));
        WhileStmt whileStmt = new WhileStmt(nonBoolCond, new NopStmt());

        assertThrows(StatementException.class, () -> whileStmt.execute(state));
        assertTrue(exeStack.isEmpty());
    }

    @Test
    void toStringFormatsAsExpected() {
        WhileStmt whileStmt = new WhileStmt(new ValueExp(new BoolValue(true)), new NopStmt());
        assertEquals("(while (true) nop)", whileStmt.toString());
    }
}