import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SalsaMain {
    
    public static void main(String[] args) throws IOException {
        List<String> lines = readFromFile("src/Text");
        Decryptor d = new Decryptor();

        d.decode(lines);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first line index:");
        int index1 = scanner.nextInt();
        System.out.println("Enter second line index:");
        int index2 = scanner.nextInt();
        System.out.println("Searching in line " + index1 + " and " + index2);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            System.out.println("Enter crib word or press ENTER to stop:");
            String word = br.readLine();
            if(word.isEmpty())
                break;
            String testWord = d.ASCIIToHex(word);
            //System.out.println(testWord);
            String xoredLine = d.XORHexString(lines.get(index1),lines.get(index2));
            //System.out.println(xoredLine);
            d.test(xoredLine, testWord);
        }

        System.out.println("Enter a line or press ENTER to stop:");
        //if(scanner.hasNext()){
            if (scanner.nextLine().isEmpty()){

                String line = scanner.nextLine();
                System.out.println("Enter line index: ");
                int ind = scanner.nextInt();
                String keyHex = d.XORHexString(d.ASCIIToHex(line), d.ASCIIToHex(lines.get(ind)));
                for (int i = 0; i < line.length(); i++) {
                    System.out.println(d.hexToAscii(d.XORHexString(keyHex.substring(0,lines.get(i).length()*2),d.ASCIIToHex(lines.get(i)))));
                }
            }
        //}


    }

    public static List<String> readFromFile(String fileName){
        List<String> output = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            Scanner sc = new Scanner(fis);
            while(sc.hasNextLine())
            {
                output.add(sc.nextLine().trim());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return output;
    }

}


