package com.itaewonproject.linkshare;
import android.os.AsyncTask;
import android.util.Log;

import com.itaewonproject.model.sender.Link;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LinkManager {


    public Link LinkApi(String url) {
        System.out.println("url req");

        Task task = new Task();
        task.execute(url);

        try{
            return task.get();
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        assert false;
        return null;
    }

    class Task extends AsyncTask<String, Integer, Link> {
        @Override
        protected Link doInBackground(String... urls) {
            Link link = new Link();
            assert urls.length == 1;
            String url = urls[0];
            link.linkUrl=url;

            try{
                if(url.indexOf("https://") == -1)
                    url = "https://" + url;

                Document rawData = Jsoup.connect(url).get();

                String[] values = url.split("/");

                String base_url = "https://" + values[2];

                Elements icons = rawData.select("link[href~=.*\\.(ico|png)]");
                Elements imgs = rawData.select("meta[property=og:image]");

                String favImage = null;
                if (icons.size() == 0){
                    icons = rawData.select("meta[itemprop=image]");

                    if (icons.size() > 0){
                        favImage = icons.first().attr("content");
                    }
                }
                else{
                    favImage = icons.attr("href");
                }

                if(favImage == null)
                    favImage = "No_Content";
                else if(favImage.indexOf("https://") == -1)
                    favImage = base_url + favImage;

                String main_img;
                if(imgs.size() == 0){
                    main_img = "No_Content";
                }else{
                    main_img = imgs.first().attr("abs:content");
                }

                link.summary = rawData.title();
                link.favicon = favImage;
                link.image = main_img;
                Log.i("pareselink",link.summary +" " +link.favicon);

                return link;
            }catch (IOException e){
            }
            return null;
        }

    }
}

