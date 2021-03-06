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

class AdapterMarkerList(val context: Context, val fragment: RouteMapFragment)
    : RecyclerView.Adapter<AdapterMarkerList.ViewHolder>(),
    MarkerItemTouchHelperCallback.OnItemMoveListener{

    private var startDragListener: OnStartDragListener = fragment

    var list: ArrayList<Product> = arrayListOf()


    override fun onItemMove(from: Int, to: Int): Boolean {
        Collections.swap(list,from,to)
        notifyItemMoved(from,to)
        return true
    }

    override fun onItemDrag(): Boolean {
        notifyDataSetChanged()
        fragment.routeUtils.setWishList()
        return true
    }

    interface OnStartDragListener {
        fun OnStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

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

            itemView.setOnLongClickListener {
                startDragListener.OnStartDrag(this)
                return@setOnLongClickListener true
            }
        }
        override fun bind(pos: Int) {
            val product = list[pos]
            title.text = product.location.name
            lineLeft.visibility = if (pos != 0) View.VISIBLE else View.INVISIBLE
            lineRight.visibility = if (pos != list.size - 1) View.VISIBLE else View.INVISIBLE
            image.borderColor = CategoryIcon.getColor(product.location.category!!)
            if(product.location.images.isEmpty()){
                image.setImageResource(R.drawable.box_empty_location)
            }else
            {
                Picasso.with(itemView.context)
                    .load(product.location.images[0])
                    .transform(RatioTransformation(100))
                    .placeholder(R.drawable.box_empty_location)
                    .into(image)
            }

        }
    }
}
