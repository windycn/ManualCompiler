package myCompile;

public class TestProgram {
	/** ÓÃÓÚ²âÊÔ´Ê·¨·ÖÎöÆ÷ */
	public static void main(String[] args) {
		theCompiler compiler = new theCompiler();
		// ²âÊÔÓï¾ä
		String sentence = "if == i>==<=<++-+13: int c = 1; else: b = 3*6+8";
        System.out.println("\nÓï¾ä: " + sentence);
        TokenReader tokenReader = compiler.tokenize(sentence);
        compiler.printAll(tokenReader);
        System.out.println("------------------------");
	}
}
