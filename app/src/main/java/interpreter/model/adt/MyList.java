package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T> {
    private final List<T> list = new ArrayList<T>();

    @Override public void add(T v) { list.add(v); }

    @Override public T get(int index) throws ADTException {
        if (list.isEmpty()) throw new ADTException("List is empty!");
        if (index < 0 || index >= list.size()) throw new ADTException("Index is out of bounds!");
        return list.get(index);
    }

    @Override public int size() { return list.size(); }
}
