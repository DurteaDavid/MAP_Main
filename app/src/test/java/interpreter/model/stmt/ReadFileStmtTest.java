package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ReadFileStmtTest {

    private PrgState prgState;

    @BeforeEach
    void setUp() {
        IStmt dummyProgram = state -> state;
        prgState = PrgState.of(dummyProgram);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (prgState != null && prgState.getFileTable() != null) {
            for (BufferedReader reader : prgState.getFileTable().toMap().values()) {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    @Test
    void testExecute_SuccessfulRead(@TempDir Path tempDir) throws IOException, MyException {
        File testFile = tempDir.resolve("numbers.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("42\n");
        }

        StringValue fileNameVal = new StringValue(testFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new java.io.FileReader(testFile));

        prgState.getFileTable().put(fileNameVal, reader);
        prgState.getSymTable().put("myVar", new IntValue(0));

        ValueExp exp = new ValueExp(fileNameVal);
        ReadFileStmt stmt = new ReadFileStmt(exp, "myVar");

        PrgState resultState = stmt.execute(prgState);

        assertSame(prgState, resultState);
        assertEquals(new IntValue(42), prgState.getSymTable().lookup("myVar"),
                "Symbol table variable should be updated with the parsed integer 42");
    }

    @Test
    void testExecute_Zero(@TempDir Path tempDir) throws IOException, MyException {
        File testFile = tempDir.resolve("empty.txt").toFile();
        assertTrue(testFile.createNewFile());

        StringValue fileNameVal = new StringValue(testFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new java.io.FileReader(testFile));

        prgState.getFileTable().put(fileNameVal, reader);
        prgState.getSymTable().put("myVar", new IntValue(100));

        ValueExp exp = new ValueExp(fileNameVal);
        ReadFileStmt stmt = new ReadFileStmt(exp, "myVar");

        stmt.execute(prgState);

        assertEquals(new IntValue(0), prgState.getSymTable().lookup("myVar"));
    }

    @Test
    void testExecute_VariableNotDefined() {
        ValueExp exp = new ValueExp(new StringValue("test.txt"));
        ReadFileStmt stmt = new ReadFileStmt(exp, "undefinedVar");

        StatementException exception = assertThrows(StatementException.class, () -> stmt.execute(prgState));
        assertTrue(exception.getMessage().contains("is not defined"));
    }

    @Test
    void testExecute_VariableNotIntType() throws ADTException {
        prgState.getSymTable().put("stringVar", new StringValue("hello"));
        ValueExp exp = new ValueExp(new StringValue("test.txt"));
        ReadFileStmt stmt = new ReadFileStmt(exp, "stringVar");

        StatementException exception = assertThrows(StatementException.class, () -> stmt.execute(prgState));
        assertTrue(exception.getMessage().contains("is not of type int"));
    }

    @Test
    void testExecute_ExpressionNotString() throws ADTException {
        prgState.getSymTable().put("myVar", new IntValue(0));
        ValueExp exp = new ValueExp(new IntValue(123));
        ReadFileStmt stmt = new ReadFileStmt(exp, "myVar");

        StatementException exception = assertThrows(StatementException.class, () -> stmt.execute(prgState));
        assertTrue(exception.getMessage().contains("is not a string"));
    }

    @Test
    void testExecute_FileNotOpen() throws ADTException {
        prgState.getSymTable().put("myVar", new IntValue(0));
        StringValue fileNameVal = new StringValue("unopened.txt");
        ValueExp exp = new ValueExp(fileNameVal);
        ReadFileStmt stmt = new ReadFileStmt(exp, "myVar");

        StatementException exception = assertThrows(StatementException.class, () -> stmt.execute(prgState));
        assertTrue(exception.getMessage().contains("is not open"));
    }

    @Test
    void testExecute_InvalidIntegerFormat(@TempDir Path tempDir) throws IOException, ADTException {
        File testFile = tempDir.resolve("bad.txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("not-an-integer\n");
        }

        StringValue fileNameVal = new StringValue(testFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new java.io.FileReader(testFile));

        prgState.getFileTable().put(fileNameVal, reader);
        prgState.getSymTable().put("myVar", new IntValue(0));

        ValueExp exp = new ValueExp(fileNameVal);
        ReadFileStmt stmt = new ReadFileStmt(exp, "myVar");

        StatementException exception = assertThrows(StatementException.class, () -> stmt.execute(prgState));
        assertTrue(exception.getMessage().contains("error reading file or parsing integer"));
    }

    @Test
    void testToString() {
        ValueExp exp = new ValueExp(new StringValue("test.txt"));
        ReadFileStmt stmt = new ReadFileStmt(exp, "varName");

        assertEquals("readFile(\"test.txt\", varName)", stmt.toString());
    }
}