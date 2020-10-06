package myCompile;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 词法分析器
 * 输出Token串
 * @author 王晔
 */
public class theCompiler {
	/** 定义词法分析使用的变量 */
	// 临时保存Token文本
	private StringBuffer tokenText = null;
	// 保存解析后的Token
	private List<Token> tokens = null;
	// 保存当前正在解析的Token
	private Token token = null;
	
	/** 定义便捷的判断方法，减少代码冗余 */
	//是否是字母
    private boolean isAlpha(int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }
    //是否是数字
    private boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }
    //是否是空白字符
    private boolean isBlank(int ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }
	
    /**
     * 创建有限自动机，并进入初始状态
     * 进入初始状态后会马上进入其他状态。
     * 开始解析的时候，进入初始状态；
     * 某个Token解析完毕，也进入初始状态，在这里把Token记下来，然后建立一个新的Token。
     * @param ch
     * @return
     */
    private DfaState initToken(char ch) {
        if (tokenText.length() > 0) {
            token.text= tokenText.toString();
            tokens.add(token);

            tokenText = new StringBuffer();
            token = new Token();
        }

        DfaState newState = DfaState.Initial;
        // 进入字母匹配状态
        if (isAlpha(ch)) {
            if (ch == 'i') {
            	// 如果字符为i，则进入int/if关键字的匹配
                newState = DfaState.Id_int1_if1;
            } else if (ch == 'e') {
            	// 如果字符为e，则进入else关键字的匹配
            	newState = DfaState.Id_else1;
            } else {
                newState = DfaState.Id; //进入Id状态
            }
            token.type = Token.TokenType.Identifier;
            tokenText.append(ch);
        } 
        // 进入数字匹配状态
        else if (isDigit(ch)) { 
            newState = DfaState.IntLiteral;
            token.type = Token.TokenType.IntLiteral;
            tokenText.append(ch);
        }
        // 依次进入符号匹配
        else if (ch == '>') {
            newState = DfaState.GT;
            token.type = Token.TokenType.GT;
            tokenText.append(ch);
        }  else if (ch == '<') {
             newState = DfaState.LT;
             token.type = Token.TokenType.LT;
             tokenText.append(ch);
        } else if (ch == '+') {
            newState = DfaState.Plus;
            token.type = Token.TokenType.Plus;
            tokenText.append(ch);
        } else if (ch == '-') {
            newState = DfaState.Minus;
            token.type = Token.TokenType.Minus;
            tokenText.append(ch);
        } else if (ch == '*') {
            newState = DfaState.Star;
            token.type = Token.TokenType.Star;
            tokenText.append(ch);
        } else if (ch == '/') {
            newState = DfaState.Slash;
            token.type = Token.TokenType.Slash;
            tokenText.append(ch);
        } else if (ch == ';') {
            newState = DfaState.SemiColon;
            token.type = Token.TokenType.SemiColon;
            tokenText.append(ch);
        } else if (ch == '(') {
            newState = DfaState.LeftParen;
            token.type = Token.TokenType.LeftParen;
            tokenText.append(ch);
        } else if (ch == ')') {
            newState = DfaState.RightParen;
            token.type = Token.TokenType.RightParen;
            tokenText.append(ch);
        } else if (ch == ':') {
            newState = DfaState.Colon;
            token.type = Token.TokenType.Colon;
            tokenText.append(ch);
        } else if (ch == '=') {
            newState = DfaState.Assignment;
            token.type = Token.TokenType.Assignment;
            tokenText.append(ch);
        } else {
        	 // 跳过所有未知部分
            newState = DfaState.Initial;
        }
        return newState;
    }
    
    /**
     * 解析字符串，形成Token。
     * 这是一个有限状态自动机，在不同的状态中迁移。
     * @param code
     * @return
     */
    public TokenReader tokenize(String code) {
        tokens = new ArrayList<Token>();
        // toCharArray()方法将字符串转换为字符数组
        // CharArrayReader类实现一个可用作字符输入流的字符缓冲区
        CharArrayReader reader = new CharArrayReader(code.toCharArray());
        tokenText = new StringBuffer();
        token = new Token();
        int ich = 0;
        char ch = 0;
        // 初始化有限自动机状态
        DfaState state = DfaState.Initial;
        try {
        	// java.io.CharArrayReader.read()用于读取单个字符
        	// 返回值为整数，表示读取的字符，如果流结束，则返回-1
            while ((ich = reader.read()) != -1) {
            	// 强制类型转换为字符类型
                ch = (char) ich;
                switch (state) {
                // 重新确定初始状态
                case Initial:
                    state = initToken(ch);
                    break;
                // 标识符识别状态
                case Id:
                    if (isAlpha(ch) || isDigit(ch)) {
                    	//保持标识符状态
                        tokenText.append(ch);       
                    } else {
                    	//退出标识符状态，并保存Token
                        state = initToken(ch);      
                    }
                    break;
                // >/>=识别状态
                case GT:
                	// 匹配是否为GE(>=)状态
                    if (ch == '=') {
                    	// 切换状态为GE
                        token.type = Token.TokenType.GE;
                        state = DfaState.GE;
                        tokenText.append(ch);
                    } else {
                    	 // 退出GT(>)状态，并保存Token
                        state = initToken(ch);   
                    }
                    break;
                case GE:
                	state = initToken(ch);
                	break;
                // </<=识别状态
                case LT:
                	if (ch == '=') {
                        token.type = Token.TokenType.LE;
                        state = DfaState.LE;
                        tokenText.append(ch);
                    } else {
                        state = initToken(ch); 
                    }
                    break;
                case LE:
                	state = initToken(ch);
                	break;
                // =/==识别状态
                case Assignment:
                	if (ch == '=') {
                        token.type = Token.TokenType.EQ;  //转换成EQ
                        state = DfaState.EQ;
                        tokenText.append(ch);
                    } else {
                        state = initToken(ch);      //退出Assignment状态，并保存Token
                    }
                	break;
                case EQ:
                	state = initToken(ch);
                	break;
                // 其他符号识别状态
                case Plus:
                case Minus:
                case Star:
                case Slash:
                case SemiColon:
                case Colon:
                case LeftParen:
                case RightParen:
                    state = initToken(ch); 
                    break;
                // 数字字面量识别状态
                case IntLiteral:
                    if (isDigit(ch)) {
                    	// 保持识别状态
                        tokenText.append(ch);
                    } else {
                    	// 退出当前状态，并保存Token
                        state = initToken(ch);      
                    }
                    break;
                // if和int关键字共用的识别状态
                case Id_int1_if1:
                    if (ch == 'n') {
                        state = DfaState.Id_int2;
                        tokenText.append(ch);
                    }
                    else if (ch == 'f') {
                    	state = DfaState.Id_if2;
                    	tokenText.append(ch);
                    }
                    else if (isDigit(ch) || isAlpha(ch)){
                        state = DfaState.Id;    //切换回Id状态
                        tokenText.append(ch);
                    }
                    else {
                        state = initToken(ch);
                    }
                    break;
                // int关键字识别状态
                case Id_int2:
                    if (ch == 't') {
                        state = DfaState.Id_int3;
                        tokenText.append(ch);
                    }
                    else if (isDigit(ch) || isAlpha(ch)){
                        state = DfaState.Id;    //切换回id状态
                        tokenText.append(ch);
                    }
                    else {
                        state = initToken(ch);
                    }
                    break;
                case Id_int3:
                    if (isBlank(ch)) {
                        token.type = Token.TokenType.Int;
                        state = initToken(ch);
                    }
                    else{
                        state = DfaState.Id;    //切换回Id状态
                        tokenText.append(ch);
                    }
                    break;
                // if关键字识别状态
                case Id_if2:
                	if (isBlank(ch)) {
                        token.type = Token.TokenType.If;
                        state = initToken(ch);
                    }
                    else{
                        state = DfaState.Id;    //切换回Id状态
                        tokenText.append(ch);
                    }
                    break;
                // else关键字识别状态
                case Id_else1:
                	if (ch == 'l') {
                        state = DfaState.Id_else2;
                        tokenText.append(ch);
                    }
                    else if (isDigit(ch) || isAlpha(ch)){
                        state = DfaState.Id;
                        tokenText.append(ch);
                    }
                    else {
                        state = initToken(ch);
                    }
                    break;
                case Id_else2:
                	if (ch == 's') {
                        state = DfaState.Id_else3;
                        tokenText.append(ch);
                    }
                    else if (isDigit(ch) || isAlpha(ch)){
                        state = DfaState.Id;
                        tokenText.append(ch);
                    }
                    else {
                        state = initToken(ch);
                    }
                    break;
                case Id_else3:
                	if (ch == 'e') {
                        state = DfaState.Id_else4;
                        tokenText.append(ch);
                    }
                    else if (isDigit(ch) || isAlpha(ch)){
                        state = DfaState.Id;
                        tokenText.append(ch);
                    }
                    else {
                        state = initToken(ch);
                    }
                    break;
                case Id_else4:
                    if (isBlank(ch)) {
                        token.type = Token.TokenType.Else;
                        state = initToken(ch);
                    } else if (ch == ':') {
                    	token.type = Token.TokenType.Else;
                    	state = initToken(ch);
                    	state = DfaState.Colon; 
                    } else{
                        state = DfaState.Id;    //切换回Id状态
                        tokenText.append(ch);
                    }
                    break;
                // 缺省状态
                default:

                }

            }
            // 把最后一个token送进去
            if (tokenText.length() > 0) {
                initToken(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new TokenReader(tokens);
    }
    
    /**
     * 打印所有的Token
     * @param tokenReader
     */
    public void printAll(TokenReader tokenReader){
        System.out.println("Token\t\t类型");
        Token token = null;
        // 输出Token及类型
        while ((token= tokenReader.read())!=null){
            System.out.println(token.getText()+"\t\t"+token.getType());
        }
    }
    
    /**
     * 用枚举类型定义有限状态机的各种状态。
     */
    private enum DfaState {
        Initial,

        If, Id_if2, 
        Else, Id_else1, Id_else2, Id_else3, Id_else4, 
        Int, Id_int1_if1, Id_int2, Id_int3, 
        
        Id, GT, GE, EQ, LT, LE,
        Assignment,

        Plus, Minus, Star, Slash,

        SemiColon,
        LeftParen,
        RightParen,
        Colon,

        IntLiteral
    }

}
