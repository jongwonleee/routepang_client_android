package com.itaewonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Message
import java.sql.Timestamp

class AdapterMessagingList(val context: Context, var list: ArrayList<Message>) : RecyclerView.Adapter<AdapterMessagingList.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_message, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun setOnItemClickClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val imageOpposite:ImageView
        private val imageMe:ImageView
        val background:ConstraintLayout
        private val textMessage:TextView
        private val textTime:TextView

        init {
            imageMe = itemView.findViewById(R.id.image_me)
            imageOpposite = itemView.findViewById(R.id.image_opposite)
            background = itemView.findViewById(R.id.background)
            textMessage = itemView.findViewById(R.id.text_message)
            textTime=itemView.findViewById(R.id.text_time)
        }
        override fun bind(pos: Int) {
            val message = list[pos]
            textMessage.text = message.text
            textTime.text = Timestamp(message.regDate).toString()
            if(message.isMe){
                imageOpposite.visibility=View.GONE
                imageMe.visibility=View.VISIBLE
                background.setBackgroundResource(R.drawable.box_grey_right_top_not_curved)
            }else
            {
                imageOpposite.visibility=View.VISIBLE
                imageMe.visibility=View.GONE
                background.setBackgroundResource(R.drawable.box_grey_right_top_not_curved)
            }
        }
    }
}
