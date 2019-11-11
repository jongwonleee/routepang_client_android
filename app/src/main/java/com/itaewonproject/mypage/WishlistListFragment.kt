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
import com.itaewonproject.search.ArticleActivity

class WishlistListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var list: ArrayList<Location>

    private fun setListViewOption(view: View) {
        Log.i("!!w", "wish list shown")
        list = (parentFragment as WishlistFragment).list
        recyclerView = view.findViewById(R.id.wishlist_RecyclerView) as RecyclerView
        val adapter = AdapterLocationList(view.context, list)

        adapter.setOnItemClickClickListener(object : AdapterLocationList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("Location", adapter.output[position])
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.translate_in_from_left,0)
            }
        })

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        // var dividerItemDeco = DividerItemDecoration(activity!!.applicationContext,linearLayoutManager.orientation)
        // recyclerView.addItemDecoration(dividerItemDeco)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist_list, container, false)
    }
}
