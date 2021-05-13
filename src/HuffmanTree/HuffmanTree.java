package HuffmanTree;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import static HuffmanTree.CONSTANT.*;

class Node<ElemType>{ //赫夫曼树的结点类
    ElemType data;
    int weight;
    Node LChild;
    Node RChild;
    Node Parent;

    public Node(){}
    public Node(ElemType data,int weight){
        this.data = data;
        this.weight = weight;
    }
}

public class HuffmanTree {

    public static void main(String args[]){
        HuffmanTreeOp huffOp = new HuffmanTreeOp<Node>();
        IOUtil ioUtil = new IOUtil(huffOp.getHashMap());
        String ReadInStrs = null;
        String PutOutStrs = null;
        String ReadInBytes = null;
        String values = "";
        String TempRegex = "";
        int[] frequency;
        int Border;
        String Selection;
        String[] Regulation;
        Scanner scanner;
        Scanner ScanRegex;
        String Delimiter;

        System.out.println("You want to decode or what? (Decode,Code)");
        scanner = new Scanner(System.in);
        Selection = scanner.next();
        switch(Selection){
            case "Decode":
                try{
                    ReadInBytes = ioUtil.readInFile(TARGET_PATH,true);
                }catch(IOException ignored){}
                /*split the read in characters*/
                Border = ioUtil.getReadInInfo();

                /*正则表达式处理模块*/
                TempRegex = "";
                String[] reg  = new String[Border];
                assert ReadInBytes != null;
                ScanRegex = new Scanner(ReadInBytes);
                for(int i=0;ScanRegex.hasNext() && i<Border;i++)
                    TempRegex +=  ScanRegex.next();
                ScanRegex = new Scanner(TempRegex);
                ScanRegex.useDelimiter("[:0-9 ]+");
                for(int i=0;ScanRegex.hasNext() && i<Border;i++)
                    values += ScanRegex.next();
                ScanRegex = new Scanner(TempRegex);
                ScanRegex.useDelimiter("[A-Za-z:]+");
                for(int i=0;ScanRegex.hasNext() && i<Border;i++)
                    reg[i] = ScanRegex.next();
                /*end*/

                huffOp.CreateDeHuffmanTree(values,reg);
                /*
                 * Use ReadInBytes to init HuffmanTree.
                 */
                try{
                    ReadInBytes = ioUtil.readInFile(TARGET_PATH,false);
                }catch(IOException ignored){}
                System.out.println("Decoding test:");  //解压缩
                PutOutStrs = huffOp.DecodeHuff(huffOp.DecToBin(ReadInBytes));
                ioUtil.writeLetterToFile(PutOutStrs);
                break;

            case "Code":
                try {
                    ReadInStrs = ioUtil.readInFile(SOURCE_PATH); //把文件中所有的内容读入内存
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("str long:"+ReadInStrs.length());
                frequency = huffOp.CountLetterfrequency(ReadInStrs); //计算ABCDE每个字符出现的次数
                huffOp.CreateHuffCode(huffOp.CreateHuffmanTree(frequency));//生成哈夫曼树
                huffOp.ReplaceLetter(ReadInStrs); //生成编码
                ioUtil.writeInfoToFile();   //将编码信息写入文件头部
                ioUtil.setCodedResultStr(huffOp.getCodedResultStr());
                ioUtil.writeCodeToFile();  //将压缩好的编码写入到文件里
                break;
        }
    }

    void RegexExtract(){

    }

}



class HuffmanTreeOp<ElemType>{
    Node head;
    Node[] nodes;  //哈夫曼树的结点数组
    String ResultStr;
    HashMap<Byte, String> SaveMap; //存储字符对应的赫夫曼编码值

    HuffmanTreeOp(){
        SaveMap = new HashMap<Byte, String>();
    }
    HashMap<Byte,String> getHashMap(){
        return SaveMap;
    }
    String getCodedResultStr(){
        return ResultStr;
    }

    /*Init Huffman Tree*/
    int CreateHuffmanTree(int[] frequency){
        int min1,min2;
        int index1,index2;
        int num;

        /*读入结点个数*/
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input the number of leaves:");
        num = scanner.nextInt();
        /*end*/

        /*初始化树的结点*/
        nodes = new Node[2*num-1];
        for(int i=0;i<2*num-1;i++)
            nodes[i] = new Node<String>();
        /*end*/

        /*给结点赋权值与数值*/
        System.out.println("Please input the weight of the nodes(end with Enter):");
        int k=0;
        for (Byte BtVal:SaveMap.keySet()) {
            nodes[k].weight = frequency[k];
            nodes[k].data = (char)(int)BtVal;
            k++;
        }
        /*end*/

        /*打印结点的权值*/
        for (Node node1:nodes) {
            System.out.println(node1.weight);
        }
        /*end*/

        /*生成HuffmanTree*/
        for(int i=0;i<num-1;i++){
            min1=min2=INFINITY;
            index1=index2=0;
            for(int j=0;j<num+i;j++){//找出所有结点中最小的两个
                if(nodes[j].weight<min1 && nodes[j].Parent==null){
                    min2 = min1;
                    index2 = index1;
                    min1 = nodes[j].weight;
                    index1 = j;
                }
                else if(nodes[j].weight<min2 && nodes[j].Parent==null){
                    min2 = nodes[j].weight;
                    index2 = j;
                }
            }
            nodes[num+i].weight = nodes[index1].weight+nodes[index2].weight;
            nodes[num+i].LChild = nodes[index1];
            nodes[num+i].RChild = nodes[index2];
            nodes[index1].Parent = nodes[num+i];
            nodes[index2].Parent = nodes[num+i];
            System.out.println("x1.weight and x2.weight in round "+i+":"+nodes[index1].weight+","+nodes[index2].weight);
        }
        /*end*/
        return num;
    }

