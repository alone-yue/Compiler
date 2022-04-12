package com.company;

import com.company.Typedef;

import java.util.*;

public class FindPredict {
    public ArrayList<Typedef.Production> productionList = new ArrayList<>();
    public HashMap<Typedef.NonTerminalType, HashSet<String>> firstSet = new HashMap<>();
    public HashMap<Typedef.NonTerminalType, HashSet<String>> followSet = new HashMap<>();
    public HashMap<Integer, HashSet<String>> PredictSet = new HashMap<>();//a->A|B
    static public HashMap<String, Integer> Non = new HashMap<>();
    ;
    static public HashMap<String, Integer> Ter = new HashMap<>();
    ;
    public int[][] LL;

    FindPredict(String text) {
        for (int i = 0; i < Typedef.NonTerminalType.values().length; i++) {
            Non.put(Typedef.NonTerminalType.values()[i].toString(), i);
        }
        for (int i = 0; i < Typedef.TerminalType.values().length; i++) {
            Ter.put(Typedef.TerminalType.values()[i].toString(), i);
        }
        //for (String k:Ter.keySet())
        //System.out.println(k);

        productionList.clear();
        firstSet.clear();
        followSet.clear();
        PredictSet.clear();


        Typedef.NonTerminalType leftText = null;
        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.length() < 2)
                continue;
            String[] words = line.split(" ");
            ArrayList<String> wd = new ArrayList<>();
            boolean flag = false;
            for (int i = 0; i < words.length; i++) {
                if (words[i] != "" && words[i] != " " && words[i] != null) {
                    wd.add(words[i]);
                    if (words[i].equals("::="))
                        flag = true;
                }
            }
            /*if(wd==null)
                continue;*/
            if (flag) {//这里使用了异常处理，看看词头是不是非终结符，不是抛出异常。
                /*try {
                    Typedef.NonTerminalType left = Typedef.NonTerminalType.valueOf(wd.get(0));
                    leftText = left;
                    //确定词头
                } catch (Exception e) {
                    System.out.println("ERROR:NonTerminalType");
                    //不是非终结符报错
                    return;
                }*/
                if (Non.containsKey(wd.get(0))) {
                    Typedef.NonTerminalType left = Typedef.NonTerminalType.valueOf(wd.get(0));
                    leftText = left;
                } else {
                    System.out.println("ERROR:NonTerminalType");
                    //不是非终结符报错
                    return;
                }
                firstSet.put(leftText, new HashSet<String>());//给新的左部分加入到first和follow集里
                followSet.put(leftText, new HashSet<String>());
               /* for(String s:wd){
                    if(s.equals("::=")){
                        wd.remove(s);
                    }
                }*/
            }//没扫，左部不变

