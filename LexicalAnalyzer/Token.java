package myCompile;

public class Token {
	/** ����Token���ͺ��ı�ֵ */
    TokenType type = null;
    String text = null;
    
    /** ��ȡToken������ */
    public TokenType getType() {
        return type;
    }
    
    /** ��ȡToken���ı�ֵ */
    public String getText() {
        return text;
    }
    
    /** ����Token������ */
    static enum TokenType{
    	Plus,   // +
    	Minus,  //  -
    	Star,   //  *
        Slash,  // /

        GE,     // >=
        GT,     // >
        EQ,     // ==
        LE,     // <=
        LT,     // <

        SemiColon, // ;
        LeftParen, // (
        RightParen,// )
        Colon, // :
        Assignment,// =

        If,
        Else,
        
        Int,

        Identifier,     //��ʶ��

        IntLiteral,     //����������
        StringLiteral   //�ַ���������
    }
}
