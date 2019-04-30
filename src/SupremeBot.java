import java.util.HashMap;

public class SupremeBot {

    public static void main(String[] args) {
        SiteDriver siteDriver = new SiteDriver();
        Profile profile = new Profile();
        FindProduct findProduct = new FindProduct(profile);

        HashMap<String, String> styleSizeIds = findProduct.find();
    }
}
