package com.company;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        String TextFilePath = args[0];
        String ProductionPath = args[1];
        String tokenPath = args[2];
        String treePath = args[3];
        String LL1Path = args[4];

        File TextFile = new File(TextFilePath);//读取文件
        File Production = new File(ProductionPath); //读取文件
        if (!TextFile.exists()) {
            System.out.println("文件不存在");
        }
        BufferedReader reader = null;
        StringBuffer a = new StringBuffer();
        StringBuffer rules = new StringBuffer();
        try (BufferedReader bufferedReader = reader = new BufferedReader(new FileReader(TextFile))) {

            String str;
            while ((str = reader.readLine()) != null) {
                a.append(str + "\n");
            }
            //System.out.print(a.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader bufferedReader = reader = new BufferedReader(new FileReader(Production))) {
            String str;
            while ((str = reader.readLine()) != null) {
                rules.append(str + "\n");
            }
            // System.out.println(product);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // write your code here
        /*String codes="{实现冒泡排序算法的SNL程序}\n" +
                "        program bubble\n" +
                "            var integer i,j,num;\n" +
                "            array [1..20] of integer a;";
        if(codes!=null&&!"".equals(codes)) {
            String result= codes.replaceAll("\\{[\\s\\S]*?\\}","");
           System.out.println(result);
        }
        String word="program";
        for (String key:Typedef.reservedWords.keySet()){
            if(word==key){
                Typedef.Token tmp= new Typedef.Token(Typedef.reservedWords.get(key),"",0,0);
                //System.out.println(Typedef.reservedWords.get(key));
            }
        }//传进来的是保留字
        for(String key:Typedef.discriminateType.keySet()){
            String data=word.replaceAll(key,"");

            if (data.length()==0){
                Typedef.Token tmp= new Typedef.Token(Typedef.discriminateType.get(key),word,0,0);
                //System.out.println(Typedef.discriminateType.get(key));
            }
    }
        DFA a=new DFA();
        a.scan(codes);
        //System.out.println(a.Tokens.size());
        for (int i=0;i<a.Tokens.size();i++){
           //System.out.println(a.Tokens.get(i).type);
        }
        try {
            Typedef.NonTerminalType.valueOf(word);
        }catch (Exception e){

        }*/
        DFA theDFA = new DFA();

        theDFA.scan(a.toString());
        for (Typedef.Token t : theDFA.Tokens) {
            // System.out.println(t.type+" "+t.data+" "+t.line+" "+t.column);
        }
        // System.out.println(theDFA.Tokens.size());
        try {
            File file = new File(tokenPath);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            for (Typedef.Token t : theDFA.Tokens) {
                // System.out.println(t.type+" "+t.data+" "+t.line+" "+t.column);
                //ps.println(t.type+" "+t.data+" "+t.line+" "+t.column);
                ps.printf("%-10s %-6s%3d%3d\n", t.type, t.data, t.line, t.column);//输出产生tokens
            }
            // ps.println("http://www.jb51.net");// 往文件里写入字符串
            // ps.append("http://www.jb51.net");// 在已有的基础上添加字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        FindPredict predict = new FindPredict(rules.toString());
        //System.out.println(predict.firstSet.isEmpty());
        SyntaxParser parser = new SyntaxParser(theDFA.Tokens, predict.productionList, predict.LL);//语法分析
        for (int i = 0; i < parser.Ps.size(); i++) {
            System.out.println(parser.Ps.get(i).productionLeft);
        }
        // System.out.println(parser.Ps.size());
        //System.out.println(parser.psIndex);
        try {
            File file = new File(treePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            if (parser.errorString == null)
                ps.println(parser.showTree());
            else
                ps.println(parser.errorString);
            // ps.println("http://www.jb51.net");// 往文件里写入字符串
            // ps.append("http://www.jb51.net");// 在已有的基础上添加字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            File file = new File(LL1Path);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            for (int i = 0; i < parser.LLTable.length; i++) {
                for (int j = 0; j < parser.LLTable[0].length; j++) {
                    ps.printf("%4d", parser.LLTable[i][j]);
                }
                ps.println();
            }
            //ps.println(parser.showTree());
            // ps.println("http://www.jb51.net");// 往文件里写入字符串
            // ps.append("http://www.jb51.net");// 在已有的基础上添加字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(parser.cnt);
    }
}
