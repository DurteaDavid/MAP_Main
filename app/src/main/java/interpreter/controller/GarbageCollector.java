package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class GarbageCollector {
    private GarbageCollector() {}
    /** The addresses that occur in the RefValues of the SymTable. */
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).address())
                .collect(Collectors.toSet());
    }
    /** UNSAFE: keeps only the heap entries referred to directly from the SymTable. */
    public static Map<Integer, IValue> unsafeGarbageCollector(Set<Integer> symTableAddr,
                                                              Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Set<Integer> reachableAddresses(Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> reachable = new HashSet<>(roots);
        boolean added;
        do {
            added = false;
            Set<Integer> snapshot = new HashSet<>(reachable);
            for (Integer addr : snapshot) {
                IValue val = heap.get(addr);
                if (val instanceof RefValue refVal) {
                    if (reachable.add(refVal.address())) {
                        added = true;
                    }
                }
            }
        } while (added);
        return reachable;
    }

    public static Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> keepAddresses = reachableAddresses(symTableAddr, heap);
        return heap.entrySet().stream()
                .filter(e -> keepAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
