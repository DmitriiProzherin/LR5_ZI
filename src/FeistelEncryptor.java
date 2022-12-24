import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static util.Utility.*;
public class FeistelEncryptor {

    private final int functionType;
    private final int keyGeneratorType;
    String[] key_arr;

    String inputTextFileName, keyFileName, encryptedFileName;
    FeistelEncryptor(int keyGeneratorType, int functionType){

        check(keyGeneratorType, functionType);

        this.functionType = functionType;
        this.keyGeneratorType = keyGeneratorType;
    }

    public String encrypt() throws IOException {

        String inputText = readFrom(inputTextFileName);
        String key = readFrom(keyFileName);

        if (keyGeneratorType == 1) key_arr = method1(key, 32);
        else key_arr = method2(key);

        String[] blocks = splitStringIntoBlocks(inputText, 4);
        ArrayList<String> binaryBlocks = new ArrayList<>();
        ArrayList<String> encryptedBlocks = new ArrayList<>();
        StringBuilder res = new StringBuilder();

        for (String b : blocks) { binaryBlocks.add(byteArrToString(strToByteArr(b))); }

        binaryBlocks.forEach(b -> encryptedBlocks.add(encryptBlock(b)));

        encryptedBlocks.forEach(res::append);

        String encryptedText = res.toString();

        writeTo(encryptedFileName, encryptedText);

        return encryptedText;
    }

    private String encryptBlock(String block){
        assert block.length() == 64;


        String left = block.substring(0, 32);
        String right = block.substring(32, 64);
        String tempR, tempL;

        for (int i = 1; i<=NUMBER_OF_ROUNDS; i++) {
            tempL = right;
            tempR = right;

            if (functionType == 1) tempR = feistel1(tempR, key_arr[i - 1]);
            else tempR = feistel2(tempR, key_arr[i - 1]);

            right = xor(tempR, left);
            left = tempL;


        }

        return right + left;

    }



    public FeistelEncryptor key(String fileName) {
        keyFileName = fileName;
        return this;
    }

    public FeistelEncryptor fromFile(String fileName) {
        inputTextFileName = fileName;
        return this;
    }

    public FeistelEncryptor toFile(String fileName) {
        encryptedFileName = fileName;
        return this;
    }

}
