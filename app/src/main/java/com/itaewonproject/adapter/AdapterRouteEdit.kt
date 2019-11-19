package com.itaewonproject.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.maps.model.DirectionsStep
import com.itaewonproject.APIs
import com.itaewonproject.maputils.CategoryIcon
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.mypage.EditItemTouchHelperCallback
import com.itaewonproject.mypage.RouteEditFragment
import java.util.*
import kotlin.collections.ArrayList

class AdapterRouteEdit(val context: Context, val fragment: RouteEditFragment) :
    RecyclerView.Adapter<BaseViewHolder>(),
    EditItemTouchHelperCallback.OnItemMoveListener {
    private lateinit var listener: OnItemClickListener
    private var startDragListener: OnStartDragListener
    private var editMode = false
    var stepList: ArrayList<List<DirectionsStep>>
    var list: ArrayList<Location>
    init {
        startDragListener = fragment
        list = fragment.list
        stepList = arrayListOf()
        for (i in 0 until list.size) stepList.add(listOf())
    }
    override fun onItemDrag(): Boolean {
        resetSteplist()
        notifyDataSetChanged()
        return true
    }

    fun resetSteplist() {
        stepList = arrayListOf()
        for (i in 0 until list.size) stepList.add(listOf())
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        Log.i("!!!", "onViewAttached")
        super.onViewAttachedToWindow(holder)
    }

    override fun onItemMove(from: Int, to: Int): Boolean {
        Log.i("Moving", "$from, $to")
        Collections.swap(list, from, to)
        resetSteplist()
        notifyItemMoved(from, to)
        fragment.list = list
        // onBindViewHolder(get)
        return true
    }

    override fun onItemSwipe(pos: Int): Boolean {
        Log.i("Removing", "$pos, ${list[pos].name}")
        list.removeAt(pos)
        resetSteplist()
        notifyItemRemoved(pos)
        fragment.list = list
        return true
    }

    fun setEditMode(mode: Boolean) {
        editMode = mode
        resetSteplist()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return EditViewHolder((LayoutInflater.from(context).inflate(R.layout.list_route_edit, parent, false)))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    interface OnStartDragListener {
        fun OnStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    fun setOnItemClickClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class EditViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private var drag: ImageView
        private var title: TextView
        private var usedTime: TextView
        private var lineUp: View
        private var lineDown: View
        private var category: ImageView
        private var background: ConstraintLayout
        private var recyclerView: RecyclerView
        private var showSteps = false
        private var adapter: AdapterStepList
        init {
            drag = itemView.findViewById(R.id.image_drag) as ImageView
            title = itemView.findViewById(R.id.text_title) as TextView
            usedTime = itemView.findViewById(R.id.text_used_time) as TextView
            lineDown = itemView.findViewById(R.id.line_down) as View
            lineUp = itemView.findViewById(R.id.line_up) as View
            category = itemView.findViewById(R.id.image_category) as ImageView
            background = itemView.findViewById(R.id.background) as ConstraintLayout
            recyclerView = itemView.findViewById(R.id.recyclerView_way) as RecyclerView
            showSteps = false
            recyclerView.visibility = View.GONE
            adapter = AdapterStepList(context, listOf())
            val linearLayoutManager = LinearLayoutManager(context)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = adapter
        }

        override fun bind(pos: Int) {
            val edit = list[pos]
            title.text = edit.name
            usedTime.text = "약 ${APIs.secToString(edit.usedTime.toInt())}"
            if (pos == 0) {
                lineUp.visibility = View.INVISIBLE
                lineDown.visibility = View.VISIBLE
            } else if (pos == list.size - 1) {
                lineDown.visibility = View.INVISIBLE
                lineUp.visibility = View.VISIBLE
            } else {
                lineUp.visibility = View.VISIBLE
                lineDown.visibility = View.VISIBLE
            }
            category.setImageResource(CategoryIcon.get(edit.category!!))
            if (editMode) {
                drag.visibility = View.VISIBLE
                showSteps = false
                recyclerView.visibility = View.GONE
                background.isClickable = false
            } else {
                drag.visibility = View.INVISIBLE
                background.isClickable = true
            }

            drag.setOnTouchListener { view: View, motionEvent: MotionEvent ->
                if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                    startDragListener.OnStartDrag(this)
                }
                return@setOnTouchListener false
            }
            background.setOnClickListener {
                showSteps = !showSteps
                if (showSteps) {
                    adapter.list = stepList[pos]
                    adapter.notifyDataSetChanged()
                    recyclerView.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.GONE
                }
            }
        }
    }
}
