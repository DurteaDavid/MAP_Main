package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record LogicExp(LogicOp op, IExp left, IExp right) implements IExp {
    @Override
    public IValue eval (IDictionary < String, IValue> symTable) throws MyException {
        if (!(left.eval(symTable) instanceof BoolValue(boolean n1)))
            throw new ExpressionException("First operand is not a boolean value!");
        if (!(right.eval(symTable) instanceof BoolValue(boolean n2)))
            throw new ExpressionException("Second operand is not a boolean value!");
        return switch (op) {
            case AND -> new BoolValue(n1 && n2);
            case OR -> new BoolValue(n1 || n2);
        };
    }
    @Override public String toString() { return "(" + left + " " + op + " " + right + ")"; }
}
