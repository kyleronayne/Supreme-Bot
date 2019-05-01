import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;

public class Setup {


    private Profile profile;
    private SiteDriver siteDriver;


    public Setup(Profile profile, SiteDriver siteDriver) {
        this.profile = profile;
        this.siteDriver = siteDriver;
    }


    private String getAddressCookie() {
        String name = profile.getName().replace(" ", "+");
        String shippingAddress = profile.getShippingAddress().replace(" ", "+");
        String city = profile.getCity().replace(" ", "+");
        String state = profile.getState();
        String zipCode = profile.getZipCode();
        String country = profile.getCountry();
        String emailAddress = profile.getEmail().replace("@", "%40");
        String phoneNumber = profile.getPhoneNumber();

        String checkoutAddressCookie = name + "%7C" + shippingAddress + "%7C%7C" + city + "%7C" + state + "%7C" + zipCode + "%7C" + country +
                "%7C" + emailAddress + "%7C" + phoneNumber;


        return  checkoutAddressCookie;
    }


    public void injectCheckoutCookies() {
        Cookie addressCookie = new Cookie("address", getAddressCookie());
        siteDriver.injectCookie(addressCookie);
        Cookie rememberMeCookie = new Cookie("remember_me", "1");
        siteDriver.injectCookie(rememberMeCookie);
    }
}
