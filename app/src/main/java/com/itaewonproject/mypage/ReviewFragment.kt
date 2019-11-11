package com.itaewonproject.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.adapter.AdapterArticleList
import com.itaewonproject.linkshare.LinkShareActivity
import com.itaewonproject.model.receiver.Article
import com.itaewonproject.player.ReviewConnector

class ReviewFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: ArrayList<com.itaewonproject.model.receiver.Article>

    private fun setListViewOption(view: View) {
        list = JsonParser().listJsonParsing(ReviewConnector().get((1).toLong()), Article::class.java)
        Log.i("list", "${list[0].summary}")
        recyclerView = view.findViewById(R.id.review_RecyclerView) as RecyclerView
        val adapter = AdapterArticleList(view.context, list)

        adapter.setOnItemClickClickListener(object : AdapterArticleList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(list[position].link.linkUrl))
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.translate_in_from_left,0)
            }
        })

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val button = view.findViewById(R.id.button_link_share) as CardView
        button.setOnClickListener({
            val intent = Intent(context, LinkShareActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.translate_in_from_left,0)
        })
        setListViewOption(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }
}