            if (!wd.isEmpty()) {
                wd.remove(0);//把头给删除了，最后就剩下右部串。或者是把｜给去除了
                if (wd.get(0).equals("::="))
                    wd.remove(0);
                //System.out.println(wd);

                // System.out.println(leftText);
                productionList.add(new Typedef.Production(leftText, wd));//将一系列的推导式子都放在了production里面，left放左部，right放右部
            }

        }
        // System.out.println(productionList.size());

        if (productionList.size() > 0) {
            followSet.get(productionList.get(0).productionLeft).add("#");//加上#
            for (int i = 0; i < productionList.size(); i++) {
                PredictSet.put(i, new HashSet<String>());
            }
            //System.out.println(PredictSet.size());
            //将first，follow还有predict都初始化好了，依次调用三个函数利用production把三个集合求出
            establishFirstSet();
            establishFollowSet();

            establishPredictSet();
            for (int i = 0; i < PredictSet.size(); i++) {
                // System.out.println(i);
                // System.out.println(PredictSet.get(i).size());
                //System.out.println(PredictSet.get(i));
            }
            LL = showTable();
            for (int[] i : LL) {
                for (int j : i) {

                }
                //  System.out.printf("%4d",j);
                // System.out.println();
            }
                /*for(Typedef.NonTerminalType n: Typedef.NonTerminalType.values()){
                    System.out.println(n);
                    System.out.println(followSet.get(n).size());
                    System.out.println(followSet.get(n));
                }
                System.out.println(followSet.size());*/

        }
    }

    public void establishFirstSet() {
        //HashMap<Typedef.NonTerminalType,HashSet<String>>temp=new HashMap<>();
        boolean flag = false;
        int mm = 0;
        while (!flag || mm < 30) {//当没有变化了就说明first集求完了，求first集算是一个np难问题
            flag = true;
            mm++;
            Typedef.NonTerminalType[] elements = Typedef.NonTerminalType.values();
            //对于每一种非终结符的情况
            for (Typedef.NonTerminalType e : elements) {
                //看推导式的左部分
                for (Typedef.Production production : productionList) {
                    //如果对得上，就开始
                    if (production.productionLeft == e) {
                        for (String word : production.productionRight) {
                            if (Non.containsKey(word)) {
                                Typedef.NonTerminalType leftWord = Typedef.NonTerminalType.valueOf(word);
                                int t = firstSet.size();
                                //和右部的每一个非终结符的first集合并，当然不是一次合并就会成功，会循环多次直到不会变化
                                firstSet.get(e).addAll(firstSet.get(leftWord));
                                // System.out.println("!"+e);
                                //合并
                                if (firstSet.get(leftWord).contains(Typedef.NULL)) {
                                    int last = production.productionRight.size() - 1;
                                    if (production.productionRight.get(last) != word) {
                                        // firstSet.get(e).remove(Typedef.NULL);//把别人带来的"空"给删除了，自己的空在最后加入
                                        for (String i : firstSet.get(e)) {
                                            if (i.equals(Typedef.NULL)) {
                                                // System.out.println(i);
                                                //  System.out.print(e);
                                                //System.out.println(firstSet.get(e).size());
                                                firstSet.get(e).remove(i);
                                                // System.out.println(firstSet.get(e).size());
                                                break;
                                            }
                                        }

                                    }
                                    if (t != firstSet.size())
                                        flag = false;
                                } else {
                                    if (t != firstSet.size())
                                        flag = false;
                                    break;
                                }
                            } else {
                                //不是非终结符把头部的单词都放进去，包括NULL
                                if (!firstSet.get(e).contains(word)) {
                                    firstSet.get(e).add(word);//把其他单词加入到first集中
                                    flag = false;
                                    break;
                                }
                                break;
                            }
                            //看右部是不是非终结符
                           /* try {
                               Typedef.NonTerminalType leftWord= Typedef.NonTerminalType.valueOf(word);
                               //和右部的每一个非终结符的first集合并，当然不是一次合并就会成功，会循环多次直到不会变化
                                firstSet.get(e).addAll(firstSet.get(leftWord));
                                //合并
                               if(firstSet.get(leftWord).contains(Typedef.NULL)){
                                   int last=production.productionRight.size()-1;
                                   if(production.productionRight.get(last)!=word){
                                       firstSet.get(e).remove(Typedef.NULL);//把别人带来的"空"给删除了，自己的空在最后加入
                                   }
                               }else{
                                   break;
                               }
                            }catch (Exception ex){
                                //不是非终结符把头部的单词都放进去，包括NULL
                                firstSet.get(e).add(word);//把其他单词加入到first集中
                                break;
                            }*/
                        }
                    }
                }
            }
        }
    }//first集ok

    public void establishFollowSet() {
        // HashMap<Typedef.NonTerminalType,HashSet<String>>temp=new HashMap<>();
        boolean flag = false;
        int mm = 0;
        while (!flag || mm < 50) {
            //System.out.println("?");
            flag = true;
            mm++;
            Typedef.NonTerminalType[] elements = Typedef.NonTerminalType.values();
            for (Typedef.NonTerminalType e : elements) {
                //每个非终结符，优先针对每个非终结符求
                //看每一个产生式，看其中是否有这个非终结符，有就利用来求一下
                for (Typedef.Production production : productionList) {
                    int turn = 1;
                    ArrayList<String> list = new ArrayList<>();
                    //求follow集要每次把每一条都扫一遍，扫到大家都没有更新的了
                    do {
                        list.clear();
                        int tturn = 0;//看对于一个推导式中第几次扫到该非终结符，每次循环一次看第一个。。第二个。。等等
                        boolean isneed = false;//标志着是否为跟在该非终结符后面
                        //看右部
                        for (int i = 0; i < production.productionRight.size(); i++) {
                            String word = production.productionRight.get(i);
                            if (isneed) {//该词的前一个词是目标非终结符
                                list.add(word);
                                //改变了
                                //flag=false;
                            }
                            if (word.equals(e.toString())) {
                                tturn++;
                                if (tturn == turn) {//确认是我们当前要找的那个
                                    isneed = true;

                                    if (i == production.productionRight.size() - 1) {//如果在末尾
                                        int t = followSet.get(e).size();
                                        followSet.get(e).addAll(followSet.get(production.productionLeft));
                                        //最末位，要算上左部都follow集
                                        if (followSet.get(e).size() != t)
                                            flag = false;
                                    }
                                }
                            }
                        }
                        if (list.size() > 0) {//list中剩下跟在目标非终结符后面的非终结符，和终结符单词
                            HashSet<String> set = new HashSet<>();
                            for (int i = 0; i < list.size(); i++) {
                                String word = list.get(i);
                               /* try {
                                    Typedef.NonTerminalType leftWord= Typedef.NonTerminalType.valueOf(word);
                                    set.addAll(firstSet.get(leftWord));//把非终结符的first集合并合并
                                    if(firstSet.get(leftWord).contains(Typedef.NULL)){
                                        if(i<list.size()-1){
                                            set.remove(Typedef.NULL);//first集中把null去除
                                        }
                                    }else {
                                        break;
                                    }
                                }catch (Exception ex){
                                    set.add(word);//把终结符加上
                                    break;
                                }*/
                                if (Non.containsKey(word)) {
                                    Typedef.NonTerminalType leftWord = Typedef.NonTerminalType.valueOf(word);
                                    set.addAll(firstSet.get(leftWord));//把非终结符的first集合并合并
                                    if (firstSet.get(leftWord).contains(Typedef.NULL)) {
                                        if (i < list.size() - 1) {
                                            set.remove(Typedef.NULL);//first集中把null去除
                                        }
                                    } else {
                                        break;
                                    }
                                } else {
                                    set.add(word);//把终结符加上
                                    break;
                                }
                            }
                            if (set.contains(Typedef.NULL)) {
                                int t = followSet.get(e).size();
                                followSet.get(e).addAll(followSet.get(production.productionLeft));//最后还是有null就合并左部分的follow集合
                                set.remove(Typedef.NULL);
                                if (t != followSet.get(e).size()) {
                                    flag = false;
                                }
                            }
                            int t = followSet.get(e).size();
                            followSet.get(e).addAll(set);//把该集合并入到e的非终结符里
                            if (t != followSet.get(e).size()) {
                                flag = false;
                            }
                        }
                        turn++;
                    } while (list.contains(e.toString()));
                }
            }
        }
    }//follow集完成

    public void establishPredictSet() {
        for (int index = 0; index < productionList.size(); index++) {
            Typedef.Production production = productionList.get(index);
            ArrayList<String> list = new ArrayList<>();
            for (String word : production.productionRight) {
                list.add(word);
            }//处理右部
            HashSet<String> set = new HashSet<>();
            for (int i = 0; i < list.size(); i++) {
                String word = list.get(i);
                   /* try {
                        Typedef.NonTerminalType leftWord= Typedef.NonTerminalType.valueOf(word);//如果是非终结符
                        set.addAll(firstSet.get(leftWord));//first集合并入
                        if(firstSet.get(leftWord).contains(Typedef.NULL)){
                            if(i<list.size()-1){//first集有空
                                set.remove(Typedef.NULL);
                                //如果有空才继续看下一个的first，不然就跳过
                            }
                        }else {
                            break;
                        }
                    }catch (Exception e){
                        set.add(word);
                        //加上然后跳出
                        break;
                    }*/
                if (Non.containsKey(word)) {
                    Typedef.NonTerminalType leftWord = Typedef.NonTerminalType.valueOf(word);//如果是非终结符
                    set.addAll(firstSet.get(leftWord));//first集合并入
                    if (firstSet.get(leftWord).contains(Typedef.NULL)) {
                        if (i < list.size() - 1) {//first集有空，而且不是最后一个
                            set.remove(Typedef.NULL);
                            //如果有空才继续看下一个的first，不然就跳过
                        }
                    } else {
                        break;
                    }
                } else {
                    set.add(word);//如果是非终结符
                    //加上然后跳出
                    break;
                }
            }
            if (set.contains(Typedef.NULL)) {
                set.remove(Typedef.NULL);
                PredictSet.get(index).addAll(followSet.get(production.productionLeft));
            }//最后还是有空，看follow集
            PredictSet.get(index).addAll(set);
        }

    }

    public ArrayList<Typedef.Production> showProduction() {
        return productionList;
    }

    public HashMap<Integer, HashSet<String>> showPredict() {
        return PredictSet;
    }

    public int[][] showTable() {


        int[][] a = new int[Typedef.NonTerminalType.values().length][Typedef.TerminalType.values().length];
        for (int[] i : a)
            Arrays.fill(i, -1);
        for (int i = 0; i < PredictSet.size(); i++) {
            for (String p : PredictSet.get(i)) {
                a[Non.get(productionList.get(i).productionLeft.toString())][Ter.get(p)] = i;//左部和其对应推导出的终结符对上号
            }

        }
        return a;
    }
}
