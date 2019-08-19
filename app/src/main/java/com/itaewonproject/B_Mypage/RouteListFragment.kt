package com.itaewonproject.B_Mypage

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
import com.itaewonproject.RecyclerviewAdapter.AdapterRouteList
import com.itaewonproject.ServerResult.Folder
import com.itaewonproject.ServerResult.Route

class RouteListFragment: Fragment(),AdapterRouteList.OnStartDragListener {
    override fun OnStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var list:ArrayList<Folder>
    private lateinit var itemTouchHelper:ItemTouchHelper
    private lateinit var par:RouteFragment
    private lateinit var adapter:AdapterRouteList

    /*fun setPar(par:RouteFragment){
        this.par=par
       // adapter.routeFragment=par
    }*/

    private fun setListViewOption(view:View){
        list = APIs.B_API1(1)
        adapter = AdapterRouteList(view.context,list,this)
        recyclerView = view.findViewById(com.itaewonproject.R.id.route_RecyclerView) as RecyclerView
        var callback = RoutesItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.setOnItemClickClickListener(object: AdapterRouteList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                Log.i("!@!","!!")
                (parentFragment as RouteFragment).toEditFragment(adapter.list[position] as Route)
            }

        })

        recyclerView.adapter=adapter

        val linearLayoutManager= LinearLayoutManager(view.context)
        recyclerView.layoutManager= linearLayoutManager as RecyclerView.LayoutManager?
        recyclerView.setHasFixedSize(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=view
        try{
            view=inflater.inflate(com.itaewonproject.R.layout.fragment_route_list, container, false)
        }catch (e:InflateException){
            e.printStackTrace()
        }

        return view
    }
}
