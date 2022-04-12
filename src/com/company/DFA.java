package com.company;
import com.company.Typedef;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DFA {
  public   int currentLine=0;
  public   int currentColumn=0;
  public   ArrayList<Typedef.Token>Tokens=new ArrayList<>();
    public void scan(String codes){
        currentColumn=0;
        currentLine=0;
        Tokens.clear();
        StringBuilder tmpString=new StringBuilder();
        char tmpChar=' ';
        String code=removeComment(codes);
        String[]lines=code.split("\n");
        for(String line:lines){
            currentLine+=1;
            currentColumn=0;
            String[]words=line.split(" ");
            for(String word:words){
                String wd=word.trim();
                if (wd==" "||word=="") continue;
                tmpString.delete(0,tmpString.length());
                tmpChar=' ';
             first:   for(int i=0;i<wd.length();i++){
                    char c=wd.charAt(i);
                    switch (c){
                        case ':'://1
                                if(tmpString.length()!=0){
                                    Tokens.add(discriminateWord(tmpString.toString()));
                                    tmpString.delete(0,tmpString.length());
                                }
                                switch (tmpChar){
                                    case ':':currentColumn+=1;
                                             Tokens.add(new Typedef.Token(Typedef.TerminalType.COLON,"",currentLine,currentColumn));
                                             currentColumn+=1;
                                             Tokens.add(new Typedef.Token(Typedef.TerminalType.COLON,"",currentLine,currentColumn));
                                             tmpChar=' ';
                                    case '.':currentColumn+=1;
                                             Tokens.add(new Typedef.Token(Typedef.TerminalType.DOT,"",currentLine,currentColumn));
                                             currentColumn+=1;
                                             Tokens.add(new Typedef.Token(Typedef.TerminalType.COLON,"",currentLine,currentColumn));
                                             tmpChar=' ';
                                    default:
                                        tmpChar=':';
                                }
                                break ;
                        case '=':
                                 if(tmpString.length()!=0){
                                       Tokens.add(discriminateWord(tmpString.toString()));
                                       tmpString.delete(0,tmpString.length());
                                 }
                                 switch (tmpChar){
                                  case ':':
                                    currentColumn+=1;
                                    Tokens.add(new Typedef.Token(Typedef.TerminalType.ASSIGN,"",currentLine,currentColumn));
                                    break ;
                                  case '.':
                                    currentColumn+=1;
                                    Tokens.add(new Typedef.Token(Typedef.TerminalType.DOT,"",currentLine,currentColumn));
                                    currentColumn+=1;
                                    Tokens.add(new Typedef.Token(Typedef.TerminalType.EQ,"",currentLine,currentColumn));
                                    break ;
                                  default:
                                    currentColumn+=1;
                                    Tokens.add(new Typedef.Token(Typedef.TerminalType.EQ,"",currentLine,currentColumn));
                                 }
                                 tmpChar=' ';
                                 break ;
                        case '.':
                                  if(tmpString.length()!=0){
                                       Tokens.add(discriminateWord(tmpString.toString()));
                                       tmpString.delete(0,tmpString.length());
                                  }
                                  switch (tmpChar){
                                      case':':
                                        currentColumn+=1;
                                        Tokens.add(new Typedef.Token(Typedef.TerminalType.COLON,"",currentLine,currentColumn));
                                        currentColumn+=1;
                                        Tokens.add(new Typedef.Token(Typedef.TerminalType.DOT,"",currentLine,currentColumn));
                                        tmpChar=' ';
                                        break ;
                                      case'.':
                                         currentColumn+=1;
                                         Tokens.add(new Typedef.Token(Typedef.TerminalType.UNDERANGE,"",currentLine,currentColumn));
                                         tmpChar=' ';
                                         break ;
                                      default:
                                          tmpChar='.';
                                  }
                                  break ;
                        default:
                            switch (tmpChar){
                                case ':':
                                    if(tmpString.length()!=0){
                                        Tokens.add(discriminateWord(tmpString.toString()));
                                        tmpString.delete(0,tmpString.length());
                                    }
                                    currentColumn+=1;
                                    Tokens.add(new Typedef.Token(Typedef.TerminalType.COLON,"",currentLine,currentColumn));
                                    break ;
                                case '.':
                                    if(tmpString.length()!=0){
                                        Tokens.add(discriminateWord(tmpString.toString()));
                                        tmpString.delete(0,tmpString.length());
                                    }
                                    currentColumn+=1;
                                    Tokens.add(new Typedef.Token(Typedef.TerminalType.DOT,"",currentLine,currentColumn));
                                    break ;
                                default:
                                    for(char key:Typedef.symbolWord.keySet()){
                                        if(key==c){
                                            if(tmpString.length()!=0){
                                                Tokens.add(discriminateWord(tmpString.toString()));
                                                tmpString.delete(0,tmpString.length());
                                            }
                                            currentColumn+=1;
                                            Tokens.add(new Typedef.Token(Typedef.symbolWord.get(key),"",currentLine,currentColumn));
                                            continue first;
                                        }
                                    }
                                    tmpString.append(c);
                            }
                           tmpChar=' ';
                    }
                }
                if(tmpString.length()!=0){
                    Tokens.add(discriminateWord(tmpString.toString()));
                }
                switch (tmpChar){
                    case ':':
                        currentColumn+=1;
                        Tokens.add(new Typedef.Token(Typedef.TerminalType.COLON,"",currentLine,currentColumn));
                        break;
                    case '.':
                        currentColumn+=1;
                        Tokens.add(new Typedef.Token(Typedef.TerminalType.DOT,"",currentLine,currentColumn));
                        break;
                    default:
                        break;
                }
            }
        }




    }

    public String removeComment(String codes){
        if(codes!=null&&!"".equals(codes)) {
           String result= codes.replaceAll("\\{[\\s\\S]*?\\}","");
           return result;
        }
        return "";
    }


    public Typedef.Token discriminateWord(String word){
        currentColumn+=1;
       for (String key:Typedef.reservedWords.keySet()){
           if(word.equals(key)){
               return new Typedef.Token(Typedef.reservedWords.get(key),"",currentLine,currentColumn);
           }
       }//传进来的是保留字
       for(String key:Typedef.discriminateType.keySet()){
           String data=word.replaceAll(key,"");
           if (data.equals("")){
               return  new Typedef.Token(Typedef.discriminateType.get(key),word,currentLine,currentColumn);
           }
       }//传进来的是字符或者是数字
        return new Typedef.Token(Typedef.TerminalType.ERROR,"",currentLine,currentColumn);
    }



    public ArrayList<Typedef.Token>showTokens(){


        return Tokens;
    }

}
