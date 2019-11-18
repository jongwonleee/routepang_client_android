package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.RatioTransformation
import com.itaewonproject.customviews.RoundedImageView
import com.itaewonproject.model.sender.Location
import com.squareup.picasso.Picasso





class AdapterAddressList(val context: Context, val locs: ArrayList<Location>) : RecyclerView.Adapter<AdapterAddressList.ViewHolder>() {
    var checkedIndex=0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_address, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int{
        return locs.size+1
    }

    fun addAddress(loc:Location){
        locs.add(loc)
        checkedIndex = locs.indexOf(loc)
        notifyDataSetChanged()
    }

    fun getCheckedItem():Location?{
        if(locs.size==0) return null
        else return locs[checkedIndex]
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var mListener: OnItemClickListener? = null

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }


    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val checkBox:CheckBox
        private val name:TextView
        private val address:TextView
        private val layoutAdd:ConstraintLayout
        private val layoutAddress:ConstraintLayout
        init {
            checkBox =itemView.findViewById(R.id.checkBox)
            name =itemView.findViewById(R.id.text_name)
            address=itemView.findViewById(R.id.text_address)
            layoutAdd=itemView.findViewById(R.id.layout_add)
            layoutAddress=itemView.findViewById(R.id.layout_address)
        }
        override fun bind(pos: Int) {
            layoutAdd.setOnClickListener({
                mListener!!.onItemClick(pos)
            })
            layoutAddress.setOnClickListener({
                checkedIndex=pos
                notifyDataSetChanged()
            })
            if (pos == locs.size) {
                layoutAddress.visibility=View.INVISIBLE
                layoutAdd.visibility=View.VISIBLE
            } else
            {
                layoutAddress.visibility=View.VISIBLE
                layoutAdd.visibility=View.INVISIBLE
                name.text = locs[pos].name
                address.text=locs[pos].address
                if(pos==checkedIndex) checkBox.isChecked=true
                else checkBox.isChecked=false
            }
        }
    }
}
