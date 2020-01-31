import java.util.StringTokenizer;

public class TestClass {

    public static void main(String[] args) {
        String val = "a|p";
        StringTokenizer tokenizer = new StringTokenizer(val, "|");

        for (String token : val.split("\\|")) {
            System.out.println(token);
        }
    }
}
