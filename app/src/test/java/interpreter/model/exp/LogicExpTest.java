package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

class LogicExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private static IExp bool(boolean b) { return new ValueExp(new BoolValue(b)); }
    private static IExp num(int n) { return new ValueExp(new IntValue(n)); }

    @Test
    void testAndOperation() throws MyException {
        // true AND false -> false
        IExp e1 = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals(new BoolValue(false), e1.eval(symTable));

        // true AND true -> true
        IExp e2 = new LogicExp(LogicOp.AND, bool(true), bool(true));
        assertEquals(new BoolValue(true), e2.eval(symTable));
    }

    @Test
    void testOrOperation() throws MyException {
        // false OR false -> false
        IExp e1 = new LogicExp(LogicOp.OR, bool(false), bool(false));
        assertEquals(new BoolValue(false), e1.eval(symTable));

        // false OR true -> true
        IExp e2 = new LogicExp(LogicOp.OR, bool(false), bool(true));
        assertEquals(new BoolValue(true), e2.eval(symTable));
    }

    @Test
    void firstOperandNotBooleanThrows() {
        // Passing an IntValue instead of BoolValue on the left
        IExp e = new LogicExp(LogicOp.AND, num(1), bool(true));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("First operand is not a boolean value!", ex.getMessage());
    }

    @Test
    void secondOperandNotBooleanThrows() {
        // Passing an IntValue instead of BoolValue on the right
        IExp e = new LogicExp(LogicOp.OR, bool(true), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("Second operand is not a boolean value!", ex.getMessage());
    }

    @Test
    void testToString() {
        IExp e = new LogicExp(LogicOp.AND, bool(true), bool(false));
        assertEquals("(true && false)", e.toString());

    }
}