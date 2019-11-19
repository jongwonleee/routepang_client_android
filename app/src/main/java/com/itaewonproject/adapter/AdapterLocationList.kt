package com.itaewonproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.APIs
import com.itaewonproject.maputils.CategoryIcon
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.model.receiver.Location

class AdapterLocationList(val context: Context, var output: ArrayList<Location>) : RecyclerView.Adapter<AdapterLocationList.ViewHolder>() {

    private lateinit var listener: onItemClickListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_location, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return output.size
    }

    fun add(oll: Location) {
        output.add(oll)
        notifyDataSetChanged()
    }

    fun refreshList(list:ArrayList<Location>){
        this.output =list
        Log.i("wishlist count",list.size.toString())
        notifyDataSetChanged()
    }


    interface onItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val title: TextView
        val imgList: RecyclerView
        val rating: RatingBar
        var placeId = ""
        val imageCategory: ImageView
        val usedTime: TextView
        val articleCount : TextView

        init {
            title = itemView.findViewById(R.id.textView_title) as TextView
            rating = itemView.findViewById(R.id.ratingBar_location) as RatingBar
            imgList = itemView.findViewById(R.id.recyclerView_images) as RecyclerView
            imageCategory = itemView.findViewById(R.id.image_category) as ImageView
            usedTime = itemView.findViewById(R.id.text_used_time) as TextView
            articleCount = itemView.findViewById(R.id.text_article_count)
            itemView.setOnClickListener({
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
            this.title.text = output.name
            this.rating.rating = output.rating
            this.placeId = output.placeId
            this.articleCount.text = "${output.articleCount}개의 게시물"
            this.imageCategory.setImageResource(CategoryIcon.get(output.category!!))
            this.usedTime.text = "평균 소요 시간: ${APIs.secToString(output.usedTime.toInt())}"
            Log.i("imageCount","${output.imgUrl.size}")
            val adapter = AdapterImageList(itemView.context, output.imgUrl)

            imgList.adapter = adapter

            val linearLayoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            imgList.layoutManager = linearLayoutManager
            imgList.setHasFixedSize(true)
        }
    }
}
