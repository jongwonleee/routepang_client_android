package com.itaewonproject.RecyclerviewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.ServerResult.Article
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.squareup.picasso.Picasso
import java.lang.invoke.ConstantCallSite

class AdapterArticleList(val context: Context, var output:ArrayList<com.itaewonproject.ServerModel.Article>) : RecyclerView.Adapter<AdapterArticleList.ViewHolder>() {

    private lateinit var listener: onItemClickListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(output[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_article,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return output.size
    }


    interface onItemClickListener{
        fun onItemClick(v: View, position:Int)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener){
        this.listener=listener
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var summary: TextView
        private var imgSmall:ImageView
        private var imgBig:ImageView
        private var buttonRef:ImageButton
        private var layoutArticle:ConstraintLayout
        private var articleId:Long=0
        init{
            summary = itemView.findViewById(R.id.text_summary) as TextView
            imgSmall = itemView.findViewById(R.id.image_article_small) as ImageView
            imgBig = itemView.findViewById(R.id.image_arcticle_big) as ImageView
            buttonRef=itemView.findViewById(R.id.imageButton_ref) as ImageButton
            layoutArticle=itemView.findViewById(R.id.layout_article) as ConstraintLayout
            imgBig.visibility=View.GONE

            layoutArticle.setOnClickListener({
                if(imgBig.visibility==View.GONE){
                    imgBig.visibility=View.VISIBLE
                    imgSmall.visibility=View.INVISIBLE
                }else
                {
                    imgBig.visibility=View.GONE
                    imgSmall.visibility=View.VISIBLE
                }
            })
            imgBig.setOnClickListener({
                imgBig.visibility=View.GONE
                imgSmall.visibility=View.VISIBLE
            })
            buttonRef.setOnClickListener(View.OnClickListener {
                val pos = adapterPosition
                if(pos!=RecyclerView.NO_POSITION){
                    if(listener!=null){
                        listener.onItemClick(it,pos)
                    }
                }
            })
        }
        fun bind(output: com.itaewonproject.ServerModel.Article){
            articleId=output.articleId
            Picasso.with(itemView.context)
                .load(output.image)
                .into(imgSmall)
            Picasso.with(itemView.context)
                .load(output.image)
                .into(imgBig)

            Picasso.with(itemView.context)
                .load(output.favicon_url)
                .transform(RatioTransformation(100))
                .into(buttonRef)
            summary.text=output.summary
            //buttonRef.setImageBitmap(output.getRefIcon())
            /*buttonRef.setOnClickListener(View.OnClickListener {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(output.link))
                (itemView.parent as Context).startActivity(intent)
            })*/
        }
    }

}