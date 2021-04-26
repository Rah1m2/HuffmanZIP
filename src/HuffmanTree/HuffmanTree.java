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
        try {
            System.out.println(huffOp.readInFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*创建赫夫曼树*/
        huffOp.CreateHuffmanTree();


        Node head = huffOp.nodes[huffOp.nodes.length-1];
        System.out.println(head.LChild.weight);
        System.out.println(head.RChild.weight);
    }

}



class HuffmanTreeOp<ElemType>{
    Node[] nodes;

    Node[] CreateHuffmanTree(){
        int weight;
        int min1,min2;
        int index1,index2;
        int num;
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input the number of leaves:");
        num = scanner.nextInt();
        nodes = new Node[2*num-1];

//        Node[] nodes = new Node[2*num-1]; //自动识别为Object类型
        for(int i=0;i<2*num-1;i++)
            nodes[i] = new Node<String>();

        System.out.println("Please input the weight of the nodes(end with Enter):");
        for(int i=0;i<num;i++){
            System.out.println("\nNode "+i+":");
            weight = scanner.nextInt();
            nodes[i].weight = weight;
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
        return nodes;
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
