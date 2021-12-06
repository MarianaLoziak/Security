import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

public class MainBetterMT {
    private static int ID = 16121;

    public static void main(String[] args) throws IOException {
        Account account = createAccount();
        long [] numbers = new long[624];
        for (int i = 0; i < 624 ; i++) {
            numbers[i] = untemper((int)makeBet(1,1));
            //numbers[i] = makeBet(1,1);
            System.out.println(numbers[i]);
            System.out.println(untemper(numbers[i]));
            System.out.println();
        }

        MTStrong MT = new MTStrong(numbers);
        System.out.println(MT.next(32));
        makeBet(1,1);
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

    public static long makeBet(int moneyAmount, int number) throws IOException {
        String result = sendGET("http://95.217.177.249/casino/playBetterMt?id=" + ID + "&bet=" + moneyAmount + "&number=" + number);
        Gson gson = new Gson();
        Bet bet = gson.fromJson(result,Bet.class);
        System.out.println(result);

        return bet.realNumber;
    }

    public static long untemper(long y){
        y ^= (y >> 18);
        y ^= (y << 15) & 0xefc60000;
        y ^= ((y <<  7) & 0x9d2c5680) ^ ((y << 14) & 0x94284000) ^ ((y << 21) & 0x14200000) ^ ((y << 28) & 0x10000000);
        y ^= (y >> 11) ^ (y >> 22);
        return y;
    }
}
