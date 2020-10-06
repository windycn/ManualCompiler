package myCompile;

import java.io.CharArrayReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * �ʷ�������
 * ���Token��
 * @author ����
 */
public class theCompiler {
	/** ����ʷ�����ʹ�õı��� */
	// ��ʱ����Token�ı�
	private StringBuffer tokenText = null;
	// ����������Token
	private List<Token> tokens = null;
	// ���浱ǰ���ڽ�����Token
	private Token token = null;
	
	/** �����ݵ��жϷ��������ٴ������� */
	//�Ƿ�����ĸ
    private boolean isAlpha(int ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }
    //�Ƿ�������
    private boolean isDigit(int ch) {
        return ch >= '0' && ch <= '9';
    }
    //�Ƿ��ǿհ��ַ�
    private boolean isBlank(int ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }
	
    /**
     * ���������Զ������������ʼ״̬
     * �����ʼ״̬������Ͻ�������״̬��
     * ��ʼ������ʱ�򣬽����ʼ״̬��
     * ĳ��Token������ϣ�Ҳ�����ʼ״̬���������Token��������Ȼ����һ���µ�Token��
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
        // ������ĸƥ��״̬
        if (isAlpha(ch)) {
            if (ch == 'i') {
            	// ����ַ�Ϊi�������int/if�ؼ��ֵ�ƥ��
                newState = DfaState.Id_int1_if1;
            } else if (ch == 'e') {
            	// ����ַ�Ϊe�������else�ؼ��ֵ�ƥ��
            	newState = DfaState.Id_else1;
            } else {
                newState = DfaState.Id; //����Id״̬
            }
            token.type = Token.TokenType.Identifier;
            tokenText.append(ch);
        } 
        // ��������ƥ��״̬
        else if (isDigit(ch)) { 
            newState = DfaState.IntLiteral;
            token.type = Token.TokenType.IntLiteral;
            tokenText.append(ch);
        }
        // ���ν������ƥ��
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
        	 // ��������δ֪����
            newState = DfaState.Initial;
        }
        return newState;
    }
    
    /**
     * �����ַ������γ�Token��
     * ����һ������״̬�Զ������ڲ�ͬ��״̬��Ǩ�ơ�
     * @param code
     * @return
     */
    public TokenReader tokenize(String code) {
        tokens = new ArrayList<Token>();
        // toCharArray()�������ַ���ת��Ϊ�ַ�����
        // CharArrayReader��ʵ��һ���������ַ����������ַ�������
        CharArrayReader reader = new CharArrayReader(code.toCharArray());
        tokenText = new StringBuffer();
        token = new Token();
        int ich = 0;
        char ch = 0;
        // ��ʼ�������Զ���״̬
        DfaState state = DfaState.Initial;
        try {
        	// java.io.CharArrayReader.read()���ڶ�ȡ�����ַ�
        	// ����ֵΪ��������ʾ��ȡ���ַ���������������򷵻�-1
            while ((ich = reader.read()) != -1) {
            	// ǿ������ת��Ϊ�ַ�����
                ch = (char) ich;
                switch (state) {
                // ����ȷ����ʼ״̬
                case Initial:
                    state = initToken(ch);
                    break;
                // ��ʶ��ʶ��״̬
                case Id:
                    if (isAlpha(ch) || isDigit(ch)) {
                    	//���ֱ�ʶ��״̬
                        tokenText.append(ch);       
                    } else {
                    	//�˳���ʶ��״̬��������Token
                        state = initToken(ch);      
                    }
                    break;
                // >/>=ʶ��״̬
                case GT:
                	// ƥ���Ƿ�ΪGE(>=)״̬
                    if (ch == '=') {
                    	// �л�״̬ΪGE
                        token.type = Token.TokenType.GE;
                        state = DfaState.GE;
                        tokenText.append(ch);
                    } else {
                    	 // �˳�GT(>)״̬��������Token
                        state = initToken(ch);   
                    }
                    break;
                case GE:
                	state = initToken(ch);
                	break;
                // </<=ʶ��״̬
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
                // =/==ʶ��״̬
                case Assignment:
                	if (ch == '=') {
                        token.type = Token.TokenType.EQ;  //ת����EQ
                        state = DfaState.EQ;
                        tokenText.append(ch);
                    } else {
                        state = initToken(ch);      //�˳�Assignment״̬��������Token
                    }
                	break;
                case EQ:
                	state = initToken(ch);
                	break;
                // ��������ʶ��״̬
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
                // ����������ʶ��״̬
                case IntLiteral:
                    if (isDigit(ch)) {
                    	// ����ʶ��״̬
                        tokenText.append(ch);
                    } else {
                    	// �˳���ǰ״̬��������Token
                        state = initToken(ch);      
                    }
                    break;
                // if��int�ؼ��ֹ��õ�ʶ��״̬
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
                        state = DfaState.Id;    //�л���Id״̬
                        tokenText.append(ch);
                    }
                    else {
                        state = initToken(ch);
                    }
                    break;
                // int�ؼ���ʶ��״̬
                case Id_int2:
                    if (ch == 't') {
                        state = DfaState.Id_int3;
                        tokenText.append(ch);
                    }
                    else if (isDigit(ch) || isAlpha(ch)){
                        state = DfaState.Id;    //�л���id״̬
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
                        state = DfaState.Id;    //�л���Id״̬
                        tokenText.append(ch);
                    }
                    break;
                // if�ؼ���ʶ��״̬
                case Id_if2:
                	if (isBlank(ch)) {
                        token.type = Token.TokenType.If;
                        state = initToken(ch);
                    }
                    else{
                        state = DfaState.Id;    //�л���Id״̬
                        tokenText.append(ch);
                    }
                    break;
                // else�ؼ���ʶ��״̬
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
                        state = DfaState.Id;    //�л���Id״̬
                        tokenText.append(ch);
                    }
                    break;
                // ȱʡ״̬
                default:

                }

            }
            // �����һ��token�ͽ�ȥ
            if (tokenText.length() > 0) {
                initToken(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new TokenReader(tokens);
    }
    
    /**
     * ��ӡ���е�Token
     * @param tokenReader
     */
    public void printAll(TokenReader tokenReader){
        System.out.println("Token\t\t����");
        Token token = null;
        // ���Token������
        while ((token= tokenReader.read())!=null){
            System.out.println(token.getText()+"\t\t"+token.getType());
        }
    }
    
    /**
     * ��ö�����Ͷ�������״̬���ĸ���״̬��
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
