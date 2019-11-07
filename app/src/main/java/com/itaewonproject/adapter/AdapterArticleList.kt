package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.itaewonproject.customviews.RoundedImageView
import com.squareup.picasso.Picasso

class AdapterArticleList(val context: Context, var output: ArrayList<com.itaewonproject.model.receiver.Article>) : RecyclerView.Adapter<AdapterArticleList.ViewHolder>() {

    private lateinit var listener: onItemClickListener

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

    interface onItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private var summary: TextView
        private var imgSmall: RoundedImageView
        private var imgBig: RoundedImageView
        private var buttonRef: ImageView
        private var layoutArticle: ConstraintLayout
        private var articleId: Long = 0
        init {
            summary = itemView.findViewById(R.id.text_summary) as TextView
            imgSmall = itemView.findViewById(R.id.image_article_small) as RoundedImageView
            imgBig = itemView.findViewById(R.id.image_arcticle_big) as RoundedImageView
            buttonRef = itemView.findViewById(R.id.imageButton_ref) as ImageView
            layoutArticle = itemView.findViewById(R.id.layout_article) as ConstraintLayout
            imgBig.visibility = View.GONE

            layoutArticle.setOnClickListener({
                if (imgBig.visibility == View.GONE) {
                    imgBig.visibility = View.VISIBLE
                    imgSmall.visibility = View.INVISIBLE
                } else {
                    imgBig.visibility = View.GONE
                    imgSmall.visibility = View.VISIBLE
                }
            })
            imgBig.setOnClickListener({
                imgBig.visibility = View.GONE
                imgSmall.visibility = View.VISIBLE
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
                .load(output.link.favicon)
                .transform(RatioTransformation(200))
                .into(buttonRef)
            summary.text = output.summary
        }
    }
}
