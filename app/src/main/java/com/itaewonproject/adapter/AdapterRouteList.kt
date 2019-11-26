package com.itaewonproject.adapter

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.mainservice.MainActivity
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.model.receiver.RouteType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AdapterRouteList(val context: Context, folderArray: ArrayList<Route>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    var list: ArrayList<Route>
    var folder: RouteListManager
    val isChecked: HashSet<Route>
    init {
        list = ArrayList()
        isChecked = hashSetOf()
        folder = RouteListManager(folderArray)
    }

    fun resetList(list:ArrayList<Route>){

        this.list.clear()
        this.isChecked.clear()
        folder = RouteListManager(list)
        notifyDataSetChanged()
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
/*
    fun newRoute():Route{
        val route = Route("새 루트","미지정",0, ((context as MainActivity).application as Routepang).customer.customerId,System.currentTimeMillis())
        route.category=RouteType.ROUTE
        this.folder.folders.add(route)

        this.folder.setList()
        isChecked.clear()
        notifyDataSetChanged()
        return route
    }*/

    fun folderChecked() {
        val checkedList = isChecked.toMutableList()
        var folder: Route? = null
        for (l in checkedList) {
            if (l.category == RouteType.FOLDER) {
                folder = l
                checkedList.remove(l)
                break
            }
        }
        if (folder == null) {
            folder = Route("새로운 폴더", "바르셀로나", 0, arrayListOf(checkedList[0]),checkedList[0].customer,checkedList[0].regDate)
            checkedList.removeAt(0)
        }
        for (l in checkedList) {
            if (l.category ==  RouteType.FOLDER)
                folder.routes.addAll(l.routes)
            else folder.routes.add((l))
        }
        folder.calculateDate()
        this.folder.removeChecked()
        this.folder.folders.add(folder)
        this.folder.setList()
        isChecked.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) RouteViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_route_single,
                parent,
                false
            )
        )
        else FolderViewHolder(
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
        return if(list[position].category==RouteType.ROUTE) 0 else 1
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
        fun onItemLongClick(size: Int)
    }

    fun setOnItemClickClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class RouteViewHolder(itemView: View) : BaseViewHolder(itemView) {
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
            val route = list[pos]
            title.text = route.title
            location.text = route.boundary
            updated.text = route.getDateString()
            itemView.setOnClickListener {
                listener.onItemClick(itemView, pos)
            }

            if (isChecked.contains(list[pos])) viewChecked.visibility = View.VISIBLE
            else viewChecked.visibility = View.INVISIBLE

            itemView.setOnLongClickListener {
                if (isChecked.contains(list[pos])) {
                    isChecked.remove(list[pos])
                    viewChecked.visibility = View.INVISIBLE
                } else {
                    isChecked.add(list[pos])
                    viewChecked.visibility = View.VISIBLE
                }
                listener.onItemLongClick(isChecked.size)
                return@setOnLongClickListener true
            }
            if (folder.isOpened(pos)) {
                emptyView.visibility= if (folder.isOpened(pos+1)) View.GONE else View.VISIBLE
                emptyLeft.visibility=View.VISIBLE
            } else {
                emptyView.visibility=View.VISIBLE
                emptyLeft.visibility=View.GONE
            }
        }
    }

    inner class FolderViewHolder(itemView: View) : BaseViewHolder(itemView) {
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
            val route = list[pos]
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

            itemView.setOnLongClickListener {
                if (isChecked.contains(list[pos])) {
                    isChecked.remove(list[pos])
                    viewChecked.visibility = View.INVISIBLE
                } else {
                    isChecked.add(list[pos])
                    viewChecked.visibility = View.VISIBLE
                }
                listener.onItemLongClick(isChecked.size)
                return@setOnLongClickListener true
            }
            itemView.setOnClickListener {
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
            }
        }
    }

    inner class RouteListManager(var folders: ArrayList<Route>) {
        private var opened = ArrayList<Boolean>()
        private var indexRoute = HashMap<Int, Route>()
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
                /*f.parentId = 0
                if (f.routes==null) {
                    list.add(f)
                    indexRoute.put(list.size - 1, f)
                } else {
                    list.add(f)
                    indexRoute.put(list.size - 1, f)
                }
                if (f.opened) {
                    opened.add(true)
                    for (c in f.routes) {
                        c.parentId = list.indexOf(f)
                        list.add(c)
                        opened.add(true)
                        indexRoute.put(list.size - 1, f)
                    }
                } else
                    opened.add(false)*/
                list.add(f)
                indexRoute[list.size - 1] = f

                if(f.opened){
                    opened.add(true)
                    for (c in f.routes) {
                        list.add(c)
                        opened.add(true)
                        indexRoute[list.size - 1] = f
                    }
                }else
                {
                    opened.add(false)
                }
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

        private fun remove(pos: Int) {
            if (list[pos].parentId == 0) {
                folders.remove(indexRoute[pos]!!)
            } else {
                val par = list[list[pos].parentId]
                folders[folders.indexOf(par)].routes.remove(list[pos])
            }
        }
    }
}
