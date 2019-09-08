package com.itaewonproject.search

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.*
import com.itaewonproject.player.ArticleConnector
import com.itaewonproject.adapter.AdapterArticleList
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.player.BasketConnector
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ArticleActivity : AppCompatActivity() {

    private var placeId=""

    private lateinit var location: Location
    private lateinit var recyclerView: RecyclerView
    private lateinit var rating:RatingBar
    private lateinit var info:TextView
    private lateinit var usedTime:TextView
    private lateinit var title:TextView
    private lateinit var buttonAddBasket:ImageView
    private var list = ArrayList<com.itaewonproject.model.receiver.Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_article_list)

        location = intent.getSerializableExtra("Location") as Location
        placeId = location.placeId
        //placeId="ChIJN1t_tDeuEmsRUsoyG83frY4"//Google 호주지점

        rating = findViewById(R.id.ratingBar2) as RatingBar
        info = findViewById(R.id.text_info) as TextView
        usedTime=findViewById(R.id.text_used_time) as TextView
        title = findViewById(R.id.title_location) as TextView
        buttonAddBasket = findViewById(R.id.button_addBucket) as ImageView
        Log.i("!!!","${location.name}")
        title.text=location.name
        usedTime.text="예상 소요시간: ${APIs.secToString(location.used.toInt())}"
        rating.rating=location.rating


        buttonAddBasket.setOnClickListener({
            BasketConnector().addBasketByLocation(1,location)
        })

        setListViewOption()

        jsonParsing(GoogleInfo().get(placeId))
    }

    inner class GoogleInfo : WebConnectStrategy() {
        override fun get(vararg params:Any): String {
            val placeID = params[0] as String
            domain = "https://maps.googleapis.com/"
            param = "json?placeid=${placeID}&fields=name,rating,formatted_phone_number,opening_hours&key=${key}"

            var task = Task()
            task.execute()
            Log.i("google info",task.get())
            return task.get()
        }

        override var param=""
        override var method: String = "GET"
        override var inner: String ="maps/api/place/details/"
        override lateinit var mockData: String
        private val key=getString(R.string.Web_key)
        init{
            mockData=""
        }

    }
    private fun setListViewOption(){
        recyclerView = findViewById(R.id.recyclerview_article_list) as RecyclerView
        list = JsonParser().listJsonParsing(ArticleConnector().get(location.locationId),Article::class.java)

        val adapter = AdapterArticleList(this, list)

        adapter.setOnItemClickClickListener(object: AdapterArticleList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link.linkUrl))
                startActivity(intent)
            }

        })

        recyclerView.adapter=adapter

        val linearLayoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=linearLayoutManager
        recyclerView.setHasFixedSize(false)
    }

    fun jsonParsing(str:String){
        try{
            var json = JSONObject(str).getJSONObject("result") as JSONObject
            val phoneNumber = json.optString("formatted_phone_number")
            var openArray = json.getJSONObject("opening_hours").getJSONArray("weekday_text") as JSONArray
            var open=""
            for(i in 0 .. openArray.length()-1)
            {
                open += "${openArray.getString(i)}+\n"
            }
            this.info.text = "전화번호: $phoneNumber\n$open"
        }catch(e:JSONException)
        {
            e.printStackTrace()
        }
    }
}
