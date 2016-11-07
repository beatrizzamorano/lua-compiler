import org.junit.Assert;
import org.junit.Test;

/**
 * Created by beatrizzamorano on 11/4/16.
 */
public class LexicalAnalysisTest {
    @Test
    public void testLexicalAnalysis() {
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis();
        lexicalAnalysis.setCode(" a = {}\n" +
                "       local x = 20\n" +
                "       for i=1,10 do\n" +
                "         local y = 0\n" +
                "         a[i] = function () y=y+1; return x+y end\n" +
                "   \"hola \"   " +
                "       end");
        boolean programIsValid = lexicalAnalysis.generateTokens();
        Assert.assertEquals(true, programIsValid);
    }
}
