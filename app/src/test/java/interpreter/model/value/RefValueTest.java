package interpreter.model.value;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import org.junit.jupiter.api.Test;

import java.sql.Ref;

import static org.junit.jupiter.api.Assertions.*;

public class RefValueTest {

    @Test
    void testRefType() {
        RefType T = new RefType(new IntType());
        assertEquals("Ref(int )", T.toString());
        assertEquals(new RefValue(0, new IntType()),T.defaultValue());
    }

    @Test
    void testEquals() {
        RefType Ref1 = new RefType(new RefType(new IntType()));
        RefType Ref2 = new RefType(new RefType(new IntType()));
        RefType Ref3 = new RefType(new IntType());
        assertEquals(Ref1, Ref2);
        assertNotEquals(Ref1, Ref3);
    }
}
