package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.squareup.picasso.Picasso

class AdapterImageList(val context: Context, var images: ArrayList<String>?) : RecyclerView.Adapter<AdapterImageList.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (images != null) {
            return images!!.size
        } else {
            return 0
        }
    }

   /* fun setImage(images:ArrayList<Bitmap>){
        this.images=images
        notifyDataSetChanged()
    }*/
    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private var img: ImageView
        init {
            img = itemView.findViewById(R.id.imageView) as ImageView
        }
        override fun bind(pos: Int) {
            // img.setImageBitmap(APIs.BitmapFromURL(url,300,300))
            // img.setImageBitmap(images[pos])
            Picasso.with(itemView.context)
                .load(images!![pos])
                .transform(RatioTransformation(300))
                .into(img)
        }
    }
}
