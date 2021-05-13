package HuffmanTree;

import java.io.*;
import java.util.HashMap;

import static HuffmanTree.CONSTANT.DECODE_PATH;
import static HuffmanTree.CONSTANT.TARGET_PATH;

public class IOUtil {
    String ReadInStr; //从文件中读入的数据
    String ResultStr;
    String TobeWriteStr; //将要写入文件的编码
    int ByteCodes;
    int ReadInCount;
    HashMap<Byte,String> SaveMap;

    IOUtil(){
    }
    IOUtil(HashMap<Byte,String> SaveMap){
        this.SaveMap = SaveMap;
    }
    void setCodedResultStr(String ResultStr){
        this.ResultStr = ResultStr;
    }

    void setHashMap(HashMap<Byte,String> SaveMap){
        this.SaveMap = SaveMap;
    }

    int getReadInInfo(){
        return ReadInCount;
    }

    String readInFile(String Address, boolean isReadInfo) throws IOException {
        /*declaration area*/
        int ChNum = 6;
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
        if (fileReader == null)
            return "EMPTY";

        /*read the head info or the codes*/
        ChNum = Integer.parseInt(bufferedreader.readLine());
        this.ReadInCount = ChNum;
        if (isReadInfo) { //decode true
//            for (int i = 0; i < ChNum && (tempStrs = bufferedreader.readLine()) != null; i++)
//                results.append(tempStrs);
            tempStrs = bufferedreader.readLine();
            results.append(tempStrs);
        } else {
//            for (int i = 0; i < ChNum; i++)
//                bufferedreader.readLine();
            bufferedreader.readLine();
        }
        while ((tempStrs = bufferedreader.readLine()) != null)
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
        if (fileReader == null)
            return "EMPTY";
        bufferedreader = new BufferedReader(fileReader);
        while ((tempStrs = bufferedreader.readLine()) != null) {
            results.append(tempStrs);
        }

        ReadInStr = String.valueOf(results);
        bufferedreader.close();
        return String.valueOf(results);
    }

    void writeInfoToFile() {
        String str = "";
        for (Byte BtKey : SaveMap.keySet()) {
            str += (char) (int) BtKey + ":" + SaveMap.get(BtKey) + " ";
        }

        str = String.valueOf(SaveMap.size()) + "\r\n" + str + "\r\n";

        //write code segments to file
        try {
            writeOutFile(TARGET_PATH, str, false, "StringType");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //end
    }


    /*将已经编码好的内容写入文件*/
    void writeCodeToFile() {
        for (int i = 0; i < ResultStr.length(); ) {
            TobeWriteStr = "";
            for (int j = 0; j < 8; j++, i++) {
                if (i >= ResultStr.length())
                    TobeWriteStr = TobeWriteStr + '0';
                else
                    TobeWriteStr += ResultStr.charAt(i);
            }
            //When routine is over,write bytes to file
            ByteCodes = BinToDec(TobeWriteStr);
            /*output sentence*/
            try {
                writeOutFile(TARGET_PATH, ByteCodes, true, "IntType");
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*end of output*/
        }
    }

    /*将已经解码好的字符内容写入文件*/
    void writeLetterToFile(String PutOutStrs) {
//        PutOutStrs = PutOutStrs.substring(0,100);
        try {
            writeOutFile(DECODE_PATH, PutOutStrs, false, "StringType");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String writeOutFile(String Address, Object Codes, boolean flag, String SaveMode) throws IOException {
        BufferedWriter bufferedwriter;
        FileWriter fileWriter;
        File file = new File(Address);
        fileWriter = new FileWriter(file, flag);
        bufferedwriter = new BufferedWriter(fileWriter);
        // write to file
        try {
            if (SaveMode.equals("IntType"))
                bufferedwriter.write((int) Codes);
            else if (SaveMode.equals("StringType"))
                bufferedwriter.write((String) Codes);
        } catch (IOException e) {
        }
        /*		System.out.println("DEBUG:"+ByteCodes);   //print debug informations*/
        bufferedwriter.close();
        return "YES";
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
}