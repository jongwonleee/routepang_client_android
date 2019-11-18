package com.itaewonproject.mypage

import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.itaewonproject.APIs
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterRouteList
import com.itaewonproject.model.receiver.*
import com.itaewonproject.rests.get.GetRouteConnector
import java.sql.Timestamp

class RouteListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<Folder>
    private lateinit var adapter: AdapterRouteList
    private lateinit var buttonMakeFolder: ImageView
    private lateinit var buttonDelete: ImageView
    private lateinit var viewDivider:View
    private fun setListViewOption(view: View) {
        val ret = GetRouteConnector().get((activity!!.application as Routepang).customer.customerId)
        list = JsonParser().listJsonParsing(ret, Folder::class.java)
        adapter = AdapterRouteList(view.context, list)
        adapter.setOnItemClickClickListener(object : AdapterRouteList.onItemClickListener {
            override fun onItemLongClick(size: Int) {
                buttonMakeFolder.visibility = if (size> 1) View.VISIBLE else View.GONE
                buttonDelete.visibility = if(size>0) View.VISIBLE else View.GONE
                viewDivider.visibility = if (size> 1) View.VISIBLE else View.GONE
            }

            override fun onItemClick(v: View, position: Int) {
                (parentFragment as RouteFragment).toEditFragment(adapter.list[position] as Folder)
            }
        })

        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttonMakeFolder = view.findViewById(R.id.image_makeFolder) as ImageView
        buttonDelete = view.findViewById(R.id.image_delete) as ImageView
        recyclerView = view.findViewById(R.id.route_RecyclerView) as RecyclerView
        viewDivider = view.findViewById(R.id.view_divider) as View

        buttonMakeFolder.visibility = View.GONE
        buttonDelete.visibility = View.GONE
        buttonMakeFolder.setOnClickListener({
            adapter.folderChecked()
            buttonMakeFolder.visibility = View.GONE
            buttonDelete.visibility = View.GONE
        })
        buttonDelete.setOnClickListener({
            adapter.removeRoutes()
            buttonMakeFolder.visibility = View.GONE
            buttonDelete.visibility = View.GONE
        })
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = view
        try {
            view = inflater.inflate(R.layout.fragment_route_list, container, false)
        } catch (e: InflateException) {
            e.printStackTrace()
        }
        return view
    }
}
