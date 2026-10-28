package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VarExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void testEvalExistingVariable() throws MyException {
        symTable.put("x", new IntValue(42));

        IExp varExp = new VarExp("x");
        IValue result = varExp.eval(symTable);

        assertEquals(new IntValue(42), result, "Eval should return the correct value associated with the variable name");
    }

    @Test
    void testEvalUndefinedVariableThrowsException() {
        IExp varExp = new VarExp("y");

        var ex = assertThrows(MyException.class, () -> varExp.eval(symTable));
        assertEquals("Key does not exist!", ex.getMessage());
    }

    @Test
    void testToString() {
        IExp varExp = new VarExp("counter");
        assertEquals("counter", varExp.toString());
    }
}