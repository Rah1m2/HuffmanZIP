package HuffmanTree;

import java.util.Scanner;

import static HuffmanTree.CONSTANT.INFINITY;

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
        huffOp.CreateHuffmanTree();
    }


}



class HuffmanTreeOp<ElemType>{


    Node CreateHuffmanTree() {
        int num;
        int weight;
        int min1,min2;
        int index1,index2;
        Scanner scanner = new Scanner(System.in);
        System.out.println("please input the number of leaves:");
        num = scanner.nextInt();
        Node[] nodes = new Node[2*num-1]; //自动识别为Object类型
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
                if(nodes[j].weight<min1 && nodes[i].Parent==null){
                    min2 = min1;
                    index2 = index1;
                    min1 = nodes[i].weight;
                    index1 = j;
                }
                if(nodes[j].weight<min2 && nodes[i].Parent==null){
                    min2 = nodes[i].weight;
                    index2 = j;
                }
                nodes[num+i].weight = nodes[index1].weight+nodes[index2].weight;
                //still working
            }
        }
        return null;
    }

}

interface CONSTANT{
    final int INFINITY = 65535;
}
