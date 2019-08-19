package com.itaewonproject.A

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.animation.ObjectAnimator
import android.content.Intent
import android.view.animation.DecelerateInterpolator
import android.text.Editable
import com.itaewonproject.APIs
import com.itaewonproject.R


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link_share)

        initActivity()
        /*if (Intent.ACTION_SEND == intent.action && intent.type != null) {
            if ("text/plain" == intent.type) {
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)    // 가져온 인텐트의 텍스트 정보
                textLink.text= Editable.Factory.getInstance().newEditable(sharedText)
                textLink.isEnabled=false
            }
        }*/
        if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
            var url = intent.getStringExtra(Intent.EXTRA_TEXT)
            textLink.text= Editable.Factory.getInstance().newEditable(url)
            textLink.isEnabled=false
        }
    }

    private fun initActivity(){
        textLink = findViewById(R.id.text_link) as EditText
        image=findViewById(R.id.imageView) as ImageView
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

        buttonCancel.setOnClickListener({
            finish()
        })
        buttonOk.setOnClickListener({

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

