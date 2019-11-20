package com.itaewonproject.search

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.itaewonproject.APIs
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Rating
import com.itaewonproject.rests.post.PostRatingConnector

class RatingActivity : AppCompatActivity() {

    private lateinit var rating: RatingBar
    private lateinit var usedTime: SeekBar
    private lateinit var buttonOk: Button
    private lateinit var buttonCancel: Button
    private lateinit var textSeekMax: TextView
    private lateinit var textUsedTime: TextView

    private lateinit var location: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        location = intent.getSerializableExtra("location") as Location
        rating = findViewById<RatingBar>(R.id.ratingBar3)
        usedTime = findViewById<SeekBar>(R.id.seek_usedTime)
        buttonCancel = findViewById<Button>(R.id.button_cancel)
        buttonOk = findViewById<Button>(R.id.button_ok)
        textSeekMax = findViewById<TextView>(R.id.text_seekMax)
        textUsedTime = findViewById<TextView>(R.id.text_used_time)
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

        buttonCancel.setOnClickListener({
            finish()
        })
        buttonOk.setOnClickListener({
            val rating = Rating()
            rating.rating = this.rating.rating
            rating.usedTime = usedTime.progress.toDouble()
            rating.location = location.getServerModel()
            rating.customer = (application as Routepang).customer
            val ret = PostRatingConnector().post(rating)
            if(ret.responceCode!=201){
                Toast.makeText(this,"별점을 줄 수 없습니다.", Toast.LENGTH_LONG).show()
            }else
            {
                Toast.makeText(this,"좋은 여행이셨길!", Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }
}
