package com.company;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

//aaaaaaa
public class SyntaxParser {
    public ArrayList<Typedef.Token> tokens;//词法分析扫入的代码。。词序列
    public int tokensIndex = 0;//语法树中找到一个终结符。。对应一个token词。。用于指向token词数到哪了
    public ArrayList<Typedef.Production> ProductionList;//推导式序列
    public int[][] LLTable;//之前得到的ll1分析表
    public ArrayList<Typedef.Production> Ps = new ArrayList<>();//语法分许过程中按照scope栈的顺序得到的推导式子序列
    public int psIndex = 0;//推导式用到哪了
    public Typedef.Node root;//语法树的根
    public String errorString;//错误。。没有错误则是null
    public int cnt = 0;

    SyntaxParser(ArrayList<Typedef.Token> Tokens, ArrayList<Typedef.Production> ProductionList, int[][] LLTable) {
        this.tokens = Tokens;
        this.ProductionList = ProductionList;
        this.LLTable = LLTable;
        ProductionAnalyze();
        buildTree();


    }

    public void ProductionAnalyze() {//分析的过程，模拟scope栈
        if (errorString != null) {
            return;
        }
        Deque<String> S = new LinkedList<>();//A,a,Program
        if (ProductionList != null) {
            Typedef.Production Start = ProductionList.get(0);
            int index = 0;//index是指示扫到了那个token，token对应终结符
            S.addLast(Start.productionLeft.toString());
            while (!S.isEmpty()) {
                String s = S.peekLast();
                if (index < tokens.size()) {
                    /*try {
                        //看栈顶是终结符还是非终结符
                        Typedef.NonTerminalType nt= Typedef.NonTerminalType.valueOf(s);
                        int i=LLTable[FindPredict.Non.get(s)][FindPredict.Ter.get(tokens.get(index).type.toString())];
                        if(i>-1){//说明对上了，是当前非终结符的predict集合之中的元素，替换为推导式右部中的元素，从后向前依次压入
                            Ps.add(ProductionList.get(i));//记录
                            S.pollLast();
                            for(int j=ProductionList.get(i).productionRight.size();j>=0;j--){
                                S.addLast(ProductionList.get(i).productionRight.get(j));
                            }
                        }else {
                            errorString = "ERROR_Production: Tokens[("+index+")] AND ("+s+") HAVE NO PRODUCTION";
                            return;
                        }
                    }catch (Exception e){
                        try {
                            Typedef.TerminalType tt= Typedef.TerminalType.valueOf(s);
                            if(tokens.get(index).type==tt){
                                S.pollLast();
                                index++;//对上了一个终结符token才向后移动
                            }else {
                                errorString = "ERROR_Match: Tokens[("+index+")] AND ("+s+") DO NOT MATCH";
                                return;
                            }
                        }catch (Exception ex){
                            S.pollLast();
                        }
                    }*/
                    if (FindPredict.Non.containsKey(s)) {
                        //看栈顶是终结符还是非终结符
                        //是非终结符
                        //token,当前的token
                        Typedef.NonTerminalType nt = Typedef.NonTerminalType.valueOf(s);
                        int i = LLTable[FindPredict.Non.get(s)][FindPredict.Ter.get(tokens.get(index).type.toString())];
                        if (i > -1) {//说明对上了，是当前非终结符的predict集合之中的元素，替换为推导式右部中的元素，从后向前依次压入
                            Ps.add(ProductionList.get(i));//记录
                            S.pollLast();//把非终结符弹出
                            for (int j = ProductionList.get(i).productionRight.size() - 1; j >= 0; j--) {//反向压入右部中的推导
                                S.addLast(ProductionList.get(i).productionRight.get(j));
                            }
                        } else {//没能对应上说明出现了错误
                            errorString = "ERROR_Production: Tokens[(" + index + ")] AND (" + s + ") HAVE NO PRODUCTION";
                            return;
                        }
                    } else if (FindPredict.Ter.containsKey(s)) {
                        //栈顶是终结符，终结符就跟当前的token对上
                        Typedef.TerminalType tt = Typedef.TerminalType.valueOf(s);
                        if (tokens.get(index).type == tt) {
                            S.pollLast();
                            index++;//对上了一个终结符token才向后移动
                        } else {
                            errorString = "ERROR_Match: Tokens[(" + index + ")] AND (" + s + ") DO NOT MATCH";
                            return;
                        }
                    } else {
                        S.pollLast();
                    }
                } else {
                    errorString = "ERROR_Tokens: Tokens NOT ENOUGH";
                    return;//token不够了
                }
            }
        }
    }//scope栈好了，Ps也准备好

