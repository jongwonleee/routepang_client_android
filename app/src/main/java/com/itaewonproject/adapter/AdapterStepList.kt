package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.maps.model.DirectionsStep
import com.google.maps.model.TravelMode
import com.google.maps.model.VehicleType
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.itaewonproject.maputils.VehicleIcon
import com.squareup.picasso.Picasso
import java.lang.NullPointerException

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
        private val travelMode: ImageView
        private val description: TextView
        private val usedTime: TextView
        private val category:ImageView
        //private val title:TextView
        init {
            travelMode = itemView.findViewById(R.id.image_travelMode) as ImageView
            description = itemView.findViewById(R.id.text_description) as TextView
            usedTime = itemView.findViewById(R.id.text_used_time) as TextView
            category = itemView.findViewById(R.id.image_category)
            //title = itemView.findViewById(R.id.text_title)
        }
        override fun bind(pos: Int) {

            try{
                when(list[pos].travelMode){
                    TravelMode.WALKING,TravelMode.BICYCLING,TravelMode.UNKNOWN->{
                        category.setImageResource(VehicleIcon.get(VehicleType.OTHER))
                        travelMode.visibility=View.GONE
                    }
                    TravelMode.DRIVING->{
                        category.setImageResource(VehicleIcon.get(VehicleType.SHARE_TAXI))
                        travelMode.visibility=View.GONE
                    }
                    TravelMode.TRANSIT->{
                        val line =list[pos].transitDetails.line
                        category.setImageResource(VehicleIcon.get(line.vehicle.type))
                        Picasso.with(itemView.context)
                            .load(line.vehicle.localIcon)
                            .placeholder(R.drawable.box_empty_location)
                            .transform(RatioTransformation(16))
                            .into(travelMode)
                    }
                }
            }catch (e:NullPointerException){
                category.setImageResource(VehicleIcon.get(VehicleType.OTHER))
                travelMode.visibility=View.GONE
            }
            //title.visibility=View.GONE
            description.text = list[pos].htmlInstructions
            usedTime.text = list[pos].duration.humanReadable
        }
    }
}
