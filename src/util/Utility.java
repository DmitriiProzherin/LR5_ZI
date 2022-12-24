package util;

import java.io.*;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Utility {
    public static int NUMBER_OF_ROUNDS = 16;

    public static String createRandom64BitKey(String fileName){
        boolean[] b = new boolean[64];
        Random r = new Random();

        for (int i = 0; i < 64; i++) {
            b[i] = r.nextBoolean();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(boolArrToString(b));
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return boolArrToString(b);
    }
    public static String boolArrToString(boolean[] arr){
        StringBuilder r = new StringBuilder();
        for (boolean b : arr) {
            if (b) r.append("1");
            else r.append("0");
        }
        return r.toString();
    }
    public static String[] splitStringIntoBlocks(String s, int block_length){
        int block_numbers = s.length() % block_length == 0 ? s.length() / block_length : s.length() / block_length + 1;

        int delta = block_length * block_numbers - s.length();
        char[] chars = new char[delta];
        Arrays.fill(chars, '\u0000');
        String prefix = new String(chars);
        s = prefix + s;


        return s.split("(?<=\\G.{"+block_length+"})");
    }

    public static byte[] strToByteArr(String str) {
        StringBuilder resStrB = new StringBuilder();
        byte[] result = new byte[str.length() * 16];

        for (int i = 0; i < str.length(); i++) {
            String s = Integer.toBinaryString(str.charAt(i));
            resStrB.append("0".repeat(Math.max(0, 16 - s.length())));
            resStrB.append(s);
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) Character.getNumericValue(resStrB.charAt(i));
        }

        return result;
    }

    public static String byteArrToString(byte[] arr){
        StringBuilder r = new StringBuilder();
        for (byte b : arr) {
            if (b == 1) r.append("1");
            else r.append("0");
        }
        return r.toString();
    }

    public static String formatedBoolStringtoString(String s){
        assert s.length() % 16 == 0;

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < s.length(); i+=16) {
            res.append(string16ToSymbol(s.substring(i, i+16)));
        }
        return res.toString();
    }

    public static String string16ToSymbol(String str){
        assert str.length() == 16;

        double res = 0;

        for (int i = str.length() - 1; i >=0 ; i--) {
            if (str.charAt(i) == '1') {
                res += Math.pow(2, str.length() - i - 1);
            }
        }
        if ((int) res == 0) return "";
        return ((char) res) + "";
    }

    public static String xor(String str1, String str2) {
        assert str1.length() == str2.length();

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < str1.length(); i++) {

            int temp = Character.getNumericValue(str1.charAt(i)) + Character.getNumericValue(str2.charAt(i));

            switch (temp) {
                case 0, 2 -> res.append("0");
                case 1 -> res.append("1");
            }

        }

        return res.toString();
    }

    public static String scramble16To32(String str){
        return str + str;
    }

    public static String readFrom(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String text = reader.lines().collect(Collectors.joining("\n"));
        reader.close();
        return text;
    }

    public static void writeTo(String fileName, String text) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(text);
        writer.close();
    }

    public static String feistel1(String right, String key) {
        return key;
    }

    public static String feistel2(String right, String key) { return xor(right, key); }

    public static String[] method1(String key, int length) {
        String[] res = new String[NUMBER_OF_ROUNDS];

        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            if (i + length <= 64) res[i] = key.substring(i, i + length);
            else res[i] = key.substring(i, 64) + key.substring(0, (i + length - 64) % 64);
        }

        return res;
    }

    public static String[] method2(String key) {
        String[] scramblers = method1(key, 8);

        for (int i = 0; i < scramblers.length; i++) {
            scramblers[i] = scramblers[i] + "00000011";
        }


        String[] res = new String[NUMBER_OF_ROUNDS];

        for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {
            res[i] = scramble16To32(scramblers[i]);
        }

        return res;
    }

    public static void check(int a, int b){
        if (a < 1 || a > 2) {
            System.out.println("ОШИБКА. НЕВЕРНЫЙ ВАРИАНТ ВЫБОРА ГЕНЕРАЦИИ КЛЮЧА. ВЫБЕРИТЕ 1 ИЛИ 2.");
            System.exit(13);
        }
        if (b < 1 || b > 2) {
            System.out.println("ОШИБКА. НЕВЕРНЫЙ ВАРИАНТ ВЫБОРА ГЕНЕРАЦИИ ФУНКЦИИ. ВЫБЕРИТЕ 1 ИЛИ 2.");
            System.exit(13);
        }
    }
}
