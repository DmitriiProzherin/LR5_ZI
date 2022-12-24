import util.Utility;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Utility.createRandom64BitKey("src/key.txt");

        FeistelEncryptor encryptor = new FeistelEncryptor(1, 2);
        encryptor.fromFile("src/input.txt")
                .toFile("src/encrypted.txt")
                .key("src/key.txt")
                .encrypt();

        FeistelDecryptor decryptor = new FeistelDecryptor(1, 2);
        decryptor.fromFile("src/encrypted.txt")
                .toFile("src/decrypted.txt")
                .key("src/key.txt")
                .decrypt();


    }
}