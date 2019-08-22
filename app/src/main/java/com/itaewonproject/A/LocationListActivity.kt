package com.itaewonproject.A

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.itaewonproject.A.API.API1
import com.itaewonproject.APIs
import com.itaewonproject.RecyclerviewAdapter.AdapterLocationList
import com.itaewonproject.ServerResult.Location
import com.itaewonproject.R
import java.io.Serializable

class LocationListActivity: AppCompatActivity(),OnMapReadyCallback,Serializable {

    private lateinit var mMap:GoogleMap
    private lateinit var latlng:LatLng
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonAdd:Button
    private lateinit var buttonSort:Button
    private var list=ArrayList<Location>()
    private var zoom=20f
    private val context = this
    private lateinit var adapter: AdapterLocationList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build() as StrictMode.ThreadPolicy
        StrictMode.setThreadPolicy(policy)

        latlng=intent.getParcelableExtra("LatLng")
        zoom = intent.getFloatExtra("Altitude",20f)
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)

        buttonAdd = findViewById(R.id.button_addLocationInfo) as Button
        buttonSort=findViewById(R.id.button_sortList) as Button

        buttonAdd.setOnClickListener({
            var intent = Intent(context, LinkShareActivity::class.java)
            startActivity(intent)
        })


    }

    private fun setListViewOption(){
        recyclerView = findViewById(R.id.recyclerView_locationList) as RecyclerView
        for(oll in list)
        {
            Log.i("!!",oll.latlng().toString())
            var markerOptions =MarkerOptions()
            markerOptions.position(oll.latlng())
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            mMap.addMarker(markerOptions)
        }
        Log.i("!!!","1")
        adapter = AdapterLocationList(this, list)

        adapter.setOnItemClickClickListener(object: AdapterLocationList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(context, LocationArticleActivity::class.java)
                //intent.putExtra("placeId",adapter.output[position].placeId)
                //intent.putExtra("rating",adapter.output[position].rating)
                //intent.putExtra("title",adapter.output[position].title)
                intent.putExtra("Location",adapter.output[position])
               // intent.putExtra("usedTime",56000)
                startActivity(intent)
            }

        })

        recyclerView.adapter=adapter

        val linearLayoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=linearLayoutManager
        recyclerView.setHasFixedSize(true)

        var dividerItemDeco = DividerItemDecoration(applicationContext,linearLayoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDeco)


        buttonSort.setOnClickListener {
            list.sortBy { it.rating }
            list.reverse()
            adapter.output=list
            adapter.notifyDataSetChanged()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap=googleMap
        list = API1().getByLatLng(latlng, zoom)

       var markerOptions = MarkerOptions()
        markerOptions.position(latlng)
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom))

        mMap.setLatLngBoundsForCameraTarget(LatLngBounds(latlng,latlng))
        mMap.uiSettings.setAllGesturesEnabled(false)
        setListViewOption()
    }


}
