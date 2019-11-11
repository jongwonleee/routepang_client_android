package com.itaewonproject.adapter

import android.animation.ValueAnimator
import android.content.Context
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
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation


class AdapterArticleList(val context: Context, var output: ArrayList<com.itaewonproject.model.receiver.Article>) : RecyclerView.Adapter<AdapterArticleList.ViewHolder>() {

    private lateinit var listener: onItemClickListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_article, parent, false)
        return ViewHolder(view,parent)
    }

    override fun getItemCount(): Int {
        return output.size
    }

    interface onItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View,parent: ViewGroup) : BaseViewHolder(itemView) {

        private var summary: TextView
        private var imgSmall: RoundedImageView
        private var imgBig: RoundedImageView
        private val imageMeasure:ImageView
        private var buttonRef: ImageView
        private var layoutArticle: ConstraintLayout
        private var articleId: Long = 0
        var imgSmallWidth=0
        var imgSmallHeight=0
        var summaryHeight=0
        val scale = context.resources.displayMetrics.density
        val pixels = (70 * scale + 0.5f).toInt()
        init {
            summary = itemView.findViewById(R.id.text_summary) as TextView
            imgSmall = itemView.findViewById(R.id.image_article_small) as RoundedImageView
            imgBig = itemView.findViewById(R.id.image_arcticle_big) as RoundedImageView
            buttonRef = itemView.findViewById(R.id.imageButton_ref) as ImageView
            layoutArticle = itemView.findViewById(R.id.layout_article) as ConstraintLayout
            imageMeasure = itemView.findViewById(R.id.image_measure) as ImageView

            layoutArticle.setOnClickListener({
                if (imgBig.visibility == View.GONE) {
                    openAnimation()
                } else {
                    closeAnimation()
                }
            })
            buttonRef.setOnClickListener(View.OnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    if (listener != null) {
                        listener.onItemClick(it, pos)
                    }
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
            imgSmallWidthAnimator.duration= 500
            imgBigHeightAnimator.duration=500
            imgBigWidthAnimator.duration=500
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
            animation.duration=500

            animation.setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    imgSmall.visibility = View.VISIBLE
                    imgBig.visibility=View.GONE
                    layoutArticle.isClickable=true
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
            val imgBigHeightAnimator = ValueAnimator.ofInt(imgSmallHeight,imageMeasure.height)
            val imgBigWidthAnimator = ValueAnimator.ofInt(imgSmallWidth,imageMeasure.width)
            val imgSmallWidthAnimator = ValueAnimator.ofInt(imgSmallWidth,0)
            imgBigHeightAnimator.duration=500
            imgBigWidthAnimator.duration=500
            imgSmallWidthAnimator.duration=500

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
            val animation = TranslateAnimation((imgBig.width/2+(70 * scale + 0.5f).toInt()).toFloat(),0f,-imgSmallHeight.toFloat()-(16 * scale + 0.5f).toInt(),0f)
            animation.duration=500

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
            var output = output[pos]
            articleId = output.articleId
            Picasso.with(itemView.context)
                .load(output.link.image)
                .into(imgSmall)
            Picasso.with(itemView.context)
                .load(output.link.image)
                .into(imgBig)
            Picasso.with(itemView.context)
                .load(output.link.image)
                .into(imageMeasure)
            Picasso.with(itemView.context)
                .load(output.link.favicon)
                .transform(RatioTransformation(200))
                .into(buttonRef)
            summary.text = output.summary
            imgBig.visibility=View.GONE

        }
    }
}
