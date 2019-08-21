package com.itaewonproject.A

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.*
import com.itaewonproject.RecyclerviewAdapter.AdapterArticleList
import com.itaewonproject.ServerResult.Article
import com.itaewonproject.ServerResult.Location
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class LocationArticleActivity : AppCompatActivity() {

    private var placeId=""

    private lateinit var location: Location
    private lateinit var recyclerView: RecyclerView
    private lateinit var rating:RatingBar
    private lateinit var info:TextView
    private lateinit var usedTime:TextView
    private lateinit var title:TextView
    private var list = ArrayList<com.itaewonproject.ServerModel.Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_location_article)

        location = intent.getSerializableExtra("Location") as Location
        //placeId = Location.placeId
        placeId="ChIJN1t_tDeuEmsRUsoyG83frY4"//Google 호주지점

        rating = findViewById(R.id.ratingBar2) as RatingBar
        info = findViewById(R.id.text_info) as TextView
        usedTime=findViewById(R.id.text_used_time) as TextView
        title = findViewById(R.id.title_location) as TextView
        Log.i("!!!","${location.name}")
        title.text=location.name
        usedTime.text="예상 소요시간: ${APIs.secToString(location.usedTime)}"
        rating.rating=location.rating


        setListViewOption()
        jsonParsing(WebParser.getInfo(placeId))
    }

    private fun setListViewOption(){
        recyclerView = findViewById(R.id.recyclerview_article_list) as RecyclerView
        list = APIs.API2(placeId)

        val adapter = AdapterArticleList(this, list)

        adapter.setOnItemClickClickListener(object: AdapterArticleList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link))
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
