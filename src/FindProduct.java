import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;

public class FindProduct {


    private Profile profile;
    private HashMap<String, String> productIds;


    public FindProduct(Profile profile) {
        this.profile = profile;
        productIds = new HashMap<>();
    }


    private JSONObject getJson(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException invalidUrlException) {
            System.out.println("     Error: Invalid URL format");
        }


        HttpURLConnection connection;
        JSONObject stockJson;
        while (true) {

            // Opens GET request connection
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(2000);
                connection.setRequestMethod("GET");
            } catch (SocketTimeoutException timeoutException) {
                System.out.println("    Error: Connection Opening Timed Out");
                continue;
            } catch (IOException connectionOpenException) {
                System.out.println("    Error: Could Not Open Connection");
                continue;
            }

            // Adds GET request headers
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("accept-language", "en-US,en;q=0.9");
            connection.setRequestProperty("referer", "https://www.supremenewyork.com/mobile/");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 " +
                    "(KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1");
            connection.setRequestProperty("x-requested-with", "XMLHttpRequest");

            // Reads GET request response
            StringBuilder response = new StringBuilder();
            try {
                if (connection.getResponseCode() == 200) {
                    BufferedReader responseInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String responseLine;
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
            } catch (IOException readResponseException) {
                System.out.println("    Error: Could Not Read Response");
                continue;
            }

            stockJson = new JSONObject(response.toString());
            return stockJson;
        }
    }


    private boolean getProductId(JSONObject stockJson) {
        JSONArray categoryProducts;
        try {
            categoryProducts = stockJson.getJSONObject("products_and_categories").getJSONArray(profile.getCategory());
        } catch (JSONException categoryNotFoundException) {
            System.out.println("Category Not Found");
            return false;
        }

        if (categoryProducts != null && !(categoryProducts.isEmpty())) {
            for (int productIndex = 0; productIndex < categoryProducts.length(); productIndex++) {
                String productName = categoryProducts.getJSONObject(productIndex).get("name").toString();
                if (productName.toLowerCase().contains(profile.getKeyword())) {
                    String productId = categoryProducts.getJSONObject(productIndex).get("id").toString();
                    productIds.put("productId", productId);
                    return true;
                }
            }
        }

        System.out.println("Could Not Find Product");
        return false;
    }


    private boolean getStyleSizeIds(JSONObject productJson) {
        JSONArray productStyles;
        try {
            productStyles = productJson.getJSONArray("styles");
        } catch (JSONException stylesNotFoundException) {
            System.out.println("Styles Not Loaded");
            return false;
        }

        if (productStyles != null && !(productStyles.isEmpty())) {
            for (int styleIndex = 0; styleIndex < productStyles.length(); styleIndex++) {
                String productStyle = productStyles.getJSONObject(styleIndex).get("name").toString();
                if (productStyle.toLowerCase().contains(profile.getStyle())) {
                    String styleId = productStyles.getJSONObject(styleIndex).get("id").toString();
                    JSONArray productSizes;
                    try {
                        productSizes = productStyles.getJSONObject(styleIndex).getJSONArray("sizes");
                    } catch (JSONException sizesNotFoundException) {
                        System.out.println("Sizes Not Loaded");
                        return false;
                    }

                    if (productSizes != null && !(productSizes.isEmpty())) {
                        for (int sizeIndex = 0; sizeIndex < productSizes.length(); sizeIndex++) {
                            String productSize = productSizes.getJSONObject(sizeIndex).get("name").toString();
                            if (productSize.toLowerCase().contains(profile.getSize())) {
                                String sizeId = productSizes.getJSONObject(sizeIndex).get("id").toString();
                                productIds.put("styleId", styleId);
                                productIds.put("sizeId", sizeId);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Could Not Find Style and/or Size IDs");
        return false;
    }


    public HashMap<String, String> find() {
        while (true) {
            JSONObject stockJson = getJson("https://www.supremenewyork.com/mobile_stock.json");
            if (getProductId(stockJson)) {
                String productJsonUrl = String.format("https://www.supremenewyork.com/shop/%s.json", productIds.get("productId"));
                JSONObject productJson = getJson(productJsonUrl);
                if (getStyleSizeIds(productJson)) {
                    return productIds;
                }
            }

            try {
                Thread.sleep(profile.getSearchDelay());
            } catch (InterruptedException threadInterruptionException) {
                continue;
            }
        }
    }
}
