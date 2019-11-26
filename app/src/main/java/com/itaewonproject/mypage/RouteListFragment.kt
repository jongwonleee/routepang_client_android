package com.itaewonproject.mypage

import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterRouteList
import com.itaewonproject.mainservice.MainActivity
import com.itaewonproject.model.receiver.*
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.get.GetRouteListConnector
import com.itaewonproject.rests.post.PostRouteConnector
import com.itaewonproject.rests.post.PostRouteListConnector

class RouteListFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<Route>
    private lateinit var adapter: AdapterRouteList
    private lateinit var buttonMakeRoute: ImageView
    private lateinit var buttonDelete: ImageView
    private lateinit var viewDivider:View
    private lateinit var buttonNewRoute:FloatingActionButton
    lateinit var customer:Customer

    private fun setListViewOption(view: View) {
        val ret = GetRouteListConnector().get(customer.customerId)
        list = JsonParser().listJsonParsing(ret, Route::class.java)
        adapter = AdapterRouteList(view.context, list)
        adapter.setOnItemClickClickListener(object : AdapterRouteList.OnItemClickListener {
            override fun onItemLongClick(size: Int) {
                if((activity!!.application as Routepang).customer.customerId == customer.customerId){
                    buttonMakeRoute.visibility = if (size> 1) View.VISIBLE else View.GONE
                    buttonDelete.visibility = if(size>0) View.VISIBLE else View.GONE
                    viewDivider.visibility = if (size> 1) View.VISIBLE else View.GONE
                }
            }

            override fun onItemClick(v: View, position: Int) {
                (parentFragment as RouteFragment).toEditFragment(adapter.list[position])
            }
        })

        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonMakeRoute = view.findViewById(R.id.image_makeFolder) as ImageView
        buttonDelete = view.findViewById(R.id.image_delete) as ImageView
        recyclerView = view.findViewById(R.id.route_RecyclerView) as RecyclerView
        viewDivider = view.findViewById(R.id.view_divider) as View
        buttonNewRoute = view.findViewById(R.id.button_new_route)
        buttonMakeRoute.visibility = View.GONE
        buttonDelete.visibility = View.GONE
        buttonMakeRoute.setOnClickListener {
            adapter.folderChecked()
            buttonMakeRoute.visibility = View.GONE
            buttonDelete.visibility = View.GONE
        }
        buttonDelete.setOnClickListener {
            adapter.removeRoutes()
            buttonMakeRoute.visibility = View.GONE
            buttonDelete.visibility = View.GONE
        }
        buttonNewRoute.setOnClickListener {
            var route = Route("새 루트","미지정",0, ((context as MainActivity).application as Routepang).customer,System.currentTimeMillis())
            val ret = PostRouteConnector().post(customer.customerId,route)
            if(ret.responceCode==200){
                val ret = GetRouteListConnector().get(customer.customerId)
                list = JsonParser().listJsonParsing(ret, Route::class.java)
                adapter.resetList(list)
            }
        }
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.customer = arguments!!.getSerializable("customer")as Customer

        var view = view
        try {
            view = inflater.inflate(R.layout.fragment_route_list, container, false)
        } catch (e: InflateException) {
            e.printStackTrace()
        }
        return view
    }
}
