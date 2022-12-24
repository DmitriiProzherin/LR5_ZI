import java.io.*;
import java.util.ArrayList;

import static util.Utility.*;

public class FeistelDecryptor {
    private final int functionType;
    private final int keyGeneratorType;
    String[] key_arr;
    String decryptedTextFileName, keyFileName, encryptedFileName;

    FeistelDecryptor(int keyGeneratorType, int functionType){

        check(keyGeneratorType, functionType);

        this.functionType = functionType;
        this.keyGeneratorType = keyGeneratorType;

    }

    public String decrypt() throws IOException {

        String encryptedText = readFrom(encryptedFileName);
        String key = readFrom(keyFileName);

        if (keyGeneratorType == 1) key_arr = method1(key, 32);
        else key_arr = method2(key);

        String[] blocks = splitStringIntoBlocks(encryptedText, 64);
        ArrayList<String> decryptedBlocks = new ArrayList<>();
        StringBuilder resultText = new StringBuilder();

        for (String b : blocks) decryptedBlocks.add(decryptBlock(b));

        decryptedBlocks.forEach(b -> resultText.append(formatedBoolStringtoString(b)));

        String decryptedText = resultText.toString();

        writeTo(decryptedTextFileName, decryptedText);

        return decryptedText;
    }

    private String decryptBlock(String block){
        assert block.length() == 64;

        String left = block.substring(0, 32);
        String right = block.substring(32, 64);
        String tempR, tempL;

        int j = 15;
        for (int i = 1; i<=NUMBER_OF_ROUNDS; i++) {
            tempL = right;
            tempR = right;

            if (functionType == 1) tempR = feistel1(tempR, key_arr[j]);
            else tempR = feistel2(tempR, key_arr[j]);

            right = xor(tempR, left);
            left = tempL;
            j--;
        }

        return right + left;

    }

    public FeistelDecryptor key(String fileName) {
        keyFileName = fileName;
        return this;
    }

    public FeistelDecryptor fromFile(String fileName) {
        encryptedFileName = fileName;
        return this;
    }

    public FeistelDecryptor toFile(String fileName) {
        decryptedTextFileName = fileName;
        return this;
    }

}
