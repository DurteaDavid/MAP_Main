package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.OpenRFileStmt;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import interpreter.model.value.IValue;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.stmt.IStmt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class OpenRFileStmtTest {

    private PrgState prgState;

    @BeforeEach
    void setUp() {
        IStmt dummyProgram = state -> state;

        prgState = PrgState.of(dummyProgram);
    }

    @AfterEach
    void delete() throws IOException {
        if (prgState != null && prgState.getFileTable() != null) {
            for (BufferedReader reader : prgState.getFileTable().toMap().values()) {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

    @Test
    void testExecute_SuccessfulOpen(@TempDir Path tempDir) throws IOException, MyException {
        File testFile = tempDir.resolve("test.txt").toFile();
        assertTrue(testFile.createNewFile());

        StringValue fileNameVal = new StringValue(testFile.getAbsolutePath());
        ValueExp exp = new ValueExp(fileNameVal);

        OpenRFileStmt stmt = new OpenRFileStmt(exp);

        PrgState resultState = stmt.execute(prgState);

        assertSame(prgState, resultState, "Execute should return the same program state instance");
        assertTrue(prgState.getFileTable().isDefined(fileNameVal), "FileTable should now contain the file name");
        assertNotNull(prgState.getFileTable().lookup(fileNameVal), "BufferedReader should be successfully created and stored");
    }

    @Test
    void testExecute_NotAStringValue() {
        ValueExp exp = new ValueExp(new IntValue(42));
        OpenRFileStmt stmt = new OpenRFileStmt(exp);

        StatementException exception = assertThrows(StatementException.class, () -> {
            stmt.execute(prgState);
        });

        assertTrue(exception.getMessage().contains("is not a string"));
    }

    @Test
    void testExecute_FileAlreadyOpen(@TempDir Path tempDir) throws IOException, ADTException {
        File testFile = tempDir.resolve("open.txt").toFile();
        assertTrue(testFile.createNewFile());

        StringValue fileNameVal = new StringValue(testFile.getAbsolutePath());

        BufferedReader reader = new BufferedReader(new java.io.FileReader(testFile));
        prgState.getFileTable().put(fileNameVal, reader);

        ValueExp exp = new ValueExp(fileNameVal);
        OpenRFileStmt stmt = new OpenRFileStmt(exp);

        StatementException exception = assertThrows(StatementException.class, () -> {
            stmt.execute(prgState);
        });

        assertTrue(exception.getMessage().contains("is already open"));

        reader.close();
    }

    @Test
    void testExecute_FileNotFound() {
        StringValue fileNameVal = new StringValue("invisibleFile.txt");
        ValueExp exp = new ValueExp(fileNameVal);
        OpenRFileStmt stmt = new OpenRFileStmt(exp);

        StatementException exception = assertThrows(StatementException.class, () -> {
            stmt.execute(prgState);
        });

        assertTrue(exception.getMessage().contains("cannot open"));
        assertFalse(prgState.getFileTable().isDefined(fileNameVal));
    }

    @Test
    void testToString() {
        ValueExp exp = new ValueExp(new StringValue("test.txt"));
        OpenRFileStmt stmt = new OpenRFileStmt(exp);

        assertEquals("openRFile(\"test.txt\")", stmt.toString());
    }
}