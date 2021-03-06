package com.itaewonproject.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.itaewonproject.customviews.RoundedImageView
import com.squareup.picasso.Picasso
import android.view.animation.TranslateAnimation
import com.itaewonproject.model.sender.Customer


class AdapterArticleList(val context: Context, var output: ArrayList<com.itaewonproject.model.receiver.Article>) : RecyclerView.Adapter<AdapterArticleList.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return output.size
    }

    interface OnItemClickListener {
        fun onReferenceClick(position: Int)
        fun onProfileClick(customer: Customer)
    }

    fun setOnItemClickClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

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
            buttonRef.setOnClickListener({
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                        listener.onReferenceClick(pos)
                }
            })
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

            animation.setAnimationListener(object :Animation.AnimationListener{
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

            animation.setAnimationListener(object :Animation.AnimationListener{
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
            val output = output[pos]
            articleId = output.articleId
            Picasso.with(itemView.context)
                .load(output.link.image)
                .placeholder(R.drawable.box_empty_location)
                .into(imgSmall)
            Picasso.with(itemView.context)
                .load(output.link.image)
                .placeholder(R.drawable.box_empty_location)
                .into(imgBig)
            Picasso.with(itemView.context)
                .load(output.link.image)
                .placeholder(R.drawable.box_empty_location)
                .into(imageMeasure)
            Picasso.with(itemView.context)
                .load(output.link.favicon)
                .placeholder(R.drawable.box_empty_location)
                .transform(RatioTransformation(200))
                .into(buttonRef)
            summary.text = output.summary
            summary.maxLines=3
            imgBig.visibility=View.GONE
            textName.text = output.customer.reference

            profile.setOnClickListener({
                listener.onProfileClick(output.customer)
            })
            textName.setOnClickListener({
                listener.onProfileClick(output.customer)
            })
        }
    }
}
