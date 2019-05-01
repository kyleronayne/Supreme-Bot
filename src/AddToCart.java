import org.openqa.selenium.Cookie;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

public class AddToCart {


    private HashMap<String, String> productIds;
    private HttpsURLConnection connection;
    private String[] connectionCookies;
    private HashMap<String, String> cartCookies;
    private SiteDriver siteDriver;


    public AddToCart(HashMap<String, String> productIds, SiteDriver siteDriver) {
        this.productIds = productIds;
        cartCookies = new HashMap<>();
        this.siteDriver = siteDriver;
    }


    private String getRequestUrl() {
        String requestUrl = String.format("https://www.supremenewyork.com/shop/%s/add.json", productIds.get("productId"));
        return requestUrl;
    }


    private String getFormData() {
        String formData = String.format("s=%s&st=%s&qty=1", productIds.get("sizeId"), productIds.get("styleId"));
        return formData;
    }


    private boolean sendPostRequest(String requestUrl, String formData) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        }

        // Catches incorrect url format
        catch (MalformedURLException invalidUrlException) {
            System.out.println("    Error: Invalid URL format");
        }


        while (true) {

            // Opens request connection
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.setConnectTimeout(2000);
                connection.setRequestMethod("POST");
            }
            catch (SocketTimeoutException timeoutException) {
                System.out.println("    Error: Connection Opening Timed Out");
                return false;
            }
            catch (IOException connectionOpenException) {
                System.out.println("    Error: Could Not Open Connection");
                return false;
            }

            // Adds POST request headers
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

            // Adds POST request to connection
            try {
                connection.setDoOutput(true);
                DataOutputStream postRequest = new DataOutputStream(connection.getOutputStream());
                postRequest.writeBytes(formData);
                postRequest.flush();
                postRequest.close();
            }
            catch (SocketTimeoutException timeoutException) {
                System.out.println("    Error: Form Data Post Timed Out");
                return false;
            }
            catch (IOException postFormDataException){
                System.out.println("    Error: Could Not Post Form Data");
                return false;
            }

            // Reads POST request response
            try {
                if (connection.getResponseCode() == 200) {
                    StringBuffer response;
                    BufferedReader responseInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String responseLine;
                    response = new StringBuffer();
                    while ((responseLine = responseInput.readLine()) != null) {
                        response.append(responseLine);
                    }
                    responseInput.close();

                    if (response.toString().equals("[]")) {
                        System.out.println("Product Out of Stock");
                        return false;
                    }

                    connectionCookies = connection.getHeaderFields().toString().replace(";", ",").split(",");

                }
                else {
                    return false;
                }

                return true;
            }
            catch (IOException readResponseException) {
                System.out.println("    Error: Could Not Read Response");
                return false;
            }
        }
    }


    private boolean getSessionCookie() {
        for (String cookie : connectionCookies) {
            if (cookie.contains("_supreme_sess")) {
                String[] sessionCookie = cookie.split("=");
                if (!(sessionCookie[2].isEmpty())) {
                    cartCookies.put("sessionCookie", sessionCookie[2]);
                    return true;
                }
            }
        }

        return false;
    }


    private boolean getPureCartCookie() {
        for (String cookie : connectionCookies) {
            if (cookie.contains("pure_cart")) {
                String[] pureCartCookie = cookie.split("=");
                if (!(pureCartCookie[1].isEmpty())) {
                    cartCookies.put("pureCartCookie", pureCartCookie[1]);
                    return true;
                }
            }
        }

        return false;
    }


    private boolean getCartCookie() {
        for (String cookie : connectionCookies) {
            if (cookie.contains("cart") && !(cookie.contains("pure_cart"))) {
                String[] cartCookie = cookie.split("=");
                if (!(cartCookie[1].isEmpty())) {
                    cartCookies.put("cartCookie", cartCookie[1]);
                    return true;
                }
            }
        }

        return false;
    }


    public void add() {
        String requestUrl = getRequestUrl();
        String formData = getFormData();

        while (true) {
            if (sendPostRequest(requestUrl, formData)) {
                if (getSessionCookie()) {
                    if (getPureCartCookie()) {
                        if (getCartCookie()) {
                            break;
                        }
                    }
                }
            }
        }
    }


    public void injectCartCookies() {
        Cookie cartCookie = new Cookie("cart", cartCookies.get("cartCookie"));
        siteDriver.injectCookie(cartCookie);
        Cookie pureCartCookie = new Cookie("pure_cart", cartCookies.get("pureCartCookie"));
        siteDriver.injectCookie(pureCartCookie);
        Cookie sessionCookie = new Cookie("_supreme_sess", cartCookies.get("sessionCookie"));
        siteDriver.injectCookie(sessionCookie);
        Cookie requestCookie = new Cookie("request_method", "POST");
        siteDriver.injectCookie(requestCookie);
        siteDriver.getPage("https://www.supremenewyork.com/checkout");
    }
}
