package com.itaewonproject.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Folder
import com.itaewonproject.model.receiver.RouteListBase
import java.util.*
import kotlin.collections.ArrayList

class AdapterRouteList(val context: Context, folderArray:ArrayList<Folder>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: onItemClickListener

    var list: ArrayList<RouteListBase>
    var folder: FolderListManager

    init {
        list = ArrayList()
        folder = FolderListManager(folderArray)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder).bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) SingleViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_route_single,
                parent,
                false
            )
        )
        else GroupViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_route_group,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    interface onItemClickListener {
        fun onItemClick(v: View, position: Int)
    }

    interface OnStartDragListener {
        fun OnStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    inner class SingleViewHolder(itemView: View) : BaseViewHolder(itemView) {

        private var drag: ImageView
        private var title: TextView
        private var location: TextView
        private var updated: TextView
        private var background: ConstraintLayout

        init {
            drag = itemView.findViewById(R.id.image_drag) as ImageView
            title = itemView.findViewById(R.id.text_title) as TextView
            location = itemView.findViewById(R.id.text_location) as TextView
            updated = itemView.findViewById(R.id.text_updated) as TextView
            background = itemView.findViewById(R.id.background) as ConstraintLayout
        }

        override fun bind(pos: Int) {
            var route = list[pos]
            title.text = route.title
            location.text = route.location
            updated.text = route.date
            itemView.setOnClickListener({
                listener.onItemClick(itemView, pos)
            })
            if (folder.isOpened(pos)) {
                background.background = Color.LTGRAY.toDrawable()
            } else {
                background.background = Color.WHITE.toDrawable()
            }

        }
    }

    inner class GroupViewHolder(itemView: View) : BaseViewHolder(itemView), OnStartDragListener {
        private var drag: ImageView
        private var folderImage: ImageView
        private var textTitle: TextView
        private var editTitle: EditText
        private var location: TextView
        private var updated: TextView
        private var background: ConstraintLayout
        private lateinit var itemTouchHelper: ItemTouchHelper


        override fun OnStartDrag(viewHolder: RecyclerView.ViewHolder) {
            itemTouchHelper.startDrag(viewHolder)
        }

        init {
            drag = itemView.findViewById(R.id.image_drag) as ImageView
            folderImage = itemView.findViewById(R.id.image_folder) as ImageView
            textTitle = itemView.findViewById(R.id.text_title) as TextView
            editTitle = itemView.findViewById(R.id.edit_title) as EditText
            location = itemView.findViewById(R.id.text_location) as TextView
            updated = itemView.findViewById(R.id.text_updated) as TextView
            background = itemView.findViewById(R.id.background) as ConstraintLayout

            editTitle.visibility = View.INVISIBLE
            textTitle.visibility = View.VISIBLE
            drag.visibility = View.VISIBLE
        }

        override fun bind(pos: Int) {
            var route = list[pos]
            textTitle.text = route.title
            editTitle.text = Editable.Factory.getInstance().newEditable(route.title)
            location.text = route.location
            updated.text = route.date

            if (folder.isOpened(pos)) {
                background.background = Color.LTGRAY.toDrawable()
            } else {
                background.background = Color.WHITE.toDrawable()
            }

            background.setOnClickListener({
                folder.open(pos)
                if (folder.isOpened(pos)) {
                    background.background = Color.LTGRAY.toDrawable()
                    folderImage.setImageResource(R.drawable.ic_folder_open_black_24dp)
                    editTitle.visibility = View.VISIBLE
                    textTitle.visibility = View.INVISIBLE
                    drag.visibility = View.INVISIBLE
                } else {
                    background.background = Color.WHITE.toDrawable()
                    folderImage.setImageResource(R.drawable.ic_folder_black_24dp)
                    editTitle.visibility = View.INVISIBLE
                    textTitle.visibility = View.VISIBLE
                    drag.visibility = View.VISIBLE
                }
                notifyDataSetChanged()
            })
        }
    }

    inner class FolderListManager(var folders: ArrayList<Folder>) : LinkedList<Folder>() {
        var opened = ArrayList<Boolean>()

        init {
            setList()
        }

        fun isOpened(pos: Int): Boolean {
            return opened[pos]
        }


        fun setList() {
            list.clear()
            opened.clear()
            for (f in folders) {
                f.parIndex = -1
                if (f.routes.size == 1) {
                    list.add(f.routes[0])
                } else {
                    list.add(f)
                }
                if (f.opened) {
                    opened.add(true)
                    for (c in f.routes) {
                        c.parIndex = list.indexOf(f)
                        list.add(c)
                        opened.add(true)
                    }
                } else
                    opened.add(false)
            }
        }

        fun open(pos: Int) {
            folders[pos].opened = !folders[pos].opened
            setList()
        }

        fun remove(pos: Int) {
            if (list[pos].parIndex == -1) {

            }
        }
    }
}