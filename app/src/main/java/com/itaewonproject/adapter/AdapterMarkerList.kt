package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.customviews.CustomTextView
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.mypage.MarkerItemTouchHelperCallback
import com.itaewonproject.mypage.RouteMapFragment
import java.util.*
import kotlin.collections.ArrayList

class AdapterMarkerList(val context: Context, val fragment: RouteMapFragment)
    : RecyclerView.Adapter<AdapterMarkerList.ViewHolder>(),
    MarkerItemTouchHelperCallback.OnItemMoveListener{

    private var startDragListener: OnStartDragListener = fragment

    var list: ArrayList<Location> = arrayListOf()
        get() = fragment.list


    override fun OnItemMove(from: Int, to: Int): Boolean {
        Collections.swap(list,from,to)
        notifyItemMoved(from,to)
        return true
    }

    override fun OnItemDrag(): Boolean {
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
        val rating: ProgressBar
        val number: CustomTextView
        val image: ImageView
        val imageAdd: ImageView
        val articleCount: ProgressBar
        val lineLeft: View
        val lineRight: View
        val title: TextView
        var isSelected = false
        init {
            rating = itemView.findViewById(R.id.progressBar_rating) as ProgressBar
            number = itemView.findViewById(R.id.text) as CustomTextView
            image = itemView.findViewById(R.id.image) as ImageView
            imageAdd = itemView.findViewById(R.id.image_add) as ImageView
            articleCount = itemView.findViewById(R.id.progressBar_articleCount) as ProgressBar
            lineLeft = itemView.findViewById(R.id.line_left) as View
            lineRight = itemView.findViewById(R.id.line_right) as View
            title = itemView.findViewById(R.id.text_title) as TextView
            rating.max = 100
            articleCount.max = 20
            itemView.setOnLongClickListener({
                startDragListener.OnStartDrag(this)
                return@setOnLongClickListener true
            })
        }
        override fun bind(pos: Int) {
            val location = list[pos]
            rating.progress = (location.rating * 10).toInt()
            articleCount.progress = if (location.articleCount > 10) 10 else location.articleCount
            if (isSelected) {
                imageAdd.setImageResource(R.drawable.ic_clear_black_24dp)
                imageAdd.visibility = View.VISIBLE
                number.text = ""
            } else {
                imageAdd.visibility = View.INVISIBLE
                number.text = (pos + 1).toString()
            }
            title.text = location.name
            lineLeft.visibility = if (pos != 0) View.VISIBLE else View.INVISIBLE
            lineRight.visibility = if (pos != list.size - 1) View.VISIBLE else View.INVISIBLE
        }
    }
}
