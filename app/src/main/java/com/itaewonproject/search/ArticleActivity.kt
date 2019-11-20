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
import com.itaewonproject.model.sender.Rating
import com.itaewonproject.rests.get.GetStrategy
import com.itaewonproject.rests.WebResponce
import com.itaewonproject.rests.delete.DeleteProductConnector
import com.itaewonproject.rests.get.GetArticleConnector
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
    private lateinit var buttonRating:ImageView
    private lateinit var buttonFollow:ImageView

    private var product:com.itaewonproject.model.receiver.Product? = null
    private var list = ArrayList<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR)
        setContentView(R.layout.activity_article_list)

        location = intent.getSerializableExtra("Location") as Location
        product = (application as Routepang).hasProduct(location.getServerModel())
        placeId = location.placeId

        rating = findViewById(R.id.ratingBar_location)
        address = findViewById(R.id.text_address)
        openNow = findViewById(R.id.text_opennow)
        restDays = findViewById(R.id.text_restdays)
        phoneNumber = findViewById(R.id.text_phonenumber)
        website = findViewById(R.id.text_website)
        addressRow = findViewById(R.id.row_address)
        openNowRow = findViewById(R.id.row_opennow)
        restDaysRow = findViewById(R.id.row_restdays)
        phoneNumberRow = findViewById(R.id.row_phonenumber)
        websiteRow = findViewById(R.id.row_website)
        usedTime = findViewById(R.id.text_used_time)
        title = findViewById(R.id.title_location)
        buttonAddBasket = findViewById(R.id.button_addBucket)
        tableInfo = findViewById(R.id.table_info)
        category = findViewById(R.id.image_category)
        buttonRating=findViewById(R.id.button_rating)
        buttonFollow=findViewById(R.id.button_follow)

        category.setImageResource(CategoryIcon.get(location.category!!))
        title.text = location.name
        usedTime.text = "예상 소요시간: ${APIs.secToString(location.usedTime.toInt())}"

        address.movementMethod = ScrollingMovementMethod()

        rating.rating = location.rating
        addressRow.visibility=View.GONE
        openNowRow.visibility=View.GONE
        restDaysRow.visibility=View.GONE
        phoneNumberRow.visibility=View.GONE
        websiteRow.visibility=View.GONE
        tableInfo.visibility=View.GONE

        buttonAddBasket.text = if(product!=null) "-제거" else "+추가"

        buttonRating.setOnClickListener({
            val intent = Intent(this,RatingActivity::class.java)
            intent.putExtra("location",location)
            startActivity(intent)
        })

        buttonAddBasket.setOnClickListener {
            if(product!=null){
                val ret = DeleteProductConnector().delete(product!!.toSenderModel(),(application as Routepang).customer.customerId)
                if(ret.responceCode!=200){
                    Toast.makeText(this,"위시리스트에서 제거할 수 없습니다.",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"위시리스트에서 제거했습니다!",Toast.LENGTH_LONG).show()
                    (application as Routepang).wishlist.remove(product!!)
                    Log.i("wishlist count",(application as Routepang).wishlist.size.toString())
                    product=null
                }
            } else {
                val product = Product()
                product.location=location.getServerModel()
                val ret = PostProductConnector().post(product,(application as Routepang).customer.customerId)
                if(ret.responceCode!=201){
                    Toast.makeText(this,"위시리스트에 추가할 수 없습니다.",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"위시리스트에 추가했습니다!",Toast.LENGTH_LONG).show()
                    (application as Routepang).wishlist.add(product.receiverModel)
                    this.product=product.receiverModel
                }
            }
            buttonAddBasket.text = if(product!=null) "-제거" else "+추가"

        }

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

            val task = Task()
            task.execute()
            Log.i("google info", domain + inner + param)
            return WebResponce(task.get(),statusCode)
        }

    }
    private fun setListViewOption() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview_article_list)
        list = JsonParser().listJsonParsing(GetArticleConnector().get(location.placeId), Article::class.java)

        val adapter = AdapterArticleList(this, list)

        adapter.setOnItemClickClickListener(object : AdapterArticleList.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link.linkUrl))
                startActivity(intent)

            }
        })

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
    }

    private fun jsonParsing(result: WebResponce){
        if(result.responceCode!=200) return
        try {
            val json = JSONObject(result.body).getJSONObject("result") as JSONObject
            val phoneNumber = json.optString("formatted_phone_number")
            val address = json.optString("formatted_address")
            val website  = json.optString("website")
            var openNow = ""
            val openArray = json.getJSONObject("opening_hours").getJSONArray("weekday_text") as JSONArray
            var restDays = ""
            var showTable =false
            for (i in 0 until openArray.length()) {
                val str = openArray.getString(i)
                if(str.contains("휴무일") || str.contains("Closed")) restDays += str.substring(0,str.indexOf(':'))+", "
            }
            if(restDays.isNotEmpty()){
                restDays= restDays.substring(0,restDays.length-2)
                restDays += " 휴무"
            }
            if(openArray.length()>0){
                val dayOfWeek = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) +5)%7
                val str =  openArray.getString(dayOfWeek)
                openNow = if(str.contains("휴무일")) "당일 휴무"
                else "당일 " + str.substring(str.indexOf(':')+2) + " 영업"
            }


            if(phoneNumber.isNotEmpty()){
                this.phoneNumber.text = phoneNumber
                this.phoneNumberRow.visibility=View.VISIBLE
                showTable=true
            }
            if(address.isNotEmpty()){
                this.address.text = address
                this.addressRow.visibility=View.VISIBLE
                showTable=true
            }
            if(website.isNotEmpty()){
                this.website.text = website
                this.websiteRow.visibility=View.VISIBLE
                showTable=true
            }
            if(openNow.isNotEmpty()){
                this.openNow.text = openNow
                this.openNowRow.visibility=View.VISIBLE
                showTable=true
            }
            if(restDays.isNotEmpty()){
                this.restDays.text = restDays
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
