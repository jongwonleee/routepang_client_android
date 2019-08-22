package com.itaewonproject.B_Mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.A.LocationArticleActivity
import com.itaewonproject.APIs
import com.itaewonproject.B_Mypage.API.API2
import com.itaewonproject.R
import com.itaewonproject.RecyclerviewAdapter.AdapterLocationList
import com.itaewonproject.ServerResult.Location

class WishlistListFragment : Fragment() {
    private lateinit var recyclerView:RecyclerView
    lateinit var list:ArrayList<Location>

    private fun setListViewOption(view:View){
        Log.i("!!w","wish list shown")
        list = (parentFragment as WishlistFragment).list
        recyclerView = view.findViewById(R.id.wishlist_RecyclerView) as RecyclerView
        val adapter = AdapterLocationList(view.context, list)

        adapter.setOnItemClickClickListener(object: AdapterLocationList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(context, LocationArticleActivity::class.java)
                intent.putExtra("Location",adapter.output[position])
                startActivity(intent)
            }

        })

        recyclerView.adapter=adapter

        val linearLayoutManager= LinearLayoutManager(view.context)
        recyclerView.layoutManager= linearLayoutManager
        recyclerView.setHasFixedSize(true)

        //var dividerItemDeco = DividerItemDecoration(activity!!.applicationContext,linearLayoutManager.orientation)
        //recyclerView.addItemDecoration(dividerItemDeco)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist_list, container, false)
    }
}
