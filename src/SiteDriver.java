import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.UserAgent;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.TimeoutException;
import java.util.concurrent.TimeUnit;

public class SiteDriver {

    UserAgent mobileUserAgent;
    JBrowserDriver jBrowserDriver;


    public SiteDriver() {
        UserAgent chromeUserAgent = new UserAgent(UserAgent.Family.WEBKIT, "", "Windows NT 10.0", "Win64; x64", "AppleWebKit/537.36 (KHTML, " +
                "like Gecko) Chrome/73.0.3683.103 Safari/537.36", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like " +
                "Gecko) Chrome/73.0.3683.103 Safari/537.36");
        jBrowserDriver =
                new JBrowserDriver(Settings.builder().headless(false).userAgent(chromeUserAgent).javascript(true).cache(true).build());
        this.getPage("https://www.supremenewyork.com");
        jBrowserDriver.manage().timeouts().pageLoadTimeout(2, TimeUnit.SECONDS);
    }


    public boolean getPage(String url) {
        while (true) {
            try {
                jBrowserDriver.get(url);
                return true;
            }
            catch (TimeoutException timeoutException) {
                System.out.println("Timeout Exception Met While Getting Page: " + url);
                continue;
            }
        }
    }

    public void injectCookie(Cookie cookie) {
        jBrowserDriver.manage().addCookie(cookie);
    }

    public String getPageHtml() {
        return jBrowserDriver.getPageSource();
    }

    public void refreshPage() {
        getPage(jBrowserDriver.getCurrentUrl());
    }


}
