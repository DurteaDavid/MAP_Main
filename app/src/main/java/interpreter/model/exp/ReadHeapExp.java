package interpreter.model.exp;

import com.sun.jdi.Value;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.type.IType;
import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

// TODO: ReadHeapExp(IExp exp) – E6, toString "rH(" + exp + ")"
// the value of exp must be a RefValue; its address must be in the heap

public record ReadHeapExp(IExp exp) implements IExp {
    @Override
    public IValue eval (IDictionary< String, IValue> symTable, IHeap heap) throws MyException {
        if (!(exp.eval(symTable,heap) instanceof RefValue(int addr, IType type)))
            throw new ExpressionException("ReadHeap: " + exp.toString() + " is not a RefValue!");
        if(!heap.isDefined(addr))
            throw new ExpressionException("Address " + addr + " is not in the heap!");
        return heap.get(addr);
    }
    @Override
    public String toString() { return "rH(" + exp + ")"; }
}
