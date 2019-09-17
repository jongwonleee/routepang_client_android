package com.itaewonproject.mypage

import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.APIs
import com.itaewonproject.adapter.AdapterRouteList
import com.itaewonproject.model.receiver.Folder
import com.itaewonproject.model.receiver.Route

class RouteListFragment: Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var list:ArrayList<Folder>
    private lateinit var itemTouchHelper:ItemTouchHelper
    private lateinit var adapter:AdapterRouteList

    private fun setListViewOption(view:View){
        list = APIs.B_API1(1)
        adapter = AdapterRouteList(view.context,list)
        recyclerView = view.findViewById(com.itaewonproject.R.id.route_RecyclerView) as RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.setOnItemClickClickListener(object: AdapterRouteList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                (parentFragment as RouteFragment).toEditFragment(adapter.list[position] as Route)
            }
        })

        recyclerView.adapter=adapter

        val linearLayoutManager= LinearLayoutManager(view.context)
        recyclerView.layoutManager= linearLayoutManager
        recyclerView.setHasFixedSize(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view=view
        try{
            view=inflater.inflate(com.itaewonproject.R.layout.fragment_route_list, container, false)
        }catch (e:InflateException){
            e.printStackTrace()
        }
        return view
    }
}
