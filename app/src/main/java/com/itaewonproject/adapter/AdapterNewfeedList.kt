package com.itaewonproject.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.itaewonproject.APIs
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.itaewonproject.customviews.RoundedImageView
import com.itaewonproject.maputils.CategoryIcon
import com.itaewonproject.model.receiver.*
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.model.sender.Link
import com.squareup.picasso.Picasso

class AdapterNewfeedList(val context: Context, val list: ArrayList<Newsfeed>) : RecyclerView.Adapter<BaseViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        lateinit var view:View
        when(viewType){
            0->{
                view = LayoutInflater.from(context).inflate(R.layout.list_article, parent, false)
                return ArticleViewHolder(view)
            }
            1->{
                view = LayoutInflater.from(context).inflate(R.layout.list_location, parent, false)
                return LocationViewHolder(view)
            }
            2->{
                view = LayoutInflater.from(context).inflate(R.layout.list_feed_route, parent, false)
                return FeedRouteViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(context).inflate(R.layout.list_feed_route, parent, false)
                return FeedRouteViewHolder(view)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position].eventType){
            NewsfeedCategory.ARTICLE-> 0
            NewsfeedCategory.LOCATION->1
            NewsfeedCategory.ROUTE->2
        }
    }

    interface OnItemClickListener {
        fun onReferenceClick(link : Link)
        fun onProfileClick(customer: Customer)
        fun onLocationClick(location :Location)
    }

    fun setOnItemClickClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class FeedRouteViewHolder(itemView: View):BaseViewHolder(itemView){
        val textLocation:TextView
        val textTitle:TextView
        val recyclerView:RecyclerView
        init{
            textLocation = itemView.findViewById(R.id.text_location)
            textTitle = itemView.findViewById(R.id.text_title)
            recyclerView = itemView.findViewById(R.id.recyclerView)
        }
        override fun bind(pos: Int) {
            val route = JsonParser().objectJsonParsing(list[pos].jsonData,Route::class.java)!!
            textTitle.text=route.title
            textLocation.text = route.boundary

            val adapter = AdapterFeedMarkerList(context,route.products)
            recyclerView.adapter = adapter
            val linearLayoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(true)

        }

    }
    inner class LocationViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val title: TextView
        private val imgList: RecyclerView
        val rating: RatingBar
        private var placeId = ""
        private val imageCategory: ImageView
        val usedTime: TextView
        private val articleCount : TextView

        init {
            title = itemView.findViewById(R.id.textView_title) as TextView
            rating = itemView.findViewById(R.id.ratingBar_location) as RatingBar
            imgList = itemView.findViewById(R.id.recyclerView_images) as RecyclerView
            imageCategory = itemView.findViewById(R.id.image_category) as ImageView
            usedTime = itemView.findViewById(R.id.text_used_time) as TextView
            articleCount = itemView.findViewById(R.id.text_article_count)

        }
        override fun bind(pos: Int) {
            val location = JsonParser().objectJsonParsing(list[pos].jsonData,Location::class.java)!!
            this.title.text = location.name
            this.rating.rating = location.rating
            this.placeId = location.placeId
            this.articleCount.text = "${location.articleCount}개의 게시물"
            this.imageCategory.setImageResource(CategoryIcon.get(location.category!!))
            this.usedTime.text = "평균 소요 시간: ${APIs.secToString(location.usedTime.toInt())}"
            Log.i("imageCount","${location.images.size}")

            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onLocationClick(location)
                }
            }
            val adapter = AdapterImageList(itemView.context, location.images)

            imgList.adapter = adapter

            val linearLayoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            imgList.layoutManager = linearLayoutManager
            imgList.setHasFixedSize(true)
        }
    }
    inner class ArticleViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private var summary: TextView
        private var imgSmall: RoundedImageView
        private var imgBig: RoundedImageView
        private var profile :ImageView
        private val imageMeasure:ImageView
        private val buttonRef: ImageView
        private val textName:TextView
        private var layoutArticle: ConstraintLayout
        private var articleId: Long = 0
        private var imgSmallWidth=0
        private var imgSmallHeight=0
        private var summaryHeight=0
        private val scale = context.resources.displayMetrics.density
        private val pixels = (70 * scale + 0.5f).toInt()
        init {
            summary = itemView.findViewById(R.id.text_summary) as TextView
            textName = itemView.findViewById(R.id.text_name)
            profile = itemView.findViewById(R.id.image_profile)
            imgSmall = itemView.findViewById(R.id.image_article_small) as RoundedImageView
            imgBig = itemView.findViewById(R.id.image_arcticle_big) as RoundedImageView
            buttonRef = itemView.findViewById(R.id.imageButton_ref) as ImageView
            layoutArticle = itemView.findViewById(R.id.layout_article) as ConstraintLayout
            imageMeasure = itemView.findViewById(R.id.image_measure) as ImageView

            layoutArticle.setOnClickListener {
                if (imgBig.visibility == View.GONE) {
                    openAnimation()
                } else {
                    closeAnimation()
                }
            }

        }

        private fun closeAnimation(){
            val params = summary.layoutParams
            params.height = pixels
            summary.layoutParams=params
            layoutArticle.isClickable=false

            val imgBigHeightAnimator = ValueAnimator.ofInt(imageMeasure.height,imgSmallHeight)
            val imgBigWidthAnimator = ValueAnimator.ofInt(imageMeasure.width,imgSmallWidth)
            val imgSmallWidthAnimator = ValueAnimator.ofInt(0,imgSmallWidth)
            imgSmallWidthAnimator.duration= 300
            imgBigHeightAnimator.duration=300
            imgBigWidthAnimator.duration=300
            imgBigHeightAnimator.addUpdateListener {
                val p = imgBig.layoutParams
                p.height = imgBigHeightAnimator.animatedValue as Int
                imgBig.layoutParams = p
            }
            imgBigHeightAnimator.addUpdateListener {
                val p = imgBig.layoutParams
                p.width = imgBigWidthAnimator.animatedValue as Int
                imgBig.layoutParams = p
            }

            imgSmallWidthAnimator.addUpdateListener {
                val p = imgSmall.layoutParams
                p.width=imgSmallWidthAnimator.animatedValue as Int
                imgSmall.layoutParams=p
            }
            imgBigHeightAnimator.start()
            imgBigWidthAnimator.start()
            imgSmallWidthAnimator.start()

            val animation = TranslateAnimation(0f,(imageMeasure.width/2-(64 * scale + 0.5f).toInt()).toFloat(),0f,-imgSmallHeight.toFloat()-(16 * scale + 0.5f).toInt())
            animation.duration=300

            animation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    imgSmall.visibility = View.VISIBLE
                    imgBig.visibility=View.GONE
                    layoutArticle.isClickable=true
                    summary.maxLines=3
                }

                override fun onAnimationStart(p0: Animation?) {
                }

            })
            imgBig.startAnimation(animation)
        }
        private fun openAnimation(){
            imgSmallWidth = imgSmall.width
            imgSmallHeight = imgSmall.height
            summaryHeight = summary.height
            imgBig.visibility = View.VISIBLE
            imgSmall.visibility = View.INVISIBLE
            layoutArticle.isClickable=false
            val params = summary.layoutParams
            summary.maxLines=6
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            summary.layoutParams=params
            layoutArticle.isClickable=false
            val imgBigHeightAnimator = ValueAnimator.ofInt(imgSmallHeight,imageMeasure.height)
            val imgBigWidthAnimator = ValueAnimator.ofInt(imgSmallWidth,imageMeasure.width)
            val imgSmallWidthAnimator = ValueAnimator.ofInt(imgSmallWidth,0)
            imgBigHeightAnimator.duration=300
            imgBigWidthAnimator.duration=300
            imgSmallWidthAnimator.duration=300

            imgBigHeightAnimator.addUpdateListener {
                val p = imgBig.layoutParams
                p.height = imgBigHeightAnimator.animatedValue as Int
                imgBig.layoutParams = p
            }
            imgBigWidthAnimator.addUpdateListener {
                val p = imgBig.layoutParams
                p.width = imgBigWidthAnimator.animatedValue as Int
                imgBig.layoutParams = p
            }
            imgSmallWidthAnimator.addUpdateListener {
                val p = imgSmall.layoutParams
                p.width=imgSmallWidthAnimator.animatedValue as Int
                imgSmall.layoutParams=p
            }

            imgBigHeightAnimator.start()
            imgBigWidthAnimator.start()
            imgSmallWidthAnimator.start()
            val animation = TranslateAnimation((imgBig.width/2+(74 * scale + 0.5f).toInt()).toFloat(),0f,-imgSmallHeight.toFloat()-(16 * scale + 0.5f).toInt(),0f)
            animation.duration=300

            animation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {

                    layoutArticle.isClickable=true
                }

                override fun onAnimationStart(p0: Animation?) {
                }

            })
            imgBig.startAnimation(animation)
        }
        override fun bind(pos: Int) {
            //val article = //JsonParser().objectJsonParsing(list[pos].jsonData, Article::class.java)!!
            val article = Gson().fromJson<Article>(list[pos].jsonData,Article::class.java)
            articleId = article.articleId
            Picasso.with(itemView.context)
                .load(article.link.image)
                .into(imgSmall)
            Picasso.with(itemView.context)
                .load(article.link.image)
                .into(imgBig)
            Picasso.with(itemView.context)
                .load(article.link.image)
                .into(imageMeasure)
            Picasso.with(itemView.context)
                .load(article.link.favicon)
                .transform(RatioTransformation(200))
                .into(buttonRef)
            summary.text = article.summary
            summary.maxLines=3
            imgBig.visibility=View.GONE
            textName.text = article.customer.reference

            profile.setOnClickListener({
                listener.onProfileClick(article.customer)
            })
            textName.setOnClickListener({
                listener.onProfileClick(article.customer)
            })
            buttonRef.setOnClickListener({
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onReferenceClick(article.link)
                }
            })
        }
    }
}
