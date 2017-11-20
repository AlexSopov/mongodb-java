import org.junit.Assert;
import org.junit.Test;
import usage.Program;

public class ProgramTest {
    @Test
    public void canGenerateLogs() {
        Assert.assertNotNull(Program.generateLogs());
    }
}
