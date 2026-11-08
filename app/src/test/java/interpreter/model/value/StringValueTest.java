package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {

    @Test
    void testString() {
        StringType str = new StringType();
        StringValue val = new StringValue("");
        assertEquals(val, str.defaultValue());

        StringValue val1 = new StringValue("str");
        StringValue val2 = new StringValue("str");
        assertEquals(val1.toString(), val2.toString());
        assertEquals(val1.hashCode(),val2.hashCode());
    }
}
