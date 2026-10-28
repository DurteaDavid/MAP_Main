package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IList;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.IExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrintStmtTest {

    private PrgState state;
    private IList<IValue> outList;

    @BeforeEach
    void setUp() throws MyException {
        outList = new MyList<>();
        IDictionary<String, IValue> symTable = new MyDictionary<>();
        state = new PrgState(new MyStack<>(), symTable, outList, new NopStmt());
    }

    @Test
    void testPrintStmtExecutionRuleS4() throws MyException {
        IExp expr = new ValueExp(new IntValue(42));
        IStmt printStmt = new PrintStmt(expr);

        assertEquals(0, outList.size(), "Output list should be empty initially");

        PrgState resultState = printStmt.execute(state);
        assertNotNull(resultState);

        assertEquals(1, outList.size(), "Output list size should be 1 after printing");
        assertEquals(new IntValue(42), outList.get(0), "The printed value should be appended to the output list");
    }

    @Test
    void testToString() {
        IExp expr = new ValueExp(new IntValue(100));
        IStmt printStmt = new PrintStmt(expr);

        assertEquals("print(100)", printStmt.toString());
    }
}