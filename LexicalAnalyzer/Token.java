package myCompile;

public class Token {
	/** 定义Token类型和文本值 */
    TokenType type = null;
    String text = null;
    
    /** 获取Token的类型 */
    public TokenType getType() {
        return type;
    }
    
    /** 获取Token的文本值 */
    public String getText() {
        return text;
    }
    
    /** 定义Token的类型 */
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

        Identifier,     //标识符

        IntLiteral,     //整型字面量
        StringLiteral   //字符串字面量
    }
}
