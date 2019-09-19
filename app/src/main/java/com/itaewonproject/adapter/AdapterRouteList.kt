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
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Folder
import com.itaewonproject.model.receiver.Route
import com.itaewonproject.model.receiver.RouteListBase
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
            if (l.type == 1) {
                folder = l as Folder
                checkedList.remove(l)
                break
            }
        }
        if (folder == null) {
            folder = Folder("새로운 폴더", "바르셀로나", -1, arrayListOf(checkedList[0] as Route))
            checkedList.removeAt(0)
        }
        for (l in checkedList) {
            if (l.type == 1)
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
        return list[position].type
    }

    interface onItemClickListener {
        fun onItemClick(v: View, position: Int)
        fun onItemLongClick(size: Int)
    }

    fun setOnItemClickClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    inner class SingleViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private var title: TextView
        private var location: TextView
        private var updated: TextView
        private var background: ConstraintLayout
        private var viewChecked: View
        init {
            title = itemView.findViewById(R.id.text_title) as TextView
            location = itemView.findViewById(R.id.text_location) as TextView
            updated = itemView.findViewById(R.id.text_updated) as TextView
            background = itemView.findViewById(R.id.background) as ConstraintLayout
            viewChecked = itemView.findViewById(R.id.view_checked) as View
        }

        override fun bind(pos: Int) {
            var route = list[pos]
            title.text = route.title
            location.text = route.location
            updated.text = route.date
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
                background.background = Color.LTGRAY.toDrawable()
            } else {
                background.background = Color.WHITE.toDrawable()
            }
        }
    }

    inner class GroupViewHolder(itemView: View) : BaseViewHolder(itemView) {
        private var folderImage: ImageView
        private var textTitle: TextView
        private var editTitle: EditText
        private var location: TextView
        private var updated: TextView
        private var background: ConstraintLayout
        private var viewChecked: View

        init {
            folderImage = itemView.findViewById(R.id.image_folder) as ImageView
            textTitle = itemView.findViewById(R.id.text_title) as TextView
            editTitle = itemView.findViewById(R.id.edit_title) as EditText
            location = itemView.findViewById(R.id.text_location) as TextView
            updated = itemView.findViewById(R.id.text_updated) as TextView
            background = itemView.findViewById(R.id.background) as ConstraintLayout
            viewChecked = itemView.findViewById(R.id.view_checked) as View
            editTitle.visibility = View.INVISIBLE
            textTitle.visibility = View.VISIBLE
        }

        override fun bind(pos: Int) {
            var route = list[pos]
            textTitle.text = route.title
            editTitle.text = Editable.Factory.getInstance().newEditable(route.title)
            location.text = route.location
            updated.text = route.date
            if (isChecked.contains(list[pos])) viewChecked.visibility = View.VISIBLE
            else viewChecked.visibility = View.INVISIBLE

            if (folder.isOpened(pos)) {
                background.background = Color.LTGRAY.toDrawable()
            } else {
                background.background = Color.WHITE.toDrawable()
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
                    background.background = Color.LTGRAY.toDrawable()
                    folderImage.setImageResource(R.drawable.ic_folder_open_black_24dp)
                    editTitle.visibility = View.VISIBLE
                    textTitle.visibility = View.INVISIBLE
                } else {
                    background.background = Color.WHITE.toDrawable()
                    folderImage.setImageResource(R.drawable.ic_folder_black_24dp)
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
            return opened[pos]
        }

        fun setList() {
            list.clear()
            opened.clear()
            for (f in folders) {
                f.parIndex = -1
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
                        c.parIndex = list.indexOf(f)
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
            if (list[pos].parIndex == -1) {
                folders.remove(indexFolder[pos]!!)
            } else {
                val par = list[list[pos].parIndex] as Folder
                folders[folders.indexOf(par)].routes.remove(list[pos] as Route)
            }
        }
    }
}
