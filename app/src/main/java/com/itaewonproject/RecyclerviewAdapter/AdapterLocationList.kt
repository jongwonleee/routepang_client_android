package com.itaewonproject.RecyclerviewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.itaewonproject.APIs
import com.itaewonproject.ServerResult.Location
import com.itaewonproject.R

class AdapterLocationList(val context: Context, var output:ArrayList<Location>) : RecyclerView.Adapter<AdapterLocationList.ViewHolder>() {

    private lateinit var listener: onItemClickListener


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(output[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_location,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return output.size
    }

    fun add(oll: Location){
        output.add(oll)
        notifyDataSetChanged()
    }

    interface onItemClickListener{
        fun onItemClick(v: View, position:Int)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener){
        this.listener=listener
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var imgList: RecyclerView
        var rating:RatingBar
        var placeId=""
        var imageCategory:ImageView
        var usedTime:TextView
        var articleCount:TextView

        init{
            title = itemView.findViewById(R.id.textView_title) as TextView
            rating = itemView.findViewById(R.id.ratingBar_location) as RatingBar
            imgList = itemView.findViewById(R.id.recyclerView_images) as RecyclerView
            imageCategory=itemView.findViewById(R.id.image_category) as ImageView
            usedTime=itemView.findViewById(R.id.text_used_time) as TextView
            articleCount=itemView.findViewById(R.id.text_articleCount) as TextView
            itemView.setOnClickListener(View.OnClickListener {
                val pos = adapterPosition
                if(pos!=RecyclerView.NO_POSITION){
                    if(listener!=null){
                        listener.onItemClick(it,pos)
                    }
                }
            })
        }
        fun bind(output: Location){
            this.title.text=output.name
            this.rating.rating=output.rating
            this.placeId=output.placeId
            this.imageCategory.setImageBitmap(APIs.getCategoryImage(output.cate))
            this.usedTime.text="평균 소요 시간: ${APIs.secToString(output.used.toInt())}"
            this.articleCount.text="관련 게시물: ${output.articleCount} 건"

            val adapter = AdapterImageList(itemView.context, output.imgUrl)

            imgList.adapter=adapter

            val linearLayoutManager= LinearLayoutManager(itemView.context,LinearLayout.HORIZONTAL,false)
            imgList.layoutManager=linearLayoutManager
            imgList.setHasFixedSize(true)
        }

    }

}