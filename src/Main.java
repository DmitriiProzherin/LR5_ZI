import util.Utility;

import java.io.IOException;

import static util.Utility.formatedBoolStringtoString;
import static util.Utility.readFrom;

public class Main {

    public static void main(String[] args) throws IOException {

        String secret_key = Utility.createRandom64BitKey("src/key.txt");
        System.out.println("Случайно сгенерированный секретный ключ: \n" + secret_key + "\n");

        String input_text = readFrom("src/input.txt");
        System.out.println("Исходный текст: \n" + input_text + "\n");

        FeistelEncryptor encryptor = new FeistelEncryptor(1, 2);
        String encrypted_text = encryptor.fromFile("src/input.txt")
                .toFile("src/encrypted.txt")
                .key("src/key.txt")
                .encrypt();
        System.out.println("Зашифрованный с помощью DES текст: \n" + formatedBoolStringtoString(encrypted_text) + "\n");

        FeistelDecryptor decryptor = new FeistelDecryptor(1, 2);
        String decrypted_text = decryptor.fromFile("src/encrypted.txt")
                .toFile("src/decrypted.txt")
                .key("src/key.txt")
                .decrypt();
        System.out.println("Расшифрованный текст: \n" + decrypted_text);

    }
}