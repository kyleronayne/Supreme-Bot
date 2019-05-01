import java.util.HashMap;
import java.util.Scanner;

public class SupremeBot {

    public static void main(String[] args) {
        SiteDriver siteDriver = new SiteDriver();
        Profile profile = new Profile();
        Setup setup = new Setup(profile, siteDriver);
        setup.injectCheckoutCookies();

        FindProduct findProduct = new FindProduct(profile);

        System.out.println("Start");
        HashMap<String, String> productIds = findProduct.find();

        AddToCart addToCart = new AddToCart(productIds, siteDriver);
        addToCart.add();
        addToCart.injectCartCookies();
        System.out.println("End");

    }
}
