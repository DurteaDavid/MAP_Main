package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

public record VarExp(String varName) implements IExp {
    @Override public IValue eval(IDictionary symTable) throws MyException {
        return (IValue) symTable.lookup(varName);
    }
    @Override public String toString() { return varName; }
}
