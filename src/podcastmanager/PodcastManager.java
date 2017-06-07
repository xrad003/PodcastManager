/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podcastmanager;

import java.io.*;
import java.net.*;
import java.util.*;

import java.util.logging.*;

/**
 *
 * @author wij
 */
public class PodcastManager {

    String path;

    /**
     * @param args the command line arguments
     */
    public PodcastManager() {
        path=getCWD();
        
        
            ArrayList<Podcast>list=readListOfPodcasts();

            /*URL url = new URL("http://mosselen0130.libsyn.com/rss");
            String podpath = path + File.separator + "mosselen";
            Podcast podcast = new Podcast(url, 3, podpath);
            podcast.download();

            url = new URL("http://www.tech45.eu/wordpress/feeds/tech45_podcast.xml");
            podpath = path + File.separator + "tech45";
            podcast = new Podcast(url, 3, podpath);
            podcast.download();*/
            
            for(Podcast p:list){
                log(p.toString());
                p.download();
            }
            createM3U();

        
    }

    public ArrayList<Podcast> readListOfPodcasts() {
        ArrayList<Podcast> list = new ArrayList<>();
        String fn = path + File.separator + "rsslist.ini";
        File f = new File(fn);
        if (f.isFile()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String ln;
                try {
                    while ((ln = br.readLine()) != null) {
                        log(ln);
                        if ((!ln.isEmpty() )){
                            String[] lnsplit=ln.split(";");
                            if(lnsplit.length==3) {
                                Podcast podcast;
                                podcast = new Podcast(new URL(lnsplit[0]),Integer.parseInt(lnsplit[2]),lnsplit[1]);
                                log("podcast found in list: "+podcast);
                                list.add(podcast);
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PodcastManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PodcastManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    public void createM3U() {
        log("creating m3u-file");
        PrintWriter pw = null;
        try {
            File folder = new File(path);
            File[] files = folder.listFiles();
            File f = new File(path + File.separator + "playlist.m3u");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fos = new FileOutputStream(f);
            pw = new PrintWriter(fos);

            for (File ff : files) {
                if (ff.isDirectory()) {
                    for (File fff : ff.listFiles()) {
                        log(fff.getAbsolutePath());
                        pw.write(fff.getAbsolutePath() + "\n");
                    }
                }
            }
            pw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PodcastManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }

    }

    void log(String s) {
        System.out.println(s);
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
    
    public static void main(String[] args) {
        new PodcastManager();
    }

}