    int CreateDeHuffmanTree(String values,String[] weights){
        Node node;
        Node temp;
        node = new Node();
        head = node;
        for(int i=0;i<values.length();i++){
            for(int j=0;j<weights[i].length();j++){

                if(weights[i].charAt(j) == '0') {
                    if(node.LChild==null) {
                        node.LChild = new Node();
                        node.LChild.Parent = node;
                    }
                    node = node.LChild;
                }
                else if(weights[i].charAt(j) == '1'){
                    if(node.RChild==null) {
                        node.RChild = new Node();
                        node.RChild.Parent = node;
                    }
                    node = node.RChild;
                    node.weight = weights[i].charAt(j);
                }

                if(j+1>=weights[i].length())
                    node.data = values.charAt(i);
            }
            node = head;
        }

        return 7;
    }

    /*init Huffman Code*/
    String CreateHuffCode(int num){
        char[] cd;
        ResultStr = "";
        String TempCodeSaver;
        int start;
        Node node;
        Node child;
        byte BtCodes;
        String StrValues;
        for(int i=0;i<num;i++){
            child = nodes[i];
            node = nodes[i].Parent;
            cd = new char[num];
            start = 0;
            while(node!=null){
                if(node.LChild == child)
                    cd[start] = '0';
                else
                    cd[start] = '1';
                child = node;
                node = node.Parent;
                start++;
            }
            for (int j=cd.length-1;j>=0;j--) //print each code on screen
                System.out.print(cd[j]);
            System.out.println();
            for(int j=start-1;j>=0;j--) //Combine the codes
                ResultStr += cd[j];
            /*save each letter's codes*/
            TempCodeSaver = "";
            for(int j=start-1;j>=0;j--)
                TempCodeSaver += cd[j];
            //save into HashMap
            StrValues = TempCodeSaver;
            BtCodes = (byte)(char)nodes[i].data;
            SaveMap.put(BtCodes,StrValues);
            /*end of saving*/
        }
        System.out.println("The total:"+ResultStr);

        return null;
    }

    /*Replace letters with Huffman Code*/
    String ReplaceLetter(String letters){
        ResultStr = "";
        for(int i=0;i<letters.length();i++){
            byte BtCode = (byte)letters.charAt(i);
            ResultStr += SaveMap.get(BtCode);
        }
        return ResultStr;
    }

    int[] CountLetterfrequency(String ReadInStrs){
        int counts;
        int[] LetterCounter = {0,0,0,0,0,0};
//        int[] LetterCounter = new int[256];
        byte[] conByte = ReadInStrs.getBytes();
        for(int i=0;i<ReadInStrs.length();i++){
            String isRecord = SaveMap.get(conByte[i]);
            if(isRecord == null)
                SaveMap.put(conByte[i],"1");
            else {
                counts = Integer.parseInt(isRecord);
                counts++;
                isRecord = String.valueOf(counts);
                SaveMap.put(conByte[i], isRecord);
            }
        }
        LetterCounter = new int[SaveMap.size()];
//        Byte BKey = 65;
//        for(int i=0;i<LetterCounter.length;i++)
//            LetterCounter[i] = Integer.parseInt(SaveMap.get(BKey++));
        int i=0;
        for(Object key:SaveMap.keySet())
        {
            LetterCounter[i] = Integer.parseInt(SaveMap.get(key));
            System.out.println("Key: "+ key +" Value: "+SaveMap.get(key));
            i++;
        }
        System.out.println("test:"+SaveMap.size());
        System.out.println("We have "+LetterCounter[0]+" A:");
        System.out.println("We have "+LetterCounter[1]+" B:");
        System.out.println("We have "+LetterCounter[2]+" C:");
        System.out.println("We have "+LetterCounter[3]+" D:");
        System.out.println("We have "+LetterCounter[4]+" E:");
        System.out.println("We have "+LetterCounter[5]+" F:");
        return LetterCounter;
    }


    String DecodeHuff(String DecodeStr){
//        DecodeStr = ResultStr;
        String result = "";
        Node root = head;
        Node temp;
        while(root.Parent != null) //find root
            root = root.Parent;
        temp = root;
        for(int i=0;i<DecodeStr.length();i++){
            if(DecodeStr.charAt(i) == '0')
                temp = temp.LChild;
            else
                temp = temp.RChild;
            if(temp.data != null) {
                System.out.print(temp.data);
                result += temp.data;
                temp = root;
            }
        }
        System.out.println();
        return result;
    }

    int BinToDec(String str){
        return ((int)str.charAt(0)-48)*128+
                ((int)str.charAt(1)-48)*64+
                ((int)str.charAt(2)-48)*32+
                ((int)str.charAt(3)-48)*16+
                ((int)str.charAt(4)-48)*8+
                ((int)str.charAt(5)-48)*4+
                ((int)str.charAt(6)-48)*2+
                ((int)str.charAt(7)-48);
    }

    String DecToBin(String DecStr){
        String DecodeStr = "";
        String tempStr = "";
        for(int i=0;i<DecStr.length();i++){
            tempStr = Integer.toBinaryString((int)DecStr.charAt(i));
            while(tempStr.length()<8)
                tempStr = '0'+tempStr;
            DecodeStr += tempStr;
        }
        return DecodeStr;
    }

}

interface CONSTANT{
    int INFINITY = 65535;
    String SOURCE_PATH = "PATH/TEST.txt";
    String TARGET_PATH ="PATH/compress.huffman";
    String DECODE_PATH = "PATH/decode.txt";
}
