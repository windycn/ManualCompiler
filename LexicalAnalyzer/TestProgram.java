package myCompile;

public class TestProgram {
	/** ���ڲ��Դʷ������� */
	public static void main(String[] args) {
		theCompiler compiler = new theCompiler();
		// �������
		String sentence = "if == i>==<=<++-+13: int c = 1; else: b = 3*6+8";
        System.out.println("\n���: " + sentence);
        TokenReader tokenReader = compiler.tokenize(sentence);
        compiler.printAll(tokenReader);
        System.out.println("------------------------");
	}
}
