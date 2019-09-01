package com.itaewonproject.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.player.ReviewConnector
import com.itaewonproject.R
import com.itaewonproject.adapter.AdapterArticleList


class ReviewFragment : Fragment() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var list:ArrayList<com.itaewonproject.model.receiver.Article>

    private fun setListViewOption(view:View){
        list = ReviewConnector().getByCustomerId(1)
        Log.i("list","${list[0].summary}")
        recyclerView = view.findViewById(R.id.review_RecyclerView) as RecyclerView
        val adapter = AdapterArticleList(view.context, list)

        adapter.setOnItemClickClickListener(object: AdapterArticleList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link.linkUrl))
                startActivity(intent)
            }
        })

        recyclerView.adapter=adapter

        val linearLayoutManager= LinearLayoutManager(view.context)
        recyclerView.layoutManager=linearLayoutManager
        recyclerView.setHasFixedSize(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var button = view.findViewById(R.id.button_create) as Button
        button.setOnClickListener({
            Log.i("!!","!!")
        })
        setListViewOption(view)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }
}
