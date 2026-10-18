
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class CurrencyService {

    public static double getExchangeRate(String from, String to)
            throws Exception {

        // Keep your existing API key unchanged
        String apiKey = "9d80241b2fb8da8b505759a1";

        String urlString = String.format(
            "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s",
            apiKey, from, to
        );

        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int statusCode = conn.getResponseCode();

            InputStream stream = (statusCode >= 200 && statusCode < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            if (stream == null) {
                throw new IOException("HTTP error: " + statusCode);
            }

            StringBuilder response = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject json = new JSONObject(response.toString());

            if (!"success".equals(json.optString("result"))) {
                throw new IOException(
                    "API error: " +
                    json.optString("error-type", "Unknown error")
                );
            }

            double rate = json.getDouble("conversion_rate");

            if (!Double.isFinite(rate) || rate <= 0) {
                throw new IOException("Invalid exchange rate");
            }

            return rate;

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
