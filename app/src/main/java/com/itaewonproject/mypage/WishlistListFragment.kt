package com.itaewonproject.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.R
import com.itaewonproject.adapter.AdapterLocationList
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.search.ArticleActivity
import java.lang.NullPointerException

class WishlistListFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var list: ArrayList<Location>
    lateinit var adapter:AdapterLocationList
    private fun setListViewOption(view: View) {
        list = (parentFragment as WishlistFragment).list
        recyclerView = view.findViewById(R.id.wishlist_RecyclerView) as RecyclerView
        adapter = AdapterLocationList(view.context, list)
        adapter.setOnItemClickClickListener(object : AdapterLocationList.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("Location", adapter.output[position])
                startActivity(intent)
            }
        })

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        // var dividerItemDeco = DividerItemDecoration(activity!!.applicationContext,linearLayoutManager.orientation)
        // recyclerView.addItemDecoration(dividerItemDeco)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        try{

        }catch (e: NullPointerException){
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        Log.i("is wishlist list resumed?","yes!!!!")
        try{
            list = (parentFragment as WishlistFragment).list
            adapter.refreshList(list)
        }catch (e: NullPointerException){
            e.printStackTrace()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist_list, container, false)
    }
}
