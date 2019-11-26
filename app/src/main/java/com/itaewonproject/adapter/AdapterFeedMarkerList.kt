package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.maputils.CategoryIcon
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.itaewonproject.model.receiver.Product
import com.itaewonproject.mypage.MarkerItemTouchHelperCallback
import com.itaewonproject.mypage.RouteMapFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class AdapterFeedMarkerList(val context: Context,  val list: ArrayList<Product>)
    : RecyclerView.Adapter<AdapterFeedMarkerList.ViewHolder>(){


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_marker, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val image: CircleImageView
        private val lineLeft: View
        private val lineRight: View
        val title: TextView

        init {
            image = itemView.findViewById(R.id.image) as CircleImageView
            lineLeft = itemView.findViewById(R.id.line_left) as View
            lineRight = itemView.findViewById(R.id.line_right) as View
            title = itemView.findViewById(R.id.text_title) as TextView
        }
        override fun bind(pos: Int) {
            val product = list[pos]
            title.text = product.location.name
            lineLeft.visibility = if (pos != 0) View.VISIBLE else View.INVISIBLE
            lineRight.visibility = if (pos != list.size - 1) View.VISIBLE else View.INVISIBLE
            image.borderColor = CategoryIcon.getColor(product.location.category!!)
            Picasso.with(itemView.context)
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Oegyujanggak_at_Kanghwa.jpg/2560px-Oegyujanggak_at_Kanghwa.jpg")
                .transform(RatioTransformation(100))
                .placeholder(R.drawable.ic_logo_colored)
                .into(image)
        }
    }
}
