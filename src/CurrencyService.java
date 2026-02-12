import java.net.*;
import java.io.*;
import org.json.JSONObject;

public class CurrencyService {
    
    public static double getExchangeRate(String from, String to) throws Exception {
        String apiKey = "9d80241b2fb8da8b505759a1";
        String urlString = String.format(
            "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s",
            apiKey, from, to
        );
        
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); // 5 seconds timeout
        conn.setReadTimeout(5000);
        
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        StringBuilder response = new StringBuilder();
        String line;
        
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        conn.disconnect();
        
        // Parse JSON response
        JSONObject json = new JSONObject(response.toString());
        
        // Check if the API call was successful
        String result = json.getString("result");
        if (!result.equals("success")) {
            throw new Exception("API request failed: " + result);
        }
        
        return json.getDouble("conversion_rate");
    }
}