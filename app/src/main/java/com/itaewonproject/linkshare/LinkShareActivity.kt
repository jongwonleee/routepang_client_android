package com.itaewonproject.linkshare

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.RestrictionsManager
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.itaewonproject.APIs
import com.itaewonproject.JsonParser
import com.itaewonproject.rests.IS_OFFLINE
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterAddressList
import com.itaewonproject.landingpage.LoginActivity
import com.itaewonproject.maputils.LocationCategoryParser
import com.itaewonproject.model.sender.Article
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.model.sender.Link
import com.itaewonproject.model.sender.Location
import com.itaewonproject.rests.authorization
import com.itaewonproject.rests.post.PostArticleConnector
import com.squareup.picasso.Picasso
import java.util.*

class LinkShareActivity : AppCompatActivity(){

    lateinit var textLink: EditText
    lateinit var image: ImageView
    lateinit var summary: TextView
    lateinit var checkVisited: CheckedTextView
    lateinit var checkWishlist: CheckedTextView
    lateinit var layoutRating: LinearLayout
    lateinit var rating: RatingBar
    lateinit var usedTime: SeekBar
    lateinit var buttonOk: Button
    lateinit var buttonCancel: Button
    lateinit var textSeekMax: TextView
    lateinit var textUsedTime: TextView
    lateinit var buttonSend: Button
    lateinit var buttonRef: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var link: Link

    val address = arrayListOf<Location>()
    var adapter:AdapterAddressList?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_share)
        if(IS_OFFLINE){
            Toast.makeText(applicationContext,"인터넷 연결이 되지 않아 접속할 수 없습니다",Toast.LENGTH_LONG).show()
            finish()
        }

        initActivity()


        if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
            val url = intent.getStringExtra(Intent.EXTRA_TEXT)
            textLink.text = Editable.Factory.getInstance().newEditable(url)
            textLink.isEnabled = false
            val sharedPreferences =  getSharedPreferences("autoLogin", Context.MODE_PRIVATE)
            val isAutoLogin = sharedPreferences.getBoolean("autoLoginCheck",false)
            if(isAutoLogin){
                val token = sharedPreferences.getString("loginToken","")
                val customer = JsonParser().objectJsonParsing(sharedPreferences.getString("autoLoginCustomer","{}")!!,
                    Customer::class.java)
                authorization = token!!
                (application as Routepang).token = token!!
                (application as Routepang).customer = customer!!
                LinkSetter(url)
            }else
            {
                Toast.makeText(this,"로그인이 되지 않아 로그인 창으로 이동합니다.",Toast.LENGTH_LONG).show()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
        }
        Places.initialize(applicationContext, getString(R.string.google_key))
        Places.createClient(this)
    }

    private fun LinkSetter(url:String){
        val linkPlaces = LinkManager(this).LinkApi(url)
        if (linkPlaces == null) {
            textLink.text = Editable.Factory.getInstance().newEditable("")
            Toast.makeText(this,"링크를 가져올 수 없습니다. 다시 시도해주세요",Toast.LENGTH_LONG).show()
            return
        }

        link = linkPlaces.link!!
        address.clear()
        address.addAll(linkPlaces.list)
        Picasso.with(applicationContext)
            .load(link.image)
            .into(image)
        Picasso.with(applicationContext)
            .load(link.favicon)
            .into(buttonRef)
        summary.text = link.summary

        adapter = AdapterAddressList(this, address)

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        var intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.TYPES)).build(this)
        adapter?.setOnItemClickListener(object:AdapterAddressList.OnItemClickListener {
            override fun onItemClick(position: Int) {
                startActivityForResult(intent, 1)
            }
        }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                var place = Autocomplete.getPlaceFromIntent(data!!)
                Log.i("id",place.id)
                val location = Location()
                location.address = place.address
                location.name=place.name
                location.placeId=place.id
                location.category = LocationCategoryParser.get(place.types!![0].name)
                location.coordinates = "POINT (${place.latLng!!.latitude} ${place.latLng!!.longitude})"

                adapter?.addAddress(location)

            } else if (requestCode == RestrictionsManager.RESULT_ERROR) {
                var status = Autocomplete.getStatusFromIntent(data!!)
                Log.e(ContentValues.TAG, status.statusMessage!!)
            }
        }
    }



    private fun initActivity() {
        textLink = findViewById(R.id.text_link) as EditText
        image = findViewById(R.id.image_article_small) as ImageView
        summary = findViewById(R.id.text_summary) as TextView
        checkVisited = findViewById(R.id.check_visited) as CheckedTextView
        layoutRating = findViewById(R.id.layout_rating) as LinearLayout
        rating = findViewById(R.id.ratingBar3) as RatingBar
        usedTime = findViewById(R.id.seek_usedTime) as SeekBar
        buttonCancel = findViewById(R.id.button_cancel) as Button
        buttonOk = findViewById(R.id.button_ok) as Button
        textSeekMax = findViewById(R.id.text_seekMax) as TextView
        textUsedTime = findViewById(R.id.text_used_time) as TextView
        buttonSend = findViewById(R.id.button_send)as Button
        buttonRef = findViewById(R.id.imageButton_ref)as ImageView
        recyclerView =findViewById(R.id.recyclerview_address)
        checkWishlist = findViewById(R.id.check_wishlist) as CheckedTextView
        checkWishlist.isChecked=true
        buttonSend.setOnClickListener({
            LinkSetter(textLink.text.toString())
        })

        buttonCancel.setOnClickListener({ finish() })
        buttonOk.setOnClickListener({
            var article = Article()
            article.customer = (application as Routepang).customer
            article.image = link.image
            article.link = link
            article.location = adapter!!.getCheckedItem()
            article.summary = link.summary
            if(article.location==null)
            {
                Toast.makeText(this,"위치를 지정해주세요",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val ret = PostArticleConnector().post(article)
            if(ret.responceCode==201)
            {
                Toast.makeText(this,"저장 완료!",Toast.LENGTH_LONG).show()
            }else
            {
                Toast.makeText(this,"다시 한번 시도해주세요.",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(checkVisited.isChecked){

            }
            if(checkWishlist.isChecked){
                
            }
        })
        layoutRating.visibility = View.GONE
        checkVisited.setOnClickListener({
            checkVisited.toggle()
            if (checkVisited.isChecked) {
                layoutRating.visibility = View.VISIBLE
            } else {
                layoutRating.visibility = View.GONE
            }
        })


        rating.max = 5
        usedTime.max = 3600
        usedTime.progress = 1800
        textUsedTime.text = APIs.secToString(usedTime.progress)
        textSeekMax.text = APIs.secToString(usedTime.max)
        usedTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                textUsedTime.text = APIs.secToString(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (seekBar.progress >= seekBar.max && seekBar.max <86400) {
                    var max = seekBar.max*2
                    if (max> 86400)max = 86400
                    textSeekMax.text = APIs.secToString(max)
                    val animation = ObjectAnimator.ofInt(seekBar, "max", max)
                    animation.duration = 100 // 0.5 second
                    animation.interpolator = DecelerateInterpolator()
                    animation.start()
                }
            }
        })
    }
}
