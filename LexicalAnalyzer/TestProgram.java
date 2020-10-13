package myCompile;

public class TestProgram {
	/** 用于测试词法分析器 */
	public static void main(String[] args) {
		LexicalAnalysis compiler = new LexicalAnalysis();
		// 测试语句
		String sentence = "if == i>==<=<++-+13: int c = 1; else: b = 3*6+8";
        System.out.println("\n语句: " + sentence);
        TokenReader tokenReader = compiler.tokenize(sentence);
        compiler.printAll(tokenReader);
        System.out.println("------------------------");
	}
}
