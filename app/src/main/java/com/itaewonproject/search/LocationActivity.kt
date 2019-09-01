package com.itaewonproject.search

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
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.appbar.AppBarLayout
import com.itaewonproject.player.LocationConnector
import com.itaewonproject.APIs
import com.itaewonproject.adapter.AdapterLocationList
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.R
import com.itaewonproject.linkshare.LinkShareActivity
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class LocationActivity: AppCompatActivity(),OnMapReadyCallback,Serializable {

    private lateinit var map:GoogleMap
    private lateinit var latlng:LatLng
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonAdd:Button
    private lateinit var buttonSort:Button
    private lateinit var appBarLayout:AppBarLayout
    private lateinit var mapFragment:MapFragment
    private lateinit var centerLatlng:LatLng
    private var centerZoom:Float=0f

    private var list = ArrayList<Location>()
    private var zoom = 20f
    private val context = this
    private lateinit var adapter: AdapterLocationList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build() as StrictMode.ThreadPolicy
        StrictMode.setThreadPolicy(policy)

        latlng = LatLng(37.576110, 126.976819)
        centerLatlng=latlng
        
        mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)

        buttonAdd = findViewById(R.id.button_addLocationInfo) as Button
        buttonSort=findViewById(R.id.button_sortList) as Button
        appBarLayout=findViewById(R.id.appBar_layout) as AppBarLayout

        Places.initialize(applicationContext,"AIzaSyCQBy7WzSBK-kamsMKt6Yk1XpxirVKiW8A")
        var placesClient = Places.createClient(this) as PlacesClient

        val autoCompleteSupportFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_location_search) as AutocompleteSupportFragment
        autoCompleteSupportFragment.setPlaceFields(
            Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG))

        autoCompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                if (place.latLng != null) {
                    map.clear()
                    Log.i("!!",place.id)
                    map.addMarker(APIs.getMarkerOption(context,place.latLng!!))
                    map.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    map.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    appBarLayout.setExpanded(false,true)


                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
            }
        })

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
            map.addMarker(markerOptions)
        }
        Log.i("!!!","1")

        adapter = AdapterLocationList(this, list)

        adapter.setOnItemClickClickListener(object: AdapterLocationList.onItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                var intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra("Location",adapter.output[position])
                startActivity(intent)
            }

        })

        recyclerView.adapter=adapter

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
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
        map=googleMap
        list = LocationConnector().getByLatLng(latlng, zoom)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom))

        map.setOnCameraIdleListener {
            //map.addMarker(APIs.getMarkerOption(con,map.cameraPosition.target))
            //Log.i("!!!")
        }

        map.setOnMapClickListener {
            list = LocationConnector().getByLatLng(map.cameraPosition.target, map.cameraPosition.zoom)
            adapter.output = list
            for(l in list){
                map.addMarker(APIs.getMarkerOption(context, l.latlng()))
            }
            adapter.notifyDataSetChanged()
            centerLatlng = map.cameraPosition.target
            centerZoom=map.cameraPosition.zoom
            appBarLayout.setExpanded(false)

        }
        appBarLayout.addOnOffsetChangedListener(object:AppBarLayout.OnOffsetChangedListener{
            override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
                var meterPerPixel = 0.15654303392 * 1.8 * Math.cos(centerLatlng.latitude * Math.PI / 180) / Math.pow((2).toDouble(), centerZoom.toDouble())
                map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(centerLatlng.latitude-meterPerPixel*p1, centerLatlng.longitude)))
                Log.i("!!!", "$p1")
            }

        })
        map.setOnMarkerClickListener {
            return@setOnMarkerClickListener false
        }
        setListViewOption()


    }


}
