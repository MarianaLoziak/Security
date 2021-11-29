import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {

    public static int ID = 3019;

    public static void main(String[] args) throws IOException {

        //createAccount();
        long[] numbers = new long[3];
        numbers[0] =makeBet(1,1);
        numbers[1] = makeBet(1,1);
        numbers[2] = makeBet(1,1);
        System.out.println(numbers[0]);
        System.out.println(numbers[1]);
        System.out.println(numbers[2]);

        Lcg lcg = new Lcg();
        lcg.findValues(numbers);
        System.out.println("a = " + lcg.a);
        System.out.println("c = " + lcg.c);
        long n = lcg.next(numbers[2],lcg.a,lcg.c);
       System.out.println("Predicted next number = " + n);
        makeBet(10,n);

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

    public static String createAccount() throws IOException {
        String result = sendGET("http://95.217.177.249/casino/createacc?id=" + ID);
        return result;
    }

    public static long makeBet(int moneyAmount, long number) throws IOException {
        String result = sendGET("http://95.217.177.249/casino/playLcg?id=" + ID + "&bet=" + moneyAmount + "&number=" + number);
        Gson gson = new Gson();
        JsonObject  jsonObject = new Gson().fromJson(result, JsonObject.class);
        Long realNumber = jsonObject.get("realNumber").getAsLong();
        System.out.println(result);

        return realNumber;
    }
}
