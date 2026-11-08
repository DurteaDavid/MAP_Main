package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RelationalExpTest {

    private static IExp num(int n) { return new ValueExp(new IntValue(n)); }
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();

    private static class NonIntValueExp implements IExp {
        @Override
        public IValue eval(IDictionary<String, IValue> symTable) {
            return new BoolValue(true);
        }
        @Override
        public String toString() { return "true"; }
    }

    @Test
    void testLT() throws MyException {
        IExp e1 = new RelationalExp(RelOp.LT, num(1), num(5));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.LT, num(10), num(5));
        assertEquals(new BoolValue(false), e2.eval(symTable));
    }

    @Test
    void testLE() throws MyException {
        IExp e1 = new RelationalExp(RelOp.LE, num(1), num(5));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.LE, num(10), num(5));
        assertEquals(new BoolValue(false), e2.eval(symTable));

        IExp e3 = new RelationalExp(RelOp.LE, num(5), num(5));
        assertEquals(new BoolValue(true), e3.eval(symTable));
    }

    @Test
    void testEQ() throws MyException {
        IExp e1 = new RelationalExp(RelOp.EQ, num(5), num(5));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.EQ, num(10), num(5));
        assertEquals(new BoolValue(false), e2.eval(symTable));
    }

    @Test
    void testNE() throws MyException {
        IExp e1 = new RelationalExp(RelOp.NE, num(1), num(5));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.NE, num(5), num(5));
        assertEquals(new BoolValue(false), e2.eval(symTable));
    }

    @Test
    void testGT() throws MyException {
        IExp e1 = new RelationalExp(RelOp.GT, num(5), num(1));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.GT, num(1), num(5));
        assertEquals(new BoolValue(false), e2.eval(symTable));
    }

    @Test
    void testGE() throws MyException {
        IExp e1 = new RelationalExp(RelOp.GE, num(10), num(5));
        assertEquals(new BoolValue(true), e1.eval(symTable));

        IExp e2 = new RelationalExp(RelOp.GE, num(1), num(5));
        assertEquals(new BoolValue(false), e2.eval(symTable));

        IExp e3 = new RelationalExp(RelOp.GE, num(5), num(5));
        assertEquals(new BoolValue(true), e3.eval(symTable));
    }

    @Test
    void firstOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, new ArithExpTest.NonIntValueExp(), num(5));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("First operand is not an integer!", ex.getMessage());
    }

    @Test
    void secondOperandNotIntegerThrows() {
        IExp e = new ArithExp(ArithOp.ADD, num(5), new ArithExpTest.NonIntValueExp());
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("Second operand is not an integer!", ex.getMessage());
    }
}
