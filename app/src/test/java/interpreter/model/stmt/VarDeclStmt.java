package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VarDeclStmtTest {

    private PrgState state;
    private IDictionary<String, interpreter.model.value.IValue> symTable;

    @BeforeEach
    void setUp() throws MyException {
        symTable = new MyDictionary<>();
        state = new PrgState(new MyStack<>(), symTable, new MyList<>(), new NopStmt());
    }

    @Test
    void testVarDeclSuccess() throws MyException {
        IStmt varDecl = new VarDeclStmt("x", new IntType());

        assertFalse(symTable.isDefined("x"), "Variable should not be defined initially");

        PrgState resultState = varDecl.execute(state);

        assertNotNull(resultState);
        assertTrue(symTable.isDefined("x"), "Variable 'x' should be defined in the symbol table after declaration");

        assertEquals(new IntValue(0), symTable.lookup("x"), "Declared variable should have its type's default value");
    }

    @Test
    void testVarDeclAlreadyDeclaredThrows() throws MyException {
        symTable.put("x", new IntValue(5));

        IStmt varDecl = new VarDeclStmt("x", new IntType());

        var ex = assertThrows(StatementException.class, () -> varDecl.execute(state));
        assertTrue(ex.getMessage().contains("is already declared!"));
    }

    @Test
    void testToString() {
        IStmt varDecl = new VarDeclStmt("x", new IntType());
        assertNotNull(varDecl.toString());
    }
}