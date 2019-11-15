package com.itaewonproject.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Folder
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.model.receiver.RouteListBase
import com.itaewonproject.model.receiver.RouteType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdapterRouteList(val context: Context, folderArray: ArrayList<Folder>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var listener: onItemClickListener

    var list: ArrayList<RouteListBase>
    var folder: FolderListManager
    val isChecked: HashSet<RouteListBase>
    init {
        list = ArrayList()
        isChecked = hashSetOf()
        folder = FolderListManager(folderArray)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BaseViewHolder).bind(position)
    }

    fun removeRoutes() {
        this.folder.removeChecked()
        this.folder.setList()
        isChecked.clear()
        notifyDataSetChanged()
    }

    fun folderChecked() {
        val checkedList = isChecked.toMutableList()
        var folder: Folder? = null
        for (l in checkedList) {
            if (l.type == RouteType.FOLDER) {
                folder = l as Folder
                checkedList.remove(l)
                break
            }
        }
        if (folder == null) {
            folder = Folder("새로운 폴더", "바르셀로나", -1, arrayListOf(checkedList[0] as Route),checkedList[0].customerId,checkedList[0].regDate)
            checkedList.removeAt(0)
        }
        for (l in checkedList) {
            if (l.type ==  RouteType.FOLDER)
                folder.routes.addAll((l as Folder).routes)
            else folder.routes.add((l as Route))
        }
        folder.calculateDate()
        this.folder.removeChecked()
        this.folder.folders.add(folder)
        this.folder.setList()
        isChecked.clear()
        notifyDataSetChanged()
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
        return if(list[position].type==RouteType.ROUTE) 0 else 1
    }

    interface onItemClickListener {
        fun onItemClick(v: View, position: Int)
        fun onItemLongClick(size: Int)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    inner class SingleViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val title: TextView
        private val location: TextView
        private val updated: TextView
        private val emptyView: LinearLayout
        private val emptyLeft:View
        private val viewChecked: View
        init {
            title = itemView.findViewById(R.id.text_title) as TextView
            location = itemView.findViewById(R.id.text_location) as TextView
            updated = itemView.findViewById(R.id.text_updated) as TextView
            emptyView = itemView.findViewById(R.id.empty_view) as LinearLayout
            emptyLeft = itemView.findViewById(R.id.empty_left) as View
            viewChecked = itemView.findViewById(R.id.view_checked) as View
        }

        override fun bind(pos: Int) {
            var route = list[pos]
            title.text = route.title
            location.text = route.boundary
            updated.text = route.getDateString()
            itemView.setOnClickListener({
                listener.onItemClick(itemView, pos)
            })

            if (isChecked.contains(list[pos])) viewChecked.visibility = View.VISIBLE
            else viewChecked.visibility = View.INVISIBLE

            itemView.setOnLongClickListener({
                if (isChecked.contains(list[pos])) {
                    isChecked.remove(list[pos])
                    viewChecked.visibility = View.INVISIBLE
                } else {
                    isChecked.add(list[pos])
                    viewChecked.visibility = View.VISIBLE
                }
                listener.onItemLongClick(isChecked.size)
                return@setOnLongClickListener true
            })
            if (folder.isOpened(pos)) {
                emptyView.visibility= if (folder.isOpened(pos+1)) View.GONE else View.VISIBLE
                emptyLeft.visibility=View.VISIBLE
            } else {
                emptyView.visibility=View.VISIBLE
                emptyLeft.visibility=View.GONE
            }
        }
    }

    inner class GroupViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private val folderImage: ImageView
        private val textTitle: TextView
        private val editTitle: EditText
        private val location: TextView
        private val updated: TextView
        private val viewChecked: View
        private val emptyView: LinearLayout

        init {
            folderImage = itemView.findViewById(R.id.image_folder) as ImageView
            textTitle = itemView.findViewById(R.id.text_title) as TextView
            editTitle = itemView.findViewById(R.id.edit_title) as EditText
            location = itemView.findViewById(R.id.text_location) as TextView
            updated = itemView.findViewById(R.id.text_updated) as TextView
            viewChecked = itemView.findViewById(R.id.view_checked) as View
            emptyView = itemView.findViewById(R.id.empty_view) as LinearLayout
            editTitle.visibility = View.INVISIBLE
            textTitle.visibility = View.VISIBLE
        }

        override fun bind(pos: Int) {
            var route = list[pos]
            textTitle.text = route.title
            editTitle.text = Editable.Factory.getInstance().newEditable(route.title)
            location.text = route.boundary
            updated.text = route.getDateString()
            if (isChecked.contains(list[pos])) viewChecked.visibility = View.VISIBLE
            else viewChecked.visibility = View.INVISIBLE

            if (folder.isOpened(pos)) {
                emptyView.visibility=View.GONE
            } else {
                emptyView.visibility=View.VISIBLE
            }

            itemView.setOnLongClickListener({
                if (isChecked.contains(list[pos])) {
                    isChecked.remove(list[pos])
                    viewChecked.visibility = View.INVISIBLE
                } else {
                    isChecked.add(list[pos])
                    viewChecked.visibility = View.VISIBLE
                }
                listener.onItemLongClick(isChecked.size)
                return@setOnLongClickListener true
            })
            itemView.setOnClickListener({
                folder.open(pos)
                if (folder.isOpened(pos)) {
                    folderImage.setImageResource(R.drawable.ic_ico_folder_open)
                    editTitle.visibility = View.VISIBLE
                    textTitle.visibility = View.INVISIBLE
                } else {
                    folderImage.setImageResource(R.drawable.ic_ico_folder_closed_gray_s)
                    editTitle.visibility = View.INVISIBLE
                    textTitle.visibility = View.VISIBLE
                }
                notifyDataSetChanged()
            })
        }
    }

    inner class FolderListManager(var folders: ArrayList<Folder>) {
        var opened = ArrayList<Boolean>()
        var indexFolder = HashMap<Int, Folder>()
        init {
            setList()
        }

        fun isOpened(pos: Int): Boolean {
            return if(opened.size>pos) opened[pos] else false
        }

        fun setList() {
            list.clear()
            opened.clear()
            for (f in folders) {
                f.parentId = -1
                if (f.routes.size == 1) {
                    list.add(f.routes[0])
                    indexFolder.put(list.size - 1, f)
                } else {
                    list.add(f)
                    indexFolder.put(list.size - 1, f)
                }
                if (f.opened) {
                    opened.add(true)
                    for (c in f.routes) {
                        c.parentId = list.indexOf(f)
                        list.add(c)
                        opened.add(true)
                        indexFolder.put(list.size - 1, f)
                    }
                } else
                    opened.add(false)
            }
        }

        fun open(pos: Int) {
            val index = folders.indexOf(list[pos])
            folders[index].opened = !folders[index].opened
            setList()
        }

        fun removeChecked() {
            for (l in isChecked)
                if (list.contains(l)) remove(list.indexOf(l))
        }

        fun remove(pos: Int) {
            if (list[pos].parentId == -1) {
                folders.remove(indexFolder[pos]!!)
            } else {
                val par = list[list[pos].parentId] as Folder
                folders[folders.indexOf(par)].routes.remove(list[pos] as Route)
            }
        }
    }
}
