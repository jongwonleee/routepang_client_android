package com.itaewonproject.linkshare;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.itaewonproject.maputils.LocationCategoryParser;
import com.itaewonproject.rests.HttpClient;
import com.itaewonproject.NaverTranslater;
import com.itaewonproject.R;
import com.itaewonproject.model.sender.Link;
import com.itaewonproject.model.sender.Location;
import com.itaewonproject.rests.WebStrategyKt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutionException;



public class LinkManager {
    private final Context context;
    LinkManager(Context context){
        this.context=context;
    }


    public void LinkApi(String url) {
        if(WebStrategyKt.IS_OFFLINE) return;
        System.out.println("url req");

        Task task = new Task();
        task.execute(url);

        try{
            task.get();
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    class Task extends AsyncTask<String, Integer, LinkPlaces> {
        @Override
        protected LinkPlaces doInBackground(String... urls) {
            LinkPlaces linkPlaces = new LinkPlaces();
            Link link = linkPlaces.getLink();
            assert urls.length == 1;
            String url = urls[0];
            link.linkUrl=url;

            try{
                if(!url.contains("https://"))
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
                else if(!favImage.contains("https://"))
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

                String translated = new NaverTranslater(context.getString(R.string.naver_client_id),context.getString(R.string.naver_client_secret)).get(link.summary.replaceAll("#","+"));

                if(!translated.equals("notTranslated")){
                    translated = translated.substring(translated.indexOf("translatedText")+17,translated.lastIndexOf('"'));
                    translated = translated.replaceAll(" ","+");
                    String address = "https://maps.googleapis.com/maps/api/geocode/json?key="+ context.getString(R.string.google_key) +"&language=ko&address="+translated+"+"+link.summary.replaceAll(" ","+").replaceAll("#","+");
                    HttpClient.Builder http = new HttpClient.Builder("GET", address); // 포트번호,서블릿주소
                    Log.i("domain!!",address);
                    HttpClient post = http.create();

                    post.request();
                    try {
                        JSONObject result = new JSONObject(post.getBody());
                        String status = result.optString("status");
                        if(status.contains("OK")){
                            JSONArray locations = result.getJSONArray("results");
                            for (int i =0; i<locations.length();i++) {
                                Location location = new Location();
                                location.placeId = locations.getJSONObject(i).optString("place_id");
                                location.address = locations.getJSONObject(i).optString("formatted_address");
                                location.name = locations.getJSONObject(i).getJSONArray("address_components").getJSONObject(0).optString("short_name");
                                location.category = LocationCategoryParser.INSTANCE.get(locations.getJSONObject(i).getJSONArray("types").get(0).toString());
                                double lat = locations.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                double lng = locations.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                location.coordinates = "POINT ("+lng+" "+lat+")";
                                linkPlaces.getList().add(location);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                ((LinkShareActivity) context).onListManagerResult(linkPlaces);
                return linkPlaces;
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

    }
}

