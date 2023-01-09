package kim.figure.site.management.common;

import org.jsoup.Jsoup;

/**
 * author         : walker
 * date           : 2023. 01. 05.
 * description    :
 */
public class JsoupUtils {
    static final int defaultSubstringLength = 180;

    public static String extractDescriptionFromHtml(String html) {
        return extractDescriptionFromHtml(html, defaultSubstringLength);
    }
    public static String extractDescriptionFromHtml(String html, int subStringLength){
        String pureString = Jsoup.parse(html).text();
        if(pureString.length()<180){
            return pureString;
        }else{
            return pureString.substring(0, subStringLength);
        }
    }

}
