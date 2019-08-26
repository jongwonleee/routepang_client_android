package com.itaewonproject.A

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.animation.ObjectAnimator
import android.content.Intent
import android.view.animation.DecelerateInterpolator
import android.text.Editable
import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.itaewonproject.A.API.API3
import com.itaewonproject.A.API.API4
import com.itaewonproject.A.API.LinkManager
import com.itaewonproject.APIs
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.itaewonproject.ServerModel.Article
import com.itaewonproject.ServerModel.Link
import com.squareup.picasso.Picasso
import java.sql.Timestamp
import java.util.*


class LinkShareActivity : AppCompatActivity() {

    lateinit var textLink:EditText
    lateinit var image:ImageView
    lateinit var summary:TextView
    lateinit var checkVisited:CheckedTextView
    lateinit var layoutRating:LinearLayout
    lateinit var rating:RatingBar
    lateinit var usedTime:SeekBar
    lateinit var buttonOk:Button
    lateinit var buttonCancel:Button
    lateinit var textRating:TextView
    lateinit var textSeekMax:TextView
    lateinit var textUsedTime:TextView
    lateinit var buttonSend:Button
    lateinit var buttonRef:ImageButton
    lateinit var link: Link

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_share)
        link = Link()


        initActivity()

        if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
            var url = intent.getStringExtra(Intent.EXTRA_TEXT)
            textLink.text= Editable.Factory.getInstance().newEditable(url)
            textLink.isEnabled=false
            link.linkUrl=url
            link = LinkManager().LinkApi(link)
            Picasso.with(applicationContext)
                .load(link.image)
                .into(image)
            Picasso.with(applicationContext)
                .load(link.favicon)
                .into(buttonRef)
            summary.text=link.summary
        }
        Places.initialize(applicationContext,"AIzaSyCQBy7WzSBK-kamsMKt6Yk1XpxirVKiW8A")
        var placesClient = Places.createClient(this) as PlacesClient
        val autoCompleteSupportFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_location_search) as AutocompleteSupportFragment
        autoCompleteSupportFragment.setPlaceFields(
            Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG))



        autoCompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                if (place.latLng != null) {
                  /*  mMap.clear()
                    Log.i("!!",place.name)
                    mMap.addMarker(APIs.getMarkerOption(con,place.latLng!!))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))*/
                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
            }
        })
    }

    private fun initActivity(){
        textLink = findViewById(R.id.text_link) as EditText
        image=findViewById(R.id.image_article) as ImageView
        summary=findViewById(R.id.text_summary) as TextView
        checkVisited = findViewById(R.id.checked_visited) as CheckedTextView
        layoutRating= findViewById(R.id.layout_rating) as LinearLayout
        rating= findViewById(R.id.ratingBar3) as RatingBar
        usedTime = findViewById(R.id.seek_usedTime) as SeekBar
        buttonCancel= findViewById(R.id.button_cancel) as Button
        buttonOk= findViewById(R.id.button_ok) as Button
        textRating=findViewById(R.id.text_rating) as TextView
        textSeekMax=findViewById(R.id.text_seekMax) as TextView
        textUsedTime=findViewById(R.id.text_used_time) as TextView
        buttonSend=findViewById(R.id.button_send)as Button
        buttonRef=findViewById(R.id.imageButton_ref)as ImageButton

        buttonSend.setOnClickListener({
            link.linkUrl=textLink.text.toString()
            link = LinkManager().LinkApi(link)
            Picasso.with(applicationContext)
                .load(link.image)
                .into(image)
            Picasso.with(applicationContext)
                .load(link.favicon)
                .into(buttonRef)
            summary.text=link.summary
        })

        buttonCancel.setOnClickListener({
            finish()
        })
        buttonOk.setOnClickListener({
            var article = Article()
            article.customerId=1
            article.image=link.image
            article.link=link
            article.locationId=1
            article.summary=link.summary
            API3().postByCustomerId(article)
            finish()
        })
        layoutRating.visibility= View.GONE
        checkVisited.setOnClickListener({
            checkVisited.toggle()
            if(checkVisited.isChecked){
                layoutRating.visibility=View.VISIBLE
            }else
            {
                layoutRating.visibility=View.GONE
            }
        })

        rating.max=5
        rating.setOnRatingBarChangeListener { ratingBar: RatingBar, fl: Float, b: Boolean ->
            textRating.text="${fl}"
        }
        usedTime.max=3600
        usedTime.progress=1800
        textUsedTime.text= APIs.secToString(usedTime.progress)
        textSeekMax.text= APIs.secToString(usedTime.max)
        usedTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                textUsedTime.text= APIs.secToString(i)


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if(seekBar.progress>=seekBar.max && seekBar.max<86400){
                    var max=seekBar.max*2
                    if(max>86400)max=86400
                    textSeekMax.text= APIs.secToString(max)
                    val animation = ObjectAnimator.ofInt(seekBar, "max", max)
                    animation.duration = 100 // 0.5 second
                    animation.interpolator = DecelerateInterpolator()
                    animation.start()

                }
            }
        })
    }
}

