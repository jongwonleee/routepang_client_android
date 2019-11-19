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
import com.itaewonproject.model.receiver.Messenger
import com.itaewonproject.model.sender.Customer

class AdapterMessengerList(val context: Context, var list: ArrayList<Messenger>) : RecyclerView.Adapter<AdapterMessengerList.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_messenger, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnItemClickListener {
        fun onItemClick(v: View, opposite: Customer)
    }

    fun setOnItemClickClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val imageProfile:ImageView
        private val textMessage:TextView
        private val textName:TextView
        val background:ConstraintLayout
        init {
            imageProfile = itemView.findViewById(R.id.image_profile)
            textMessage = itemView.findViewById(R.id.text_message)
            textName=itemView.findViewById(R.id.text_name)
            background =itemView.findViewById(R.id.background)
        }
        override fun bind(pos: Int) {
            val messenger = list[pos]
            textMessage.text = messenger.lastMessage.text
            textName.text= messenger.customer.reference
            background.setOnClickListener {
                listener.onItemClick(itemView,messenger.customer)
            }
        }
    }
}
