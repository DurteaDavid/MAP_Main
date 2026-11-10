package interpreter.model.stmt;

import interpreter.exceptions.ADTException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CloseRFileStmtTest {

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
    void testExecute_SuccessfulClose(@TempDir Path tempDir) throws IOException, MyException {
        File testFile = tempDir.resolve("close_test.txt").toFile();
        assertTrue(testFile.createNewFile());

        StringValue fileNameVal = new StringValue(testFile.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new java.io.FileReader(testFile));

        prgState.getFileTable().put(fileNameVal, reader);
        assertTrue(prgState.getFileTable().isDefined(fileNameVal), "Pre-condition: File should be defined in fileTable");

        ValueExp exp = new ValueExp(fileNameVal);
        CloseRFileStmt stmt = new CloseRFileStmt(exp);

        PrgState resultState = stmt.execute(prgState);

        assertSame(prgState, resultState, "Execute should return the same program state instance");
        assertFalse(prgState.getFileTable().isDefined(fileNameVal), "File should be removed from fileTable after closing");
    }

    @Test
    void testExecute_ExpressionNotString() {
        ValueExp exp = new ValueExp(new IntValue(123));
        CloseRFileStmt stmt = new CloseRFileStmt(exp);

        StatementException exception = assertThrows(StatementException.class, () -> stmt.execute(prgState));
        assertTrue(exception.getMessage().contains("is not a string"));
    }

    @Test
    void testExecute_FileNotOpen() {
        StringValue fileNameVal = new StringValue("non_existent_file.txt");
        ValueExp exp = new ValueExp(fileNameVal);
        CloseRFileStmt stmt = new CloseRFileStmt(exp);

        StatementException exception = assertThrows(StatementException.class, () -> stmt.execute(prgState));
        assertTrue(exception.getMessage().contains("is not open"));
    }

    @Test
    void testToString() {
        ValueExp exp = new ValueExp(new StringValue("test.txt"));
        CloseRFileStmt stmt = new CloseRFileStmt(exp);

        assertEquals("closeRFile(\"test.txt\")", stmt.toString());
    }
}