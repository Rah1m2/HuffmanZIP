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
        String ReadInStrs = null;
        String PutOutStrs = null;
        String ReadInBytes = null;
        String values = "ABCDE";
        int[] frequency;
        String Selection;
        String[] Regulation;
        Scanner scanner;

        System.out.println("You want to decode or what? (Decode,Code)");
        scanner = new Scanner(System.in);
        Selection = scanner.next();
        switch(Selection){
            case "Decode":
                try{
                    ReadInBytes = huffOp.readInFile(TARGET_PATH,true);
                }catch(IOException ignored){}
                /*split the read in characters*/
                Regulation = ReadInBytes.split("[A-Z:]+");
                String[] reg  = new String[Regulation.length-1];
                for(int i=0,j=1;j<Regulation.length;i++,j++){
                    reg[i] = Regulation[j];
                }
                huffOp.CreateHuffCode(huffOp.CreateDeHuffmanTree(values,reg));
                /*
                 * Use ReadInBytes to init HuffmanTree.
                 */
                try{
                    ReadInBytes = huffOp.readInFile(TARGET_PATH,false);
                }catch(IOException ignored){}
                System.out.println("Decoding test:");  //解压缩
                PutOutStrs = huffOp.DecodeHuff(huffOp.DecToBin(ReadInBytes));
                huffOp.writeLetterToFile(PutOutStrs);
                break;

            case "Code":
                try {
                    ReadInStrs = huffOp.readInFile(SOURCE_PATH); //把文件中所有的内容读入内存
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("str long:"+ReadInStrs.length());
                frequency = huffOp.CountLetterfrequency(ReadInStrs); //计算ABCDE每个字符出现的次数
                huffOp.CreateHuffCode(huffOp.CreateHuffmanTree(values,frequency));//生成哈夫曼树
                huffOp.ReplaceLetter(ReadInStrs); //生成编码
                huffOp.writeInfoToFile();   //将编码信息写入文件头部
                huffOp.writeCodeToFile();  //将压缩好的编码写入到文件里
                break;
        }

    }

}



class HuffmanTreeOp<ElemType>{
    Node head;
    Node[] nodes;  //哈夫曼树的结点数组
    int ByteCodes;  //二进制转化成的哈夫曼编码
    String ReadInStr; //从文件中读入的数据
    String ResultStr;
    String TobeWriteStr; //将要写入文件的编码
    String[] CodeCmp; //存储字符对应的赫夫曼编码值
    HashMap<Byte, Integer> SaveMap;
    HashMap<Byte, Character> CodeMap;

    HuffmanTreeOp(){
        CodeCmp = new String[5];
    }

    /*Init Huffman Tree*/
    int CreateHuffmanTree(String values,int[] frequency){
        int weight;
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
        for(int i=0;i<num;i++){
            System.out.println("\nNode "+values.charAt(i)+":");
            nodes[i].weight = frequency[i];
            nodes[i].data = values.charAt(i);
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

        return 0;
    }

    /*init Huffman Code*/
    String CreateHuffCode(int num){
        char[] cd;
        ResultStr = "";
        int start;
        Node node;
        Node child;
        byte[] BCodes;
        int IntValues;
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
            CodeCmp[i] = "";
            for(int j=start-1;j>=0;j--)
                CodeCmp[i] += cd[j];
//            byte BCode = (byte)(char)nodes[i].data;
            IntValues = Integer.parseInt(CodeCmp[i]);
//            BCodes = ((String)nodes[i].data).getBytes();
                SaveMap.put((byte)(char)nodes[i].data,IntValues);

//            while(CodeCmp[i].length()<3)
//                CodeCmp[i] = "0"+CodeCmp[i];
            /*end of saving*/
        }
        System.out.println("The total:"+ResultStr);

        return null;
    }

    /*Replace letters with Huffman Code*/
    String ReplaceLetter(String letters){
        ResultStr = "";

        for(int i=0;i<letters.length();i++){
            if(letters.charAt(i) == 'A'){
                ResultStr += CodeCmp[0];
                continue;
            }
            if(letters.charAt(i) == 'B'){
                ResultStr += CodeCmp[1];
                continue;
            }
            if(letters.charAt(i) == 'C'){
                ResultStr += CodeCmp[2];
                continue;
            }
            if(letters.charAt(i) == 'D'){
                ResultStr += CodeCmp[3];
                continue;
            }
            if(letters.charAt(i) == 'E'){
                ResultStr += CodeCmp[4];
                continue;
            }
        }
        return null;
    }

