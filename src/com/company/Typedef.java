package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Typedef {
  static public enum TerminalType {
        // 特殊单词符号
     ENDFILE,
     ERROR,
        // 保留字
     PROGRAM,
     PROCEDURE,
     TYPE,
     VAR,
     IF,
     THEN,
     ELSE,
     FI,
     WHILE,
     DO,
     ENDWH,
     BEGIN,
     END,
     READ,
     WRITE,
     ARRAY,
     OF,
     RECORD,
     RETURN,
     INTEGER,
     CHAR,
        // 多字符单词
     ID,
     INTC,
     CHARC,
        // 符号
     ASSIGN,
     EQ,
     LT,
     PLUS,
     MINUS,
     TIMES,
     OVER,
     LPAREN,
     RPAREN,
     DOT,
     COLON,
     SEMI,
     COMMA,
     LMIDPAREN,
     RMIDPAREN,
     UNDERANGE;
    }
 public static enum NonTerminalType {
         Program,
     ProgramHead,
     ProgramName,
     DeclarePart,
     TypeDec,
     TypeDeclaration,
     TypeDecList,
     TypeDecMore,
     TypeId,
     TypeName,
     BaseType,
     StructureType,
     ArrayType,
     Low,
     Top,
     RecType,
     FieldDecList,
     FieldDecMore,
     IdList,
     IdMore,
     VarDec,
     VarDeclaration,
     VarDecList,
     VarDecMore,
     VarIdList,
     VarIdMore,
     ProcDec,
     ProcDeclaration,
     ProcDecMore,
     ProcName,
     ParamList,
     ParamDecList,
     ParamMore,
     Param,
     FormList,
     FidMore,
     ProcDecPart,
     ProcBody,
     ProgramBody,
     StmList,
     StmMore,
     Stm,
     AssCall,
     AssignmentRest,
     ConditionalStm,
     LoopStm,
     InputStm,
     Invar,
     OutputStm,
     ReturnStm,
     CallStmRest,
     ActParamList,
     ActParamMore,
     RelExp,
     OtherRelE,
     Exp,
     OtherTerm,
     Term,
     OtherFactor,
     Factor,
     Variable,
     VariMore,
     FieldVar,
     FieldVarMore,
     CmpOp,
     AddOp,
     MultOp;
  }
  public final static String NULL="ε";
public static class Token{
    TerminalType type;
    String data;
    int line;
    int column;
    Token(TerminalType type,String data,int line,int column){
        this.type=type;
        this.data=data;
        this.line=line;
        this.column=column;
    }
   }
  public static HashMap<String,TerminalType>reservedWords=new HashMap<>(){
    {
      put("program",TerminalType.PROGRAM);
      put("type",TerminalType.TYPE);
      put("var",TerminalType.VAR);
      put("procedure",TerminalType.PROCEDURE);
        put("begin",TerminalType.BEGIN);
        put("end",TerminalType.END);
        put("array",TerminalType.ARRAY);
        put("of",TerminalType.OF);
        put("record",TerminalType.RECORD);
        put("if",TerminalType.IF);
        put("then",TerminalType.THEN);
        put("else",TerminalType.ELSE);
        put("fi",TerminalType.FI);
        put("while",TerminalType.WHILE);
        put("do",TerminalType.DO);
        put("endwh",TerminalType.ENDWH);
        put("read",TerminalType.READ);
        put("write",TerminalType.WRITE);
        put("return",TerminalType.RETURN);
        put("integer",TerminalType.INTEGER);
        put("char",TerminalType.CHAR);
    }
};
    public static HashMap<Character,TerminalType>symbolWord=new HashMap<>(){
        {
            put('=',TerminalType.EQ);
            put('<',TerminalType.LT);
            put('+',TerminalType.PLUS);
            put('-',TerminalType.MINUS);
            put('*',TerminalType.TIMES);
            put('/',TerminalType.OVER);
            put('(',TerminalType.LPAREN);
            put(')',TerminalType.RPAREN);
            put('.',TerminalType.DOT);
            put(':',TerminalType.COLON);
            put(';',TerminalType.SEMI);
            put(',',TerminalType.COMMA);
            put('[',TerminalType.LMIDPAREN);
            put(']',TerminalType.RMIDPAREN);
        }
    };
    public static HashMap<String,TerminalType>discriminateType=new HashMap<>(){
        {
            put( "\\d+",TerminalType.INTC);
            put("\'.{1}\'",TerminalType.CHAR);
            put("[a-zA-Z_][a-zA-Z_0-9]*",TerminalType.ID);
        }
    };
   public static class Production{
       NonTerminalType  productionLeft;
       ArrayList<String> productionRight;//不用构造？？
       Production(NonTerminalType productionLeft,ArrayList<String> productionRight){
           this.productionLeft=productionLeft;
           this.productionRight=productionRight;
       }
   }
   static public class Node{
        Node parentNode;
        ArrayList<Node>children;
        TerminalType tType;
        NonTerminalType nType;
        String data="";
        Node(Node parentNode,ArrayList<Node>children,TerminalType tType,NonTerminalType nType,String data){
                this.parentNode=parentNode;
                this.children=children;
                this.data=data;
                this.nType=nType;
                this.tType=tType;
        }
    }

}
