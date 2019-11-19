package com.itaewonproject.linkshare

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.RestrictionsManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.itaewonproject.model.sender.*
import com.itaewonproject.rests.authorization
import com.itaewonproject.rests.post.PostArticleConnector
import com.itaewonproject.rests.post.PostProductConnector
import com.itaewonproject.rests.post.PostRatingConnector
import com.squareup.picasso.Picasso
import java.util.*

class LinkShareActivity : AppCompatActivity(){

    private lateinit var textLink: EditText
    lateinit var image: ImageView
    private lateinit var summary: TextView
    private lateinit var checkVisited: CheckedTextView
    private lateinit var checkWishlist: CheckedTextView
    private lateinit var layoutRating: LinearLayout
    lateinit var rating: RatingBar
    lateinit var usedTime: SeekBar
    private lateinit var buttonOk: Button
    private lateinit var buttonCancel: Button
    lateinit var textSeekMax: TextView
    lateinit var textUsedTime: TextView
    private lateinit var buttonSend: Button
    private lateinit var buttonRef: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var link: Link

    private val address = arrayListOf<Location>()
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
                (application as Routepang).token = token
                (application as Routepang).customer = customer!!
                linkSetter(url)
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

    private fun linkSetter(url:String){
        LinkManager(this).LinkApi(url)

    }

    fun onListManagerResult(linkPlaces:LinkPlaces){
        Handler(Looper.getMainLooper()).post( {

            link = linkPlaces.link
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

            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.TYPES)
            ).build(this)
            adapter?.setOnItemClickListener(object:AdapterAddressList.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    startActivityForResult(intent, 1)
                }
            }
            )

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.i("id",place.id)
                val location = Location()
                location.address = place.address
                location.name=place.name
                location.placeId=place.id
                location.category = LocationCategoryParser.get(place.types!![0].name)
                location.coordinates = "POINT (${place.latLng!!.latitude} ${place.latLng!!.longitude})"

                adapter?.addAddress(location)

            } else if (requestCode == RestrictionsManager.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e(ContentValues.TAG, status.statusMessage!!)
            }
        }
    }



    private fun initActivity() {
        textLink = findViewById<EditText>(R.id.text_link)
        image = findViewById<ImageView>(R.id.image_article_small)
        summary = findViewById<TextView>(R.id.text_summary)
        checkVisited = findViewById<CheckedTextView>(R.id.check_visited)
        layoutRating = findViewById<LinearLayout>(R.id.layout_rating)
        rating = findViewById<RatingBar>(R.id.ratingBar3)
        usedTime = findViewById<SeekBar>(R.id.seek_usedTime)
        buttonCancel = findViewById<Button>(R.id.button_cancel)
        buttonOk = findViewById<Button>(R.id.button_ok)
        textSeekMax = findViewById<TextView>(R.id.text_seekMax)
        textUsedTime = findViewById<TextView>(R.id.text_used_time)
        buttonSend = findViewById<Button>(R.id.button_send)
        buttonRef = findViewById<ImageView>(R.id.imageButton_ref)
        recyclerView =findViewById(R.id.recyclerview_address)
        checkWishlist = findViewById<CheckedTextView>(R.id.check_wishlist)
        checkWishlist.isChecked=true
        buttonSend.setOnClickListener {
            linkSetter(textLink.text.toString())
        }

        buttonCancel.setOnClickListener { finish() }
        buttonOk.setOnClickListener {
            var article = Article()
            article.customer = (application as Routepang).customer
            article.image = link.image
            article.link = link
            article.location = adapter!!.getCheckedItem()
            article.summary = link.summary
            if(article.location==null) {
                Toast.makeText(this,"위치를 지정해주세요",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val ret = PostArticleConnector().post(article)
            if(ret.responceCode!=201) {
                Toast.makeText(this,"다시 한번 시도해주세요.",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(checkVisited.isChecked){
                val rating = Rating()
                rating.rating = this.rating.rating
                rating.usedTime = usedTime.progress.toDouble()
                rating.location = adapter!!.getCheckedItem()
                rating.customer = (application as Routepang).customer
                val ret = PostRatingConnector().post(rating)
                if(ret.responceCode!=201){
                    Toast.makeText(this,"별점을 줄 수 없습니다.",Toast.LENGTH_LONG).show()
                }
            }
            if(checkWishlist.isChecked){
                val product = Product()
                product.location=adapter!!.getCheckedItem()
                val ret = PostProductConnector().post(product,(application as Routepang).customer.customerId)
                if(ret.responceCode!=201){
                    Toast.makeText(this,"위시리스트에 추가할 수 없습니다.",Toast.LENGTH_LONG).show()
                }
            }
            finish()
        }
        layoutRating.visibility = View.GONE
        checkVisited.setOnClickListener {
            checkVisited.toggle()
            if (checkVisited.isChecked) {
                layoutRating.visibility = View.VISIBLE
            } else {
                layoutRating.visibility = View.GONE
            }
        }
        checkWishlist.setOnClickListener {checkWishlist.toggle()}
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
