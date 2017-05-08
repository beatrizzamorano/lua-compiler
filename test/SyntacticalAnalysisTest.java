import org.junit.Test;

import java.util.List;

/**
 * Created by beatrizzamorano on 1/5/17.
 */
public class SyntacticalAnalysisTest {
    @Test
    public void testSyntacticalAnalysis() {
        Compiler.LexicalAnalysis lexicalAnalysis = new Compiler.LexicalAnalysis();
        lexicalAnalysis.setCode(" a = {}\n" +
                "       local x = 20\n" +
                "       for i=1,10 do\n" +
                "         local y = 0\n" +
                "         a[i] = function () y=y+1; return x+y end\n" +
                "       end");
        lexicalAnalysis.generateTokens();
        List<Compiler.Token> tokens = lexicalAnalysis.getTokens();

        Compiler.SyntacticalAnalysis syntacticalAnalysis = new Compiler.SyntacticalAnalysis(tokens);
    }
}
