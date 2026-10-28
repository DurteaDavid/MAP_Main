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

class ArithExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();
    private static IExp num(int n) { return new ValueExp(new IntValue(n)); }

    private static class NonIntValueExp implements IExp {
        @Override
        public IValue eval(IDictionary<String, IValue> symTable) {
            return new BoolValue(true);
        }
        @Override
        public String toString() { return "true"; }
    }

    @Test
    void respectsOperatorPrecedenceEncodedInTheTree() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(2),
                new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), e.eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExp e = new ArithExp(ArithOp.DIV, num(1), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("Cannot divide by 0!", ex.getMessage());
    }

    @Test
    void subtractionWorks() throws MyException {
        IExp e = new ArithExp(ArithOp.SUB, num(10), num(4));
        assertEquals(new IntValue(6), e.eval(symTable));
    }

    @Test
    void multiplicationWorks() throws MyException {
        IExp e = new ArithExp(ArithOp.MUL, num(6), num(7));
        assertEquals(new IntValue(42), e.eval(symTable));
    }

    @Test
    void divisionWorks() throws MyException {
        IExp e = new ArithExp(ArithOp.DIV, num(20), num(4));
        assertEquals(new IntValue(5), e.eval(symTable));
    }

    @Test
    void firstOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, new NonIntValueExp(), num(5));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("First operand is not an integer!", ex.getMessage());
    }

    @Test
    void secondOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, num(5), new NonIntValueExp());
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("Second operand is not an integer!", ex.getMessage());
    }

    @Test
    void testToString() {
        IExp e = new ArithExp(ArithOp.ADD, num(2), num(3));
        assertEquals("(2 + 3)", e.toString());
    }
}