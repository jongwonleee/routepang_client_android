package com.itaewonproject.B_Mypage

import android.os.Bundle
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.APIs
import com.itaewonproject.R
import com.itaewonproject.RecyclerviewAdapter.AdapterRouteEdit
import com.itaewonproject.ServerResult.Location
import com.itaewonproject.ServerResult.Route

class RouteEditFragment : Fragment(),AdapterRouteEdit.OnStartDragListener  {

    override fun OnStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var list:ArrayList<Location>
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var buttonMap:ImageView
    private lateinit var buttonBack:Button
    private lateinit var buttonEdit:ImageView
    private lateinit var textTitle:TextView
    private lateinit var editTitle:TextView
    private lateinit var adapter:AdapterRouteEdit
    var editMode=false

    private fun setListViewOption(view:View){
        list = APIs.API1(12.333333,123.333333,14f)

        adapter = AdapterRouteEdit(view.context, list,this)
        var callback = EditItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter=adapter

        val linearLayoutManager= LinearLayoutManager(view.context)
        recyclerView.layoutManager= linearLayoutManager!!
        recyclerView.setHasFixedSize(true)
        setEditMode()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonMap = view.findViewById(R.id.image_map)
        recyclerView = view.findViewById(R.id.edit_recyclerView) as RecyclerView
        buttonBack = view.findViewById(R.id.button_back)
        buttonEdit = view.findViewById(R.id.image_edit)
        textTitle = view.findViewById(R.id.text_title)
        editTitle = view.findViewById(R.id.edit_title)
        buttonMap.setOnClickListener({
            if(!editMode)(parentFragment as RouteFragment).toFragment(true)
        })
        buttonBack.setOnClickListener({
            if(!editMode) (parentFragment as RouteFragment).toFragment(false)
        })
        buttonEdit.setOnClickListener({
            editMode=!editMode
            setEditMode()
        })

        setListViewOption(view)
    }

    fun setEditMode(){
        if(editMode){
            textTitle.visibility=View.INVISIBLE
            editTitle.visibility=View.VISIBLE
        }else
        {
            textTitle.visibility=View.VISIBLE
            editTitle.visibility=View.INVISIBLE
        }
        adapter.setEditMode(editMode)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=view
        try{
            view=inflater.inflate(R.layout.fragment_route_edit, container, false)
        }catch (e: InflateException){
            e.printStackTrace()
        }
        return view
    }
}
