package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.maps.model.DirectionsStep
import com.itaewonproject.R

class AdapterStepList(val context: Context, var list: List<DirectionsStep>) : RecyclerView.Adapter<AdapterStepList.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_route_edit_step, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private var travelMode: ImageView
        private var description: TextView
        private var usedTime: TextView
        init {
            travelMode = itemView.findViewById(R.id.image_travelMode) as ImageView
            description = itemView.findViewById(R.id.text_description) as TextView
            usedTime = itemView.findViewById(R.id.text_used_time) as TextView
        }
        override fun bind(pos: Int) {
            // img.setImageBitmap(APIs.BitmapFromURL(url,300,300))
            // img.setImageBitmap(images[pos])
           /* Picasso.with(itemView.context)
                .load(images!![pos])
                .transform(RatioTransformation(300))
                .into(img)*/
            travelMode.setImageDrawable(null)
            description.text = list[pos].htmlInstructions
            usedTime.text = list[pos].duration.humanReadable
        }
    }
}
