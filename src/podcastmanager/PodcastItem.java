/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podcastmanager;

import java.net.*;

import java.text.*;
import java.util.*;

import java.util.logging.*;

/**
 *
 * @author wij
 */
public class PodcastItem {

    private String title;
    private String pubDate;
    private Date date;
    private URL url;
    private String fileName;

    public PodcastItem(String title, String pubDate, URL url) {
        this.title = title;
        this.pubDate = pubDate;
        this.url = url;

    }

    public PodcastItem(String strItem) {
        try {
            this.title = findTitle(strItem);
            this.pubDate = findPubDate(strItem);
            DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
            date = format.parse(this.pubDate);
            String urlstr = findURLstr(strItem);
            this.url = new URL(urlstr);

            this.fileName = getFileNameFromUrl(url);
        } catch (MalformedURLException ex) {
            this.url = null;
        } catch (ParseException ex) {
            Logger.getLogger(PodcastItem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * This function will take an URL as input and return the file name.
     * <p>
     * Examples :</p>
     * <ul>
     * <li>http://example.com/a/b/c/test.txt -> test.txt</li>
     * <li>http://example.com/ -> an empty string </li>
     * <li>http://example.com/test.txt?param=value -> test.txt</li>
     * <li>http://example.com/test.txt#anchor -> test.txt</li>
     * </ul>
     *
     * @param url The input URL
     * @return The URL file name
     */
    public static String getFileNameFromUrl(URL url) {

        String urlString = url.getFile();

        return urlString.substring(urlString.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];
    }

    private String findTitle(String strItem) {
        String start = "<title>";
        String stop = "</title>";

        int pos0 = strItem.toLowerCase().indexOf(start);
        int pos1 = strItem.toLowerCase().indexOf(stop);

        if (pos1 - pos0 > start.length()) {
            return strItem.substring(pos0 + start.length(), pos1);
        } else {
            return "-";
        }
    }

    private String findPubDate(String strItem) {
        String start = "<pubdate>";
        String stop = "</pubdate>";

        int pos0 = strItem.toLowerCase().indexOf(start);
        int pos1 = strItem.toLowerCase().indexOf(stop);

        //System.out.println(""+pos0+" "+pos1+" "+strItem.substring(pos0 + start.length(), pos1));
        if (pos1 - pos0 > start.length()) {
            return strItem.substring(pos0 + start.length(), pos1);
        } else {
            return "-";
        }
    }

    private String findURLstr(String strItem) {
        String start = "<enclosure";
        String stop = "/>";

        int pos0 = strItem.toLowerCase().indexOf(start);
        int pos1 = strItem.toLowerCase().indexOf(stop, pos0);

        if (pos1 - pos0 > start.length()) {
            String str = strItem.substring(pos0, pos1);
            start = "url=\"";
            stop = "\"";
            pos0 = str.toLowerCase().indexOf(start) + start.length();
            pos1 = str.toLowerCase().indexOf(stop, pos0);
            //System.out.println(""+pos0+" "+pos1+" "+str);
            if (pos1 > pos0 + start.length()) {
                str = str.substring(pos0, pos1 + stop.length() - 1);
                return str;
            } else {
                return "-";
            }
        }
        return "null";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "PodcastItem{" + "title=" + title + ", pubDate=" + pubDate + ", date=" + date + ", url=" + url + ", filename=" + fileName+ '}';
    }

}
