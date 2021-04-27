package HuffmanTree;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

import static HuffmanTree.CONSTANT.INFINITY;
import static HuffmanTree.CONSTANT.SOURCE_PATH;

class Node<ElemType>{
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
        try {
            ReadInStrs = huffOp.readInFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] frequence = huffOp.CountLetterFrequence(ReadInStrs);
        String values = "ABCDE";
        /*创建赫夫曼树*/
        huffOp.CreateHuffCode(huffOp.CreateHuffmanTree(values,frequence));


        Node head = huffOp.nodes[huffOp.nodes.length-1];
        System.out.println(head.LChild.weight);
        System.out.println(head.RChild.weight);
    }

}



class HuffmanTreeOp<ElemType>{
    Node[] nodes;

    int CreateHuffmanTree(String values,int[] frequence){
        int weight;
        int min1,min2;
        int index1,index2;
        int num;
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input the number of leaves:");
        num = scanner.nextInt();
        nodes = new Node[2*num-1];
                                    //自动识别为Object类型
        for(int i=0;i<2*num-1;i++)
            nodes[i] = new Node<String>();

        System.out.println("Please input the weight of the nodes(end with Enter):");
        for(int i=0;i<num;i++){
            System.out.println("\nNode "+values.charAt(i)+":");
//            weight = scanner.nextInt();
//            nodes[i].weight = weight;
            nodes[i].weight = frequence[i];
//            System.out.println("Please input the weight of the nodes(end with Enter):");
            nodes[i].data = values.charAt(i);
        }

        for (Node node1:nodes) {
            System.out.println(node1.weight);
        }

        for(int i=0;i<num-1;i++){
            min1=min2=INFINITY;
            index1=index2=0;
            for(int j=0;j<num+i;j++){
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
                //still working
            }
            nodes[num+i].weight = nodes[index1].weight+nodes[index2].weight;
            nodes[num+i].LChild = nodes[index1];
            nodes[num+i].RChild = nodes[index2];
            nodes[index1].Parent = nodes[num+i];
            nodes[index2].Parent = nodes[num+i];
            System.out.println("x1.weight and x2.weight in round "+i+":"+nodes[index1].weight+","+nodes[index2].weight);
        }
        return num;
    }

    String CreateHuffCode(int num){
        char cd[] = new char[num];
        int start;
        Node node;
        Node child;
        for(int i=0;i<num;i++){
            child = nodes[i];
            node = nodes[i].Parent;
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
            for (int j=cd.length-1;j>=0;j--) {
                System.out.print(cd[j]);
            }
            System.out.println();
        }

        return null;
    }

    int[] CountLetterFrequence(String ReadInStrs){
        int[] LetterCounter = {0,0,0,0,0};
        for (int i=0;i<ReadInStrs.length();i++) {
            if(ReadInStrs.charAt(i) == 'A')
                LetterCounter[0]++;
            if(ReadInStrs.charAt(i) == 'B')
                LetterCounter[1]++;
            if(ReadInStrs.charAt(i) == 'C')
                LetterCounter[2]++;
            if(ReadInStrs.charAt(i) == 'D')
                LetterCounter[3]++;
            if(ReadInStrs.charAt(i) == 'E')
                LetterCounter[4]++;
        }
        System.out.println("We have "+LetterCounter[0]+" A:");
        System.out.println("We have "+LetterCounter[1]+" B:");
        System.out.println("We have "+LetterCounter[2]+" C:");
        System.out.println("We have "+LetterCounter[3]+" D:");
        System.out.println("We have "+LetterCounter[4]+" E:");
        return LetterCounter;
    }

    String DecodeHuff(){

        return null;
    }

    String readInFile() throws IOException {
        StringBuilder results = new StringBuilder();
        String tempStrs;
        File file = new File(SOURCE_PATH);
        FileReader fileReader = null;
        BufferedReader bufferedreader = null;
        StringBuffer buffer = new StringBuffer();
            fileReader = new FileReader(file);
        if(fileReader!=null)
            bufferedreader = new BufferedReader(fileReader);
        while((tempStrs = bufferedreader.readLine())!=null){
            results.append(tempStrs).append("\n");
        }
//        System.out.println(results);

        return String.valueOf(results);
    }


}

interface CONSTANT{
    final int INFINITY = 65535;
    final String SOURCE_PATH = "PATH/TEST.huffman";
}
