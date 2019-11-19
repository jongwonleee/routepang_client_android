package com.itaewonproject.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.*
import com.itaewonproject.adapter.AdapterArticleList
import com.itaewonproject.maputils.CategoryIcon
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Product
import com.itaewonproject.rests.GetStrategy
import com.itaewonproject.rests.WebResponce
import com.itaewonproject.rests.get.GetArticleConnector
import com.itaewonproject.rests.post.PostBasketConnector
import com.itaewonproject.rests.post.PostProductConnector
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class ArticleActivity : AppCompatActivity() {

    private var placeId = ""

    private lateinit var location: Location
    private lateinit var recyclerView: RecyclerView
    private lateinit var rating: RatingBar
    private lateinit var address: TextView
    private lateinit var openNow: TextView
    private lateinit var restDays: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var website: TextView
    private lateinit var addressRow: TableRow
    private lateinit var openNowRow: TableRow
    private lateinit var restDaysRow: TableRow
    private lateinit var phoneNumberRow: TableRow
    private lateinit var websiteRow: TableRow
    private lateinit var tableInfo:TableLayout
    private lateinit var usedTime: TextView
    private lateinit var title: TextView
    private lateinit var buttonAddBasket: TextView
    private lateinit var category:ImageView
    private var list = ArrayList<com.itaewonproject.model.receiver.Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_article_list)

        location = intent.getSerializableExtra("Location") as Location
        placeId = location.placeId

        rating = findViewById(R.id.ratingBar_location) as RatingBar
        address = findViewById(R.id.text_address) as TextView
        openNow = findViewById(R.id.text_opennow) as TextView
        restDays = findViewById(R.id.text_restdays) as TextView
        phoneNumber = findViewById(R.id.text_phonenumber) as TextView
        website = findViewById(R.id.text_website) as TextView
        addressRow = findViewById(R.id.row_address) as TableRow
        openNowRow = findViewById(R.id.row_opennow) as TableRow
        restDaysRow = findViewById(R.id.row_restdays) as TableRow
        phoneNumberRow = findViewById(R.id.row_phonenumber) as TableRow
        websiteRow = findViewById(R.id.row_website) as TableRow
        usedTime = findViewById(R.id.text_used_time) as TextView
        title = findViewById(R.id.title_location) as TextView
        buttonAddBasket = findViewById(R.id.button_addBucket) as TextView
        tableInfo = findViewById(R.id.table_info) as TableLayout
        category = findViewById(R.id.image_category)

        category.setImageResource(CategoryIcon.get(location.category!!))
        title.text = location.name
        usedTime.text = "예상 소요시간: ${APIs.secToString(location.usedTime.toInt())}"

        address.setMovementMethod(ScrollingMovementMethod());

        rating.rating = location.rating
        addressRow.visibility=View.GONE
        openNowRow.visibility=View.GONE
        restDaysRow.visibility=View.GONE
        phoneNumberRow.visibility=View.GONE
        websiteRow.visibility=View.GONE
        tableInfo.visibility=View.GONE

        buttonAddBasket.setOnClickListener({
            val product = Product()
            product.location=location.getServerModel()
            val ret = PostProductConnector().post(product,(application as Routepang).customer.customerId)
            if(ret.responceCode!=201){
                Toast.makeText(this,"위시리스트에 추가할 수 없습니다.",Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this,"위시리스트에 추가했습니다!",Toast.LENGTH_LONG).show()
                (application as Routepang).wishlist.add(product.receiverModel)
            }
        })

        setListViewOption()

        jsonParsing(GoogleInfo().get(placeId))
    }

    inner class GoogleInfo : GetStrategy() {
        override var param = ""
        override val inner: String = "maps/api/place/details/"
        override lateinit var mockData: String
        private val key = getString(R.string.google_key)
        init {
            mockData = ""
            domain = "https://maps.googleapis.com/"
        }
        override fun get(vararg params: Any): WebResponce {
            val placeID = params[0] as String
            param = "json?placeid=$placeID&fields=name,formatted_phone_number,formatted_address,permanently_closed,opening_hours,website&key=$key&language=ko"

            var task = Task()
            task.execute()
            Log.i("google info", domain + inner + param)
            return WebResponce(task.get(),statusCode)
        }

    }
    private fun setListViewOption() {
        recyclerView = findViewById(R.id.recyclerview_article_list) as RecyclerView
        list = JsonParser().listJsonParsing(GetArticleConnector().get(location.placeId), Article::class.java)

        val adapter = AdapterArticleList(this, list)

        adapter.setOnItemClickClickListener(object : AdapterArticleList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link.linkUrl))
                startActivity(intent)

            }
        })

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
    }

    fun jsonParsing(result: WebResponce){
        if(result.responceCode!=200) return
        try {
            val json = JSONObject(result.body).getJSONObject("result") as JSONObject
            val phoneNumber = json.optString("formatted_phone_number")
            val address = json.optString("formatted_address")
            val website  = json.optString("website")
            var openNow = ""
            var openArray = json.getJSONObject("opening_hours").getJSONArray("weekday_text") as JSONArray
            var restDays = ""
            var showTable =false
            for (i in 0..openArray.length() - 1) {
                val str = openArray.getString(i)
                if(str.contains("휴무일") || str.contains("Closed")) restDays += str.substring(0,str.indexOf(':'))+", "
            }
            if(restDays.length>0){
                restDays= restDays.substring(0,restDays.length-2)
                restDays += " 휴무"
            }
            if(openArray.length()>0){
                val dayOfWeek = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) +5)%7
                val str =  openArray.getString(dayOfWeek)
                openNow = if(str.contains("휴무일")) "당일 휴무"
                else "당일 " + str.substring(str.indexOf(':')+2) + " 영업"
            }


            if(phoneNumber.length>0){
                this.phoneNumber.text = "$phoneNumber"
                this.phoneNumberRow.visibility=View.VISIBLE
                showTable=true
            }
            if(address.length>0){
                this.address.text = "$address"
                this.addressRow.visibility=View.VISIBLE
                showTable=true
            }
            if(website.length>0){
                this.website.text = "$website"
                this.websiteRow.visibility=View.VISIBLE
                showTable=true
            }
            if(openNow.length>0){
                this.openNow.text = "$openNow"
                this.openNowRow.visibility=View.VISIBLE
                showTable=true
            }
            if(restDays.length>0){
                this.restDays.text = "$restDays"
                this.restDaysRow.visibility=View.VISIBLE
                showTable=true
            }
            if(showTable){
                tableInfo.visibility=View.VISIBLE
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
