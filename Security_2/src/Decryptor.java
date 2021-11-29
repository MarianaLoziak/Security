import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Decryptor {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void test(String xoredLine, String testWord){
        for (int i = 0; i <= xoredLine.length() - testWord.length(); i++) {
            String temp = xoredLine.substring(i, i + testWord.length());
            String result = hexToAscii(XORHexString(temp, testWord));
            if(readable(result)){
                System.out.println(ANSI_GREEN + result+ ANSI_RESET);
            } else{
                System.out.println(result);
            }
        }
    }

    public static String ASCIIToHex(String ascii){

        char[] ch = ascii.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : ch) {
            int i = (int) c;
            builder.append(Integer.toHexString(i).toUpperCase());
        }
        String hex = builder.toString();

        return hex;
    }

    public static String hexToAscii(String hexText){
        StringBuilder newStr = new StringBuilder("");
        if(hexText.length()%2!=0){
            hexText = "0" + hexText;
        }

        for (int i = 0; i < hexText.length(); i+=2) {
            String temp = hexText.substring(i, i+2);
            newStr.append((char)Integer.parseInt(temp,16));
        }

        return  newStr.toString();
    }

    public static String XORHexString(String hex1, String hex2) {
        String output = new String();
        String longerHex = (hex1.length()> hex2.length())? hex1:hex2;
        String shorterHex = (hex1.length()> hex2.length())? hex2:hex1;
        for (int i = 0; i < (longerHex.length())  ; i+=2) {
           int n1 = Integer.parseInt(longerHex.substring(i, i+2),16);
           int n2 = i<shorterHex.length()?Integer.parseInt(shorterHex.substring(i, i+2), 16):0;
           int out = n1 ^ n2;
           output+=String.format("%02x", out);
        }
        return output;
    }

    public static boolean readableLetter(String input){
        return  Pattern.matches("^[a-zA-Z ,.:]*$",input);
    }//
    public static boolean readable(String input){
        return  Pattern.matches("^[a-zA-Z,.!? :;‘]*$",input);
    }

    public static void decode(List<String> cipherHex){
        List<String> cipher = new ArrayList<String>();
        for (String str: cipherHex) {
            cipher.add (hexToASCII( str));
        }
        int max = maxLength(cipher);
        String[] transponated = new String[max];
        for (int i = 0; i < max; i++) {
            transponated[i]="";
        }
        for (String str : cipher) {
            for (int i = 0; i < str.length(); i++) {
                transponated[i] += str.charAt(i);
            }
        }

        int[] key = new int[max];

        for (int i = 0; i < max; i++) {
            String temp = transponated[i];
            for (int j = 0; j < 256; j++) {
                int count = 0;
                //String maybe = new String();
                for (char c: temp.toCharArray()) {
                    char t = (char)(c ^ j);
                    //maybe += t;
                    if(readableLetter(Character.toString(t))){
                        count++;
                    }
                }
                if(count == temp.length()) {
                   if(key[i]==0)
                        key[i] = j;
                    else{
                        key[i] = 256;
                        break;
                    }

                }
            }
        }

        System.out.println(key);

        for (int k = 0; k < cipher.size(); k++) {
            String res = "";
            char [] array = cipher.get(k).toCharArray();
            for (int i = 0; i < cipher.get(k).length(); i++) {
                if(key[i]!= 256&&key[i]!=0){
                    res+= (char)(array[i]^key[i]);
                }
                else{
                    res+="_";
                }

            }
            System.out.println("[LINE " + k + "]: " + res);
        }
    }

    public static int maxLength(List<String> cipher){
        int maxLength = 0;
        for (String str : cipher) {
            if (str.length() > maxLength)
                maxLength = str.length();
        }
        return maxLength;
    }

    public static String hexToASCII (String hex){
        String output = new String();
        for (int i = 0; i < hex.length(); i+=2) {
            String temp = hex.substring(i, i+2);
            output += (char)Integer.parseInt(temp,16);
        }

        return output;
    }

}
