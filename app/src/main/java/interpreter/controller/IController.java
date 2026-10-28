package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

public interface IController {
    PrgState oneStep(PrgState state) throws MyException;
    void allStep() throws MyException;
}