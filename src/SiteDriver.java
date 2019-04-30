import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.UserAgent;
import org.openqa.selenium.TimeoutException;

import java.util.concurrent.TimeUnit;

public class SiteDriver {

    UserAgent mobileUserAgent;
    JBrowserDriver jBrowserDriver;


    public SiteDriver() {
        mobileUserAgent = new UserAgent(UserAgent.Family.WEBKIT, "", "iPhone", "CPU iPhone OS 12_1_4 like Mac OS X", "AppleWebKit/605.1.15 " +
                "(KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1", "Mozilla/5.0" + " (iPhone; CPU iPhone OS 12_1_4 like Mac" +
                " OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1");
        jBrowserDriver = new JBrowserDriver(Settings.builder().headless(false).userAgent(mobileUserAgent).cache(true).build());
        this.getPage("https://www.supremenewyork.com/mobile/");
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


}
