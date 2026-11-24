package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
import interpreter.model.adt.IHeap;

public record ValueExp(IValue val) implements IExp{
    @Override public IValue eval(IDictionary symTable, IHeap heap) throws ExpressionException {
        return val;
    }
    @Override public String toString() { return val.toString(); }

}
