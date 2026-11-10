package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

import java.util.List;

public interface IRepository {
    PrgState getCrtPrg();
    void add(PrgState prg);
    List<PrgState> getPrgList();
    void logPrgStateExec() throws MyException;
}