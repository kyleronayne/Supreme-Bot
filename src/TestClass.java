import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class TestClass {

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.sendAddToCartPost("https://www.supremenewyork.com/shop/172325/add.json", "s=67295&st=23740&qty=1");
    }

    private HttpsURLConnection sendAddToCartPost(String requestUrl, String formData) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        }

        // Catches incorrect url format
        catch (MalformedURLException invalidUrlException) {
            System.out.println("    Error: Invalid URL format");
        }

        HttpsURLConnection connection;
        while (true) {

            // Opens request connection
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.setConnectTimeout(2000);
                connection.setRequestMethod("POST");
            }
            catch (SocketTimeoutException timeoutException) {
                System.out.println("    Error: Connection Opening Timed Out");
                continue;
            }
            catch (IOException connectionOpenException) {
                System.out.println("    Error: Could Not Open Connection");
                continue;
            }

            // Adds request headers
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("accept-encoding", "gzip, deflate, br");
            connection.setRequestProperty("accept-language", "en-US,en;q=0.9");
            connection.setRequestProperty("content-length", "22");
            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("origin", "https://www.supremenewyork.com");
            connection.setRequestProperty("referer", "https://www.supremenewyork.com/mobile/");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 " +
                    "(KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1");
            connection.setRequestProperty("x-requested-with", "XMLHttpRequest");

            // Adds post request
            try {
                connection.setDoOutput(true);
                DataOutputStream postRequest = new DataOutputStream(connection.getOutputStream());
                postRequest.writeBytes(formData);
                postRequest.flush();
                postRequest.close();
            }
            catch (SocketTimeoutException timeoutException) {
                System.out.println("    Error: Form Data Post Timed Out");
                continue;
            }
            catch (IOException postFormDataException){
                System.out.println("    Error: Could Not Post Form Data");
                continue;
            }

            // Reads POST request response
            try {
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    StringBuffer response;
                    BufferedReader responseInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String responseLine;
                    response = new StringBuffer();
                    while ((responseLine = responseInput.readLine()) != null) {
                        response.append(responseLine);
                    }
                    responseInput.close();

                    if (response.toString().isEmpty()) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            catch (IOException readResponseException) {
                System.out.println("    Error: Could Not Read Response");
                continue;
            }

            return connection;
        }
    }
}