    int[] CountLetterfrequency(String ReadInStrs){
        int[] LetterCounter = {0,0,0,0,0};
        byte[] conByte = ReadInStrs.getBytes();
        SaveMap = new HashMap<Byte, Integer>();
        for(int i=0;i<ReadInStrs.length();i++){
            Integer Count = SaveMap.get(conByte[i]);
            if(Count == null)
                SaveMap.put(conByte[i],1);
            else
                SaveMap.put(conByte[i],++Count);
        }
        Byte BKey = 65;
        for(int i=0;i<LetterCounter.length;i++)
            LetterCounter[i] = SaveMap.get(BKey++);
        System.out.println("We have "+LetterCounter[0]+" A:");
        System.out.println("We have "+LetterCounter[1]+" B:");
        System.out.println("We have "+LetterCounter[2]+" C:");
        System.out.println("We have "+LetterCounter[3]+" D:");
        System.out.println("We have "+LetterCounter[4]+" E:");
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

    void writeInfoToFile() {
        String values = "ABCDE";
        String str = "";
        for (int i=0;i<CodeCmp.length;i++)
            str += values.charAt(i)+":"+CodeCmp[i]+"\r\n";
        //write code segments to file
        try {
            writeOutFile(TARGET_PATH,str,false,"StringType");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //end
    }


    /*将已经编码好的内容写入文件*/
    void writeCodeToFile(){
        for(int i=0;i<ResultStr.length();){
            TobeWriteStr = "";
            for(int j=0;j<8;j++,i++){
                if (i >= ResultStr.length())
                    TobeWriteStr = TobeWriteStr+'0';
                else
                    TobeWriteStr += ResultStr.charAt(i);
            }
            //When routine is over,write bytes to file
            ByteCodes = BinToDec(TobeWriteStr);
            /*output sentence*/
            try {
                writeOutFile(TARGET_PATH,ByteCodes,true,"IntType");
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*end of output*/
        }
    }

    void writeLetterToFile(String PutOutStrs){
//        PutOutStrs = PutOutStrs.substring(0,100);
        try {
            writeOutFile(DECODE_PATH,PutOutStrs,false,"StringType");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    String readInFile(String Address,boolean ReadCode) throws IOException {
        /*declaration area*/
        StringBuilder results;
        String tempStrs;
        File file;
        FileReader fileReader;
        BufferedReader bufferedreader;
        /*end of declaration*/

        results = new StringBuilder();
        file = new File(Address);
        fileReader = new FileReader(file);
        bufferedreader = new BufferedReader(fileReader);
        if(fileReader==null)
            return "EMPTY";

        /*read the head info or the codes*/
        if(ReadCode)
            for (int i=0;i<CodeCmp.length && (tempStrs=bufferedreader.readLine())!=null;i++)
                results.append(tempStrs);
        else
            for(String val:CodeCmp)
                bufferedreader.readLine();
        while((tempStrs = bufferedreader.readLine())!=null)
            results.append(tempStrs);
        /*end*/

        ReadInStr = String.valueOf(results);
        bufferedreader.close();
        return String.valueOf(results);
    }

    String readInFile(String Address) throws IOException { //reload
        StringBuilder results;
        String tempStrs;
        File file;
        FileReader fileReader;
        BufferedReader bufferedreader;

        results = new StringBuilder();
        file = new File(Address);
        fileReader = new FileReader(file);
        if(fileReader == null)
            return "EMPTY";
        bufferedreader = new BufferedReader(fileReader);
        while((tempStrs = bufferedreader.readLine())!=null){
            results.append(tempStrs);
        }

        ReadInStr = String.valueOf(results);
        bufferedreader.close();
        return String.valueOf(results);
    }

    String writeOutFile(String Address,Object Codes,boolean flag,String SaveMode) throws IOException{
        BufferedWriter bufferedwriter;
        FileWriter fileWriter;
        File file = new File(Address);
        fileWriter = new FileWriter(file,flag);
        bufferedwriter = new BufferedWriter(fileWriter);
        // write to file
        try {
            if(SaveMode.equals("IntType"))
                bufferedwriter.write((int)Codes);
            else if(SaveMode.equals("StringType"))
                bufferedwriter.write((String)Codes);
        }catch (IOException e){}
        /*		System.out.println("DEBUG:"+ByteCodes);   //print debug informations*/
        bufferedwriter.close();
        return "YES";
    }


}

interface CONSTANT{
    int INFINITY = 65535;
    String SOURCE_PATH = "PATH/TEST.txt";
    String TARGET_PATH ="PATH/compress.huffman";
    String DECODE_PATH = "PATH/decode.txt";
}
