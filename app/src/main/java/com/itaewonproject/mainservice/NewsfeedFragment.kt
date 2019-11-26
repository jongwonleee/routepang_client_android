package com.itaewonproject.mainservice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterFeedMarkerList
import com.itaewonproject.adapter.AdapterNewfeedList
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.receiver.Newsfeed
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.model.sender.Link
import com.itaewonproject.rests.get.GetNewsfeedConnector
import com.itaewonproject.search.ArticleActivity

class NewsfeedFragment : Fragment() {

    private lateinit var buttonSort:LinearLayout
    private lateinit var textSort:TextView
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter:AdapterNewfeedList
    private var list = arrayListOf<Newsfeed>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSort =view.findViewById(R.id.button_sort)
        textSort = view.findViewById(R.id.text_sort)
        recyclerView =view.findViewById(R.id.recyclerView)
        list = JsonParser().listJsonParsing(GetNewsfeedConnector().get((activity!!.application as Routepang).customer.customerId),Newsfeed::class.java)
        adapter = AdapterNewfeedList(context!!,list)
        adapter.setOnItemClickClickListener(object :AdapterNewfeedList.OnItemClickListener{
            override fun onReferenceClick(link:Link) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.linkUrl))
                startActivity(intent)
            }

            override fun onProfileClick(customer: Customer) {
                val intent = Intent(activity, UserInfoActivity::class.java)
                intent.putExtra("customer",customer)
                startActivity(intent)
            }

            override fun onLocationClick(location: Location) {
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("Location", location)
                startActivity(intent)
            }
        })

        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        linearLayoutManager.reverseLayout=true
        linearLayoutManager.stackFromEnd=true
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_newsfeed, container, false)
    }

}
