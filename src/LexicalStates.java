import java.util.HashMap;

/**
 * Created by beatrizzamorano on 11/4/16.
 */
public class LexicalStates {
   public HashMap<Integer, HashMap<String, Integer>> matrix;

   public LexicalStates() {
       initializeMatrix();
   }

    private void initializeMatrix() {
        matrix = new HashMap<>();
        matrix.put(0, hash0());
        matrix.put(1, hash1());
        matrix.put(2, hash2());
        matrix.put(3, hash3());
        matrix.put(4, hash4());
        matrix.put(5, hash5());
        matrix.put(6, hash6());
        matrix.put(7, hash7());
        matrix.put(8, hash8());
        matrix.put(9, hash9());
        matrix.put(10, hash10());
        matrix.put(11, hash11());
        matrix.put(12, hash12());
        matrix.put(13, hash13());
        matrix.put(14, hash14());
        matrix.put(15, hash15());
        matrix.put(16, hash16());
        matrix.put(17, hash17());
        matrix.put(18, hash18());
        matrix.put(19, hash19());

    }

    private HashMap<String,Integer> hash0() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 1);
        hash.put("Digit", 2);
        hash.put("+", 102);
        hash.put("-", 5);
        hash.put("*", 104);
        hash.put("/", 105);
        hash.put("%", 106);
        hash.put("^", 107);
        hash.put("#", 108);
        hash.put("=", 10);
        hash.put("~", 11);
        hash.put("<", 12);
        hash.put(">", 13);
        hash.put("(", 116);
        hash.put(")", 117);
        hash.put("{", 118);
        hash.put("}", 119);
        hash.put("[", 120);
        hash.put("]", 121);
        hash.put(";", 122);
        hash.put(":", 123);
        hash.put(",", 124);
        hash.put(".", 14);
        hash.put("'", 18);
        hash.put("\"", 16);
        hash.put("_", 500);
        hash.put("\\", 500);
        hash.put(" ", 0 );
        hash.put("\t", 0);
        hash.put("\n", 0);
        hash.put("EOF", 0);
        return hash;
    }

    private HashMap<String,Integer> hash1() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 1);
        hash.put("Digit", 1);
        hash.put("+", 100);
        hash.put("-", 100);
        hash.put("*", 100);
        hash.put("/", 100);
        hash.put("%", 100);
        hash.put("^", 100);
        hash.put("#", 100);
        hash.put("=", 100);
        hash.put("~", 100);
        hash.put("<", 100);
        hash.put(">", 100);
        hash.put("(", 100);
        hash.put(")", 100);
        hash.put("{", 100);
        hash.put("}", 100);
        hash.put("[", 100);
        hash.put("]", 100);
        hash.put(";", 100);
        hash.put(":", 100);
        hash.put(",", 100);
        hash.put(".", 100);
        hash.put("'", 100);
        hash.put("\"", 100);
        hash.put("_", 1);
        hash.put("\\", 100);
        hash.put(" ", 100);
        hash.put("\t", 100);
        hash.put("\n", 100);
        hash.put("EOF", 100);
        return hash;
    }

    private HashMap<String,Integer> hash2() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 101);
        hash.put("Digit", 2);
        hash.put("+", 101);
        hash.put("-", 101);
        hash.put("*", 101);
        hash.put("/", 101);
        hash.put("%", 101);
        hash.put("^", 101);
        hash.put("#", 101);
        hash.put("=", 101);
        hash.put("~", 101);
        hash.put("<", 101);
        hash.put(">", 101);
        hash.put("(", 101);
        hash.put(")", 101);
        hash.put("{", 101);
        hash.put("}", 101);
        hash.put("[", 101);
        hash.put("]", 101);
        hash.put(";", 101);
        hash.put(":", 101);
        hash.put(",", 101);
        hash.put(".", 3);
        hash.put("'", 101);
        hash.put("\"", 101);
        hash.put("_", 101);
        hash.put("\\", 101);
        hash.put(" ", 101);
        hash.put("\t", 101);
        hash.put("\n", 101);
        hash.put("EOF", 101);
        return hash;
    }

    private HashMap<String,Integer> hash3() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 500);
        hash.put("Digit", 4);
        hash.put("+", 500);
        hash.put("-", 500);
        hash.put("*", 500);
        hash.put("/", 500);
        hash.put("%", 500);
        hash.put("^", 500);
        hash.put("#", 500);
        hash.put("=", 500);
        hash.put("~", 500);
        hash.put("<", 500);
        hash.put(">", 500);
        hash.put("(", 500);
        hash.put(")", 500);
        hash.put("{", 500);
        hash.put("}", 500);
        hash.put("[", 500);
        hash.put("]", 500);
        hash.put(";", 500);
        hash.put(":", 500);
        hash.put(",", 500);
        hash.put(".", 500);
        hash.put("'", 500);
        hash.put("\"", 500);
        hash.put("_", 500);
        hash.put("\\", 500);
        hash.put(" ", 500);
        hash.put("\t", 500);
        hash.put("\n", 500);
        hash.put("EOF", 501);
        return hash;
    }

    private HashMap<String,Integer> hash4() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 101);
        hash.put("Digit", 4);
        hash.put("+", 101);
        hash.put("-", 101);
        hash.put("*", 101);
        hash.put("/", 101);
        hash.put("%", 101);
        hash.put("^", 101);
        hash.put("#", 101);
        hash.put("=", 101);
        hash.put("~", 101);
        hash.put("<", 101);
        hash.put(">", 101);
        hash.put("(", 101);
        hash.put(")", 101);
        hash.put("{", 101);
        hash.put("}", 101);
        hash.put("[", 101);
        hash.put("]", 101);
        hash.put(";", 101);
        hash.put(":", 101);
        hash.put(",", 101);
        hash.put(".", 101);
        hash.put("'", 101);
        hash.put("\"", 101);
        hash.put("_", 101);
        hash.put("\\", 101);
        hash.put(" ", 101);
        hash.put("\t", 101);
        hash.put("\n", 101);
        hash.put("EOF",101);
        return hash;
    }

    private HashMap<String,Integer> hash5() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 103);
        hash.put("Digit", 103);
        hash.put("+", 103);
        hash.put("-", 6);
        hash.put("*", 103);
        hash.put("/", 103);
        hash.put("%", 103);
        hash.put("^", 103);
        hash.put("#", 103);
        hash.put("=", 103);
        hash.put("~", 103);
        hash.put("<", 103);
        hash.put(">", 103);
        hash.put("(", 103);
        hash.put(")", 103);
        hash.put("{", 103);
        hash.put("}", 103);
        hash.put("[", 103);
        hash.put("]", 103);
        hash.put(";", 103);
        hash.put(":", 103);
        hash.put(",", 103);
        hash.put(".", 103);
        hash.put("'", 103);
        hash.put("\"", 103);
        hash.put("_", 103);
        hash.put("\\", 103);
        hash.put(" ", 103);
        hash.put("\t", 103);
        hash.put("\n", 103);
        hash.put("EOF", 103);
        return hash;
    }

    private HashMap<String,Integer> hash6() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 6);
        hash.put("Digit", 6);
        hash.put("+", 6);
        hash.put("-", 6);
        hash.put("*", 6);
        hash.put("/", 6);
        hash.put("%", 6);
        hash.put("^", 6);
        hash.put("#", 6);
        hash.put("=", 6);
        hash.put("~", 6);
        hash.put("<", 6);
        hash.put(">", 6);
        hash.put("(", 6);
        hash.put(")", 6);
        hash.put("{", 6);
        hash.put("}", 6);
        hash.put("[", 7);
        hash.put("]", 6);
        hash.put(";", 6);
        hash.put(":", 6);
        hash.put(",", 6);
        hash.put(".", 6);
        hash.put("'", 6);
        hash.put("\"", 6);
        hash.put("_", 6);
        hash.put("\\", 6);
        hash.put(" ", 6);
        hash.put("\t", 6);
        hash.put("\n", 0);
        hash.put("EOF", 0);
        return hash;
    }

    private HashMap<String,Integer> hash7() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 6);
        hash.put("Digit", 6);
        hash.put("+", 6);
        hash.put("-", 6);
        hash.put("*", 6);
        hash.put("/", 6);
        hash.put("%", 6);
        hash.put("^", 6);
        hash.put("#", 6);
        hash.put("=", 6);
        hash.put("~", 6);
        hash.put("<", 6);
        hash.put(">", 6);
        hash.put("(", 6);
        hash.put(")", 6);
        hash.put("{", 6);
        hash.put("}", 6);
        hash.put("[", 8);
        hash.put("]", 6);
        hash.put(";", 6);
        hash.put(":", 6);
        hash.put(",", 6);
        hash.put(".", 6);
        hash.put("'", 6);
        hash.put("\"", 6);
        hash.put("_", 6);
        hash.put("\\", 6);
        hash.put(" ", 6);
        hash.put("\t", 6);
        hash.put("\n", 0);
        hash.put("EOF", 0);
        return hash;
    }

    private HashMap<String,Integer> hash8() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 8);
        hash.put("Digit", 8);
        hash.put("+", 8);
        hash.put("-", 8);
        hash.put("*", 8);
        hash.put("/", 8);
        hash.put("%", 8);
        hash.put("^", 8);
        hash.put("#", 8);
        hash.put("=", 8);
        hash.put("~", 8);
        hash.put("<", 8);
        hash.put(">", 8);
        hash.put("(", 8);
        hash.put(")", 8);
        hash.put("{", 8);
        hash.put("}", 8);
        hash.put("[", 8);
        hash.put("]", 9);
        hash.put(";", 8);
        hash.put(":", 8);
        hash.put(",", 8);
        hash.put(".", 8);
        hash.put("'", 8);
        hash.put("\"", 8);
        hash.put("_", 8);
        hash.put("\\", 8);
        hash.put(" ", 8);
        hash.put("\t", 8);
        hash.put("\n", 8);
        hash.put("EOF", 501);
        return hash;
    }

    private HashMap<String,Integer> hash9() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 8);
        hash.put("Digit", 8);
        hash.put("+", 8);
        hash.put("-", 8);
        hash.put("*", 8);
        hash.put("/", 8);
        hash.put("%", 8);
        hash.put("^", 8);
        hash.put("#", 8);
        hash.put("=", 8);
        hash.put("~", 8);
        hash.put("<", 8);
        hash.put(">", 8);
        hash.put("(", 8);
        hash.put(")", 8);
        hash.put("{", 8);
        hash.put("}", 8);
        hash.put("[", 8);
        hash.put("]", 0);
        hash.put(";", 8);
        hash.put(":", 8);
        hash.put(",", 8);
        hash.put(".", 8);
        hash.put("'", 8);
        hash.put("\"", 8);
        hash.put("_", 8);
        hash.put("\\", 8);
        hash.put(" ", 8);
        hash.put("\t", 8);
        hash.put("\n", 8);
        hash.put("EOF", 8);
        return hash;
    }

    private HashMap<String,Integer> hash10() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 115);
        hash.put("Digit", 115);
        hash.put("+", 115);
        hash.put("-", 115);
        hash.put("*", 115);
        hash.put("/", 115);
        hash.put("%", 115);
        hash.put("^", 115);
        hash.put("#", 115);
        hash.put("=", 109);
        hash.put("~", 115);
        hash.put("<", 115);
        hash.put(">", 115);
        hash.put("(", 115);
        hash.put(")", 115);
        hash.put("{", 115);
        hash.put("}", 115);
        hash.put("[", 115);
        hash.put("]", 115);
        hash.put(";", 115);
        hash.put(":", 115);
        hash.put(",", 115);
        hash.put(".", 115);
        hash.put("'", 115);
        hash.put("\"", 115);
        hash.put("_", 115);
        hash.put("\\", 115);
        hash.put(" ", 115);
        hash.put("\t", 115);
        hash.put("\n", 115);
        hash.put("EOF", 115);
        return hash;
    }

    private HashMap<String,Integer> hash11() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 500);
        hash.put("Digit", 500);
        hash.put("+", 500);
        hash.put("-", 500);
        hash.put("*", 500);
        hash.put("/", 500);
        hash.put("%", 500);
        hash.put("^", 500);
        hash.put("#", 500);
        hash.put("=", 110);
        hash.put("~", 500);
        hash.put("<", 500);
        hash.put(">", 500);
        hash.put("(", 500);
        hash.put(")", 500);
        hash.put("{", 500);
        hash.put("}", 500);
        hash.put("[", 500);
        hash.put("]", 500);
        hash.put(";", 500);
        hash.put(":", 500);
        hash.put(",", 500);
        hash.put(".", 500);
        hash.put("'", 500);
        hash.put("\"", 500);
        hash.put("_", 500);
        hash.put("\\", 500);
        hash.put(" ", 500);
        hash.put("\t", 500);
        hash.put("\n", 502);
        hash.put("EOF", 501);
        return hash;
    }

    private HashMap<String,Integer> hash12() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 113);
        hash.put("Digit", 113);
        hash.put("+", 113);
        hash.put("-", 113);
        hash.put("*", 113);
        hash.put("/", 113);
        hash.put("%", 113);
        hash.put("^", 113);
        hash.put("#", 113);
        hash.put("=", 111);
        hash.put("~", 113);
        hash.put("<", 113);
        hash.put(">", 113);
        hash.put("(", 113);
        hash.put(")", 113);
        hash.put("{", 113);
        hash.put("}", 113);
        hash.put("[", 113);
        hash.put("]", 113);
        hash.put(";", 113);
        hash.put(":", 113);
        hash.put(",", 113);
        hash.put(".", 113);
        hash.put("'", 113);
        hash.put("\"", 113);
        hash.put("_", 113);
        hash.put("\\", 113);
        hash.put(" ", 113);
        hash.put("\t", 113);
        hash.put("\n", 113);
        hash.put("EOF", 113);
        return hash;
    }

    private HashMap<String,Integer> hash13() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 114);
        hash.put("Digit", 114);
        hash.put("+", 114);
        hash.put("-", 114);
        hash.put("*", 114);
        hash.put("/", 114);
        hash.put("%", 114);
        hash.put("^", 114);
        hash.put("#", 114);
        hash.put("=", 112);
        hash.put("~", 114);
        hash.put("<", 114);
        hash.put(">", 114);
        hash.put("(", 114);
        hash.put(")", 114);
        hash.put("{", 114);
        hash.put("}", 114);
        hash.put("[", 114);
        hash.put("]", 114);
        hash.put(";", 114);
        hash.put(":", 114);
        hash.put(",", 114);
        hash.put(".", 114);
        hash.put("'", 114);
        hash.put("\"", 114);
        hash.put("_", 114);
        hash.put("\\", 114);
        hash.put(" ", 114);
        hash.put("\t", 114);
        hash.put("\n", 114);
        hash.put("EOF", 114);
        return hash;
    }

    private HashMap<String,Integer> hash14() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 125);
        hash.put("Digit", 125);
        hash.put("+", 125);
        hash.put("-", 125);
        hash.put("*", 125);
        hash.put("/", 125);
        hash.put("%", 125);
        hash.put("^", 125);
        hash.put("#", 125);
        hash.put("=", 125);
        hash.put("~", 125);
        hash.put("<", 125);
        hash.put(">", 125);
        hash.put("(", 125);
        hash.put(")", 125);
        hash.put("{", 125);
        hash.put("}", 125);
        hash.put("[", 125);
        hash.put("]", 125);
        hash.put(";", 125);
        hash.put(":", 125);
        hash.put(",", 125);
        hash.put(".", 15);
        hash.put("'", 125);
        hash.put("\"", 125);
        hash.put("_", 125);
        hash.put("\\", 125);
        hash.put(" ", 125);
        hash.put("\t", 125);
        hash.put("\n", 125);
        hash.put("EOF", 125);
        return hash;
    }

    private HashMap<String,Integer> hash15() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 126);
        hash.put("Digit", 126);
        hash.put("+", 126);
        hash.put("-", 126);
        hash.put("*", 126);
        hash.put("/", 126);
        hash.put("%", 126);
        hash.put("^", 126);
        hash.put("#", 126);
        hash.put("=", 126);
        hash.put("~", 126);
        hash.put("<", 126);
        hash.put(">", 126);
        hash.put("(", 126);
        hash.put(")", 126);
        hash.put("{", 126);
        hash.put("}", 126);
        hash.put("[", 126);
        hash.put("]", 126);
        hash.put(";", 126);
        hash.put(":", 126);
        hash.put(",", 126);
        hash.put(".", 127);
        hash.put("'", 126);
        hash.put("\"", 126);
        hash.put("_", 126);
        hash.put("\\", 126);
        hash.put(" ", 126);
        hash.put("\t", 126);
        hash.put("\n", 126);
        hash.put("EOF", 126);
        return hash;
    }

    private HashMap<String,Integer> hash16() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 16);
        hash.put("Digit", 16);
        hash.put("+", 16);
        hash.put("-", 16);
        hash.put("*", 16);
        hash.put("/", 16);
        hash.put("%", 16);
        hash.put("^", 16);
        hash.put("#", 16);
        hash.put("=", 16);
        hash.put("~", 16);
        hash.put("<", 16);
        hash.put(">", 16);
        hash.put("(", 16);
        hash.put(")", 16);
        hash.put("{", 16);
        hash.put("}", 16);
        hash.put("[", 18);
        hash.put("]", 16);
        hash.put(";", 16);
        hash.put(":", 16);
        hash.put(",", 16);
        hash.put(".", 16);
        hash.put("'", 16);
        hash.put("\"", 128);
        hash.put("_", 16);
        hash.put("\\", 17);
        hash.put(" ", 16);
        hash.put("\t", 16);
        hash.put("\n", 502);
        hash.put("EOF", 501);
        return hash;
    }

    private HashMap<String,Integer> hash17() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 16);
        hash.put("Digit", 16);
        hash.put("+", 16);
        hash.put("-", 16);
        hash.put("*", 16);
        hash.put("/", 16);
        hash.put("%", 16);
        hash.put("^", 16);
        hash.put("#", 16);
        hash.put("=", 16);
        hash.put("~", 16);
        hash.put("<", 16);
        hash.put(">", 16);
        hash.put("(", 16);
        hash.put(")", 16);
        hash.put("{", 16);
        hash.put("}", 16);
        hash.put("[", 18);
        hash.put("]", 16);
        hash.put(";", 16);
        hash.put(":", 16);
        hash.put(",", 16);
        hash.put(".", 16);
        hash.put("'", 16);
        hash.put("\"", 128);
        hash.put("_", 16);
        hash.put("\\", 16);
        hash.put(" ", 16);
        hash.put("\t", 16);
        hash.put("\n", 16);
        hash.put("EOF", 501);
        return hash;
    }

    private HashMap<String,Integer> hash18() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 18);
        hash.put("Digit", 18);
        hash.put("+", 18);
        hash.put("-", 18);
        hash.put("*", 18);
        hash.put("/", 18);
        hash.put("%", 18);
        hash.put("^", 18);
        hash.put("#", 18);
        hash.put("=", 18);
        hash.put("~", 18);
        hash.put("<", 18);
        hash.put(">", 18);
        hash.put("(", 18);
        hash.put(")", 18);
        hash.put("{", 18);
        hash.put("}", 18);
        hash.put("[", 18);
        hash.put("]", 18);
        hash.put(";", 18);
        hash.put(":", 18);
        hash.put(",", 18);
        hash.put(".", 18);
        hash.put("'", 128);
        hash.put("\"", 18);
        hash.put("_", 18);
        hash.put("\\", 19);
        hash.put(" ", 18);
        hash.put("\t", 18);
        hash.put("\n", 502);
        hash.put("EOF", 501);
        return hash;
    }

    private HashMap<String,Integer> hash19() {
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("Alpha", 18);
        hash.put("Digit", 18);
        hash.put("+", 18);
        hash.put("-", 18);
        hash.put("*", 18);
        hash.put("/", 18);
        hash.put("%", 18);
        hash.put("^", 18);
        hash.put("#", 18);
        hash.put("=", 18);
        hash.put("~", 18);
        hash.put("<", 18);
        hash.put(">", 18);
        hash.put("(", 18);
        hash.put(")", 18);
        hash.put("{", 18);
        hash.put("}", 18);
        hash.put("[", 18);
        hash.put("]", 18);
        hash.put(";", 18);
        hash.put(":", 18);
        hash.put(",", 18);
        hash.put(".", 18);
        hash.put("'", 128);
        hash.put("\"", 18);
        hash.put("_", 18);
        hash.put("\\", 19);
        hash.put(" ", 18);
        hash.put("\t", 18);
        hash.put("\n", 18);
        hash.put("EOF", 501);
        return hash;
    }

    public int getNextState(int currentState, String key){
        HashMap row = matrix.get(currentState);
        return (int) row.get(key);
    }
}