    public void recursionForTree(Typedef.Node node) {
        if (errorString != null)
            return;
        //psIndex是指着初始按照scope栈找到的非终结符顺序
        for (String s : Ps.get(psIndex).productionRight) {
            if (s == null)
                continue;
            /*try {//是终结符
                Typedef.TerminalType t= Typedef.TerminalType.valueOf(s);
                node.children.add(new Typedef.Node(node,new ArrayList<Typedef.Node>(),t,null,""));
            }catch (Exception e){
                    try {
                        //是非终结符
                        Typedef.NonTerminalType n= Typedef.NonTerminalType.valueOf(s);
                        node.children.add(new Typedef.Node(node,new ArrayList<Typedef.Node>(),null,n,""));
                    }catch (Exception ex){
                        errorString = "ERROR_Production: PRODUCTION ERROR";
                        return;
                    }
            }*/
            if (FindPredict.Ter.containsKey(s)) {//该子结点是终结符，生成结点加上
                Typedef.TerminalType t = Typedef.TerminalType.valueOf(s);
                node.children.add(new Typedef.Node(node, new ArrayList<Typedef.Node>(), t, null, ""));
            } else if (FindPredict.Non.containsKey(s)) {
                //该子结点是非终结符，生成结点加上
                Typedef.NonTerminalType n = Typedef.NonTerminalType.valueOf(s);
                node.children.add(new Typedef.Node(node, new ArrayList<Typedef.Node>(), null, n, ""));
            } else if (s.equals(Typedef.NULL)) {

            } else {
                System.out.println(s);
                errorString = "ERROR_Production: PRODUCTION ERROR";
                return;
            }
        }
        psIndex += 1;//已经读入了一个推导式
        for (Typedef.Node n : node.children) {
            if (n.tType == null) {
                recursionForTree(n);
            } else {
                n.data = tokens.get(tokensIndex).data;
                //对于终结符结点，即语法树中的叶子结点，找到了结点对应token中的终结符信息，把token串中的data放入结点
                tokensIndex++;
            }
        }
    }

    //
    public void buildTree() {
        if (errorString != null)
            return;
        if (Ps.isEmpty())
            return;
        root = new Typedef.Node(null, new ArrayList<Typedef.Node>(), null, Ps.get(0).productionLeft, "");
        for (String s : Ps.get(0).productionRight) {
            if (FindPredict.Ter.containsKey(s)) {//该子结点是终结符，生成结点加上
                Typedef.TerminalType t = Typedef.TerminalType.valueOf(s);
                root.children.add(new Typedef.Node(root, new ArrayList<Typedef.Node>(), t, null, ""));
            } else if (FindPredict.Non.containsKey(s)) {//该子结点是非终结符，生成结点加上
                Typedef.NonTerminalType n = Typedef.NonTerminalType.valueOf(s);
                root.children.add(new Typedef.Node(root, new ArrayList<Typedef.Node>(), null, n, ""));
            } else {
                errorString = "ERROR_Production: PRODUCTION ERROR";
                return;
            }
        }//把root的子结点加上
        tokensIndex = 0;//为什么tokensIndex从0开始。。因为主推导式中只有最后一个是终结符DOT
        psIndex = 1;
        for (Typedef.Node child : root.children) {
            if (child.nType != null) {//是非终结符，拓展
                recursionForTree(child);
            }
        }
    }

    //通过build——tree我们产生了语法树，叶子结点为终结符，其余父亲结点为非终结符
    public Typedef.Node OfferRoot() {
        return root;
    }

    //显示语法树，通过深搜语法树，显示出得到语法树字符串
    public StringBuffer getNode(String str, StringBuffer append, Typedef.Node node) {
        StringBuffer b = append;
        if (node.tType != null) {

            b.append(node.tType.toString());
        } else if (node.nType != null) {

            b.append(node.nType.toString());
        }
        if (!node.data.equals("")) {
            b.append("(" + node.data + ")");
        }
        b.append("\n");
        if (!node.children.isEmpty()) {
            cnt++;
            int len = node.children.size();
            for (int i = 0; i < node.children.size(); i++) {
                b.append(str);
                StringBuffer buff = getNode(str + "    | ", new StringBuffer().append("    |-"), node.children.get(i));
                b.append(buff);
            }
        }
        return b;
    }

    public String showTree() {
        if (errorString != null) {
            return errorString;
        } else {
            return getNode("", new StringBuffer(), root).toString();
        }
    }
}

