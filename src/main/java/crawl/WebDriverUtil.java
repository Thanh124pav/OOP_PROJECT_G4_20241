package crawl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.*;

public class WebDriverUtil {
    public static WebDriver setUpDriver() throws InterruptedException, IOException {
        new ProcessBuilder("taskkill", "/F", "/IM", "msedge.exe").start().waitFor();
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--user-data-dir=C:\\Users\\USER\\AppData\\Local\\Microsoft\\Edge\\User Data");
        options.addArguments("--profile-directory=Default");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-debugging-port=9222");
        return new EdgeDriver(options);
    }
}
