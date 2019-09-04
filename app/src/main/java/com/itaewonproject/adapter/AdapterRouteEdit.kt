package com.itaewonproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.APIs
import com.itaewonproject.mypage.EditItemTouchHelperCallback
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import java.util.*
import kotlin.collections.ArrayList

class AdapterRouteEdit(val context: Context, var edits:ArrayList<Location>, var startDragListener:OnStartDragListener) :
    RecyclerView.Adapter<BaseViewHolder>(),
    EditItemTouchHelperCallback.OnItemMoveListener {
    private lateinit var listener: onItemClickListener
    private var editMode=false
    override fun OnItemDrag(from: Int, to: Int,date:Date): Boolean {
        notifyDataSetChanged()
        return true
    }

    override fun OnItemMove(from: Int, to: Int): Boolean {
        Log.i("Moving","$from, $to")
        Collections.swap(edits,from,to)
        notifyItemMoved(from,to)
        //onBindViewHolder(get)
        return true
    }

    override fun OnItemSwipe(pos: Int): Boolean {
        Log.i("Removing","$pos, ${edits[pos].name}")
        edits.removeAt(pos)
        notifyItemRemoved(pos)
        return true
    }

    fun setEditMode(mode:Boolean){
        editMode=mode
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if(viewType==0) return EditViewHolder((LayoutInflater.from(context).inflate(R.layout.list_route_edit, parent, false)))
        else return AddViewHolder((LayoutInflater.from(context).inflate(R.layout.list_route_add, parent, false)))

    }

    override fun getItemCount(): Int {
        return edits.size+1
    }

    override fun getItemViewType(position: Int): Int {
        if(position== edits.size) return 1
        else return 0
    }

    interface onItemClickListener{
        fun onItemClick(v: View, position:Int)
    }

    interface OnStartDragListener{
        fun OnStartDrag(viewHolder:RecyclerView.ViewHolder)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener){
        this.listener=listener
    }

    inner class EditViewHolder(itemView: View) : BaseViewHolder(itemView){

        private var drag:ImageView
        private var title:TextView
        private var usedTime:TextView
        private var lineUp:View
        private var lineDown:View
        private var category:ImageView
        init{
            drag = itemView.findViewById(R.id.image_drag) as ImageView
            title = itemView.findViewById(R.id.text_title) as TextView
            usedTime=itemView.findViewById(R.id.text_used_time) as TextView
            lineDown=itemView.findViewById(R.id.line_down) as View
            lineUp=itemView.findViewById(R.id.line_up) as View
            category=itemView.findViewById(R.id.image_category) as ImageView


        }

        override fun bind(pos:Int){
            var edit = edits[pos]
            title.text = edit.name
            usedTime.text= "약 ${APIs.secToString(edit.used.toInt())}"
            if(pos==0) {
                lineUp.visibility=View.INVISIBLE
                lineDown.visibility=View.VISIBLE
            }else if(pos==edits.size-1){
                lineDown.visibility=View.INVISIBLE
                lineUp.visibility=View.VISIBLE
            }else{
                lineUp.visibility=View.VISIBLE
                lineDown.visibility=View.VISIBLE
            }
            category.setImageBitmap(APIs.getCategoryImage(edit.cate))
            if(editMode){
                drag.visibility=View.VISIBLE
            }else
            {
                drag.visibility=View.INVISIBLE
            }

            drag.setOnTouchListener({ view: View, motionEvent: MotionEvent ->
                if(motionEvent.actionMasked==MotionEvent.ACTION_DOWN){
                    startDragListener.OnStartDrag(this)
                }
                return@setOnTouchListener false
            })
        }
    }

    inner class AddViewHolder(itemView: View) :BaseViewHolder(itemView) {
        private var buttonAdd: ImageView
        init{
            buttonAdd = itemView.findViewById(R.id.button_add) as ImageView
        }
        override fun bind(pos:Int){
            if(editMode){
                buttonAdd.visibility=View.VISIBLE
            }else
            {
                buttonAdd.visibility=View.INVISIBLE
            }
            buttonAdd.setOnClickListener({

            })
        }
    }

}