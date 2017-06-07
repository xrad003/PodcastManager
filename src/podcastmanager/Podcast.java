/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podcastmanager;

import java.io.*;
import java.net.URL;
import java.nio.channels.*;

import java.util.*;
import java.util.logging.*;

/**
 *
 * @author wij
 */
public class Podcast {

    URL url;

    int numberToKeep;
    Date fromDate;
    String path;

    public Podcast(URL url, int numberToKeep, String path) {
        this.url = url;
        this.numberToKeep = numberToKeep;
        this.path = getCWD()+File.separator+path;
        File folder=new File(path);
        if(folder.isDirectory())
            log("path exists: "+path);
        else{
            log("trying to create path: "+path);
            folder.mkdir();
        }
            

    }

    private String getCWD(){
        String current="";
        try {
            current= new java.io.File( "." ).getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(Podcast.class.getName()).log(Level.SEVERE, null, ex);
        }
        return current;
    }
    
    public TreeMap<Date, PodcastItem> findItems() {
        log("findItems");
        TreeMap<Date, PodcastItem> map = new TreeMap<>();
        String itemstart = "<item>";
        String itemstop = "</item>";
        try {
            String strItem = "";
            Scanner s = new Scanner(url.openStream());
            while (s.hasNextLine()) {

                String l = s.nextLine();

                if (strItem.length() > 0) {
                    if (l.toLowerCase().contains(itemstop)) {
                        int pos = l.toLowerCase().indexOf(itemstop);
                        strItem += l.substring(0, pos + 7);
                        PodcastItem item = new PodcastItem(strItem);
                        //log("item found: "+item.toString());
                        map.put(item.getDate(), item);
                    } else {
                        strItem += l;
                    }
                }
                if (l.contains(itemstart)) {
                    int pos = l.toLowerCase().indexOf(itemstart);
                    strItem = l.substring(pos);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Podcast.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    void log(String s) {
        System.out.println(s);
    }

    int min(int a, int b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }

    void download() {
        log("download " + toString());
        TreeMap<Date, PodcastItem> map = findItems();
        File folder = new File(path);
        log("folder: " + folder.getName());
        log("number of items to keep: " + numberToKeep);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < min(numberToKeep, map.keySet().size()); i++) {
            PodcastItem item = map.get(map.lastKey());
            log(item.toString());
            boolean download = true;
            for (File f : listOfFiles) {
                if (f.getName().equals(item.getFileName())) {
                    download = false;
                }
            }
            if (download) {
                log("going to download " + item.getFileName());
                download(item);
            } else {
                log(item.getFileName() + " is already here.");
            }
            map.remove(map.lastKey());
        }

        for (Date date : map.keySet()) {
            PodcastItem item = map.get(date);
            for (File f : listOfFiles) {
                if (f.getName().equals(item.getFileName())) {
                    log("deleting " + f.getName());
                    f.delete();
                }
            }
        }

    }

    void download(PodcastItem item) {
        System.out.println("trying to download " + item.getFileName());
        try {
            ReadableByteChannel rbc = Channels.newChannel(item.getUrl().openStream());
            FileOutputStream fos = new FileOutputStream(path + File.separator + item.getFileName());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException ex) {
            Logger.getLogger(Podcast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "Podcast{" + "url=" + url + ", numberToKeep=" + numberToKeep + ", fromDate=" + fromDate + ", path=" + path + '}';
    }

}
