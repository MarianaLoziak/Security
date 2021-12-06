import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;

public class MainMT {

    public static int ID = 250006;
    public static void main(String[] args) throws IOException {
        Account account = createAccount();
        Instant instant = Instant.parse(account.deletionTime);
        //System.out.println(account.deletionTime);
        int number = (int)makeBet(1,1);
        //System.out.println("Number = " + number);
        int seed = (int)instant.getEpochSecond() - 3600;
        //System.out.println("epoch = "  + seed );
        int predictedNumber = 0;
        MT199937 MT;
        do{
            MT = new MT199937(seed);
            predictedNumber = MT.next(32);
            System.out.println(predictedNumber);
            seed++;
        } while (predictedNumber!=number);
        //System.out.println(MT.next(32));
        makeBet(999, MT.next(32));

    }

    private static String sendGET(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();

    }

    public static Account createAccount() throws IOException {
        String result = sendGET("http://95.217.177.249/casino/createacc?id=" + ID);
        Gson gson = new Gson();
        Account account = gson.fromJson(result, Account.class);
        return account;
    }

    public static long makeBet(int moneyAmount, long number) throws IOException {
        String result = sendGET("http://95.217.177.249/casino/playMt?id=" + ID + "&bet=" + moneyAmount + "&number=" + number);
        Gson gson = new Gson();
        Bet bet = gson.fromJson(result,Bet.class);
        System.out.println(result);

        return bet.realNumber;
    }
}
