package crawl;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Page {
    protected static String tweetCard = "a.css-146c3p1.r-bcqeeo.r-1ttztb7.r-qvutc0.r-37j5jr.r-a023e6.r-rjixqe.r-16dba41.r-xoduu5.r-1q142lx.r-1w6e6rj.r-9aw3ui.r-3s2u2q.r-1loqt21";
    protected static String userCard = "a:not([tabindex='-1']).css-175oi2r.r-1wbh5a2.r-dnmrzs.r-1ny4l3l.r-1loqt21";

    protected String link;
    public Page(){};
    public Page(String link){
        this.link = link;
    }

    static void scrollUp(JavascriptExecutor js) throws InterruptedException {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        System.out.println("Go to the bottom of page!");
        Thread.sleep(5000);
    }

    protected Set<String> getElementsByScroll(WebDriver driver, int limit, String query) throws InterruptedException {
        System.out.println("Crawl elements!");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Set<String> pageElements = new HashSet<>();
        System.out.println(query);
        Set<WebElement> elements = new HashSet<>();
        while(elements.size() < limit) {
            int oldSize = elements.size();
            Set<WebElement> newElements = new HashSet<>(driver.findElements(By.cssSelector(query)));
            elements.addAll(newElements);
            System.out.println(newElements.size());
            if(elements.size() == oldSize){
                System.out.println("get all elements, break the loop!");
                break;
            }
            pageElements.addAll(extractInfoBySCroll(newElements));
            scrollUp(js);
        }
        System.out.println("finish crawling!");
        return pageElements;
    }

    protected Set<String> extractInfoBySCroll(Set<WebElement> elements){
        //Set<Page> basicInfos = new HashSet<>();
        Set<String> links = new HashSet<>();
        for (WebElement element: elements) {
            String link = element.getAttribute("href");
            Page page = new Page(link);
            if(!links.contains(link)){
                links.add(link);
                //basicInfos.add(page);
                System.out.println("\t" + link +  " - " + "added!");
            }
        }
        return links;
    };


}
