package com.itaewonproject.search

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.appbar.AppBarLayout
import com.itaewonproject.*
import com.itaewonproject.R
import com.itaewonproject.adapter.AdapterLocationList
import com.itaewonproject.maputils.MarkerUtils
import com.itaewonproject.maputils.MyLocationSetting
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.rests.get.GetLocationConnector
import com.itaewonproject.maputils.MyLocationSetting.Companion.TAG
import com.itaewonproject.maputils.MyLocationSetting.Companion.con
import com.itaewonproject.maputils.MyLocationSetting.Companion.mGoogleApiClient
import com.itaewonproject.maputils.MyLocationSetting.Companion.mMoveMapByAPI
import com.itaewonproject.maputils.MyLocationSetting.Companion.mRequestingLocationUpdates
import com.itaewonproject.maputils.MyLocationSetting.Companion.map
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.pow

class LocationActivity : AppCompatActivity(),  Serializable,
    MyLocationSetting {



    private lateinit var latlng: LatLng
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonSort: LinearLayout
    private lateinit var textSort:TextView
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var mapFragment: MapFragment
    private lateinit var centerLatlng: LatLng
    private lateinit var markerUtils: MarkerUtils
    private var centerZoom: Float = 0f

    private var list = ArrayList<Location>()
    private val context = this
    private lateinit var adapter: AdapterLocationList
    private val arraySortname = listOf("별점 순","소요 시간 순","관련 게시글 순")
    private var sortIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build() as StrictMode.ThreadPolicy
        StrictMode.setThreadPolicy(policy)

        latlng = LatLng(37.576110, 126.976819)
        centerLatlng = latlng

        con = this
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)

        buttonSort = findViewById(R.id.button_sort)
        appBarLayout = findViewById(R.id.appBar_layout)
        textSort = findViewById(R.id.text_sort)



        Places.initialize(applicationContext, context.getString(R.string.google_key))
        Places.createClient(this)

        val autoCompleteSupportFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_location_search) as AutocompleteSupportFragment
        autoCompleteSupportFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG)
        )

        autoCompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                if (place.latLng != null) {
                    map!!.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    mapSearching()
                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
            }
        })
    }

    fun showArticleActivity(location: Location){
        val intent = Intent(context, ArticleActivity::class.java)
        intent.putExtra("Location", location)
        startActivity(intent)
    }

    private fun setListViewOption() {
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_locationList)
        for (l in list) {
            markerUtils.addLocationMarker(l, false)
        }

        adapter = AdapterLocationList(this, list)
        sortList()

        adapter.setOnItemClickClickListener(object : AdapterLocationList.OnItemClickListener {
            override fun onItemClick(position: Int) {
                showArticleActivity(adapter.output[position])
            }
        })

        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)


        buttonSort.setOnClickListener {sortList()
        }
    }

    private fun sortList(){
        when(sortIndex)
        {
            0->adapter.output.sortBy { it.rating}
            1->adapter.output.sortBy { it.usedTime}
            else->adapter.output.sortBy { it.articleCount}
        }

        textSort.text = arraySortname[sortIndex]
        adapter.notifyDataSetChanged()
        sortIndex = (sortIndex+1)%3
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        markerUtils = MarkerUtils(map!!, context)
        list = arrayListOf()
        map!!.cameraPosition.zoom
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            val mapPhoneRatio = 0.15654303392
            var meterPerPixel = mapPhoneRatio * 1.8 * Math.cos(centerLatlng.latitude * Math.PI / 180) / Math.pow((2).toDouble(), centerZoom.toDouble())
            map!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(centerLatlng.latitude - meterPerPixel*p1, centerLatlng.longitude)))
        })
        mMoveMapByAPI=true
        setMapReady()

        map!!.setOnMapClickListener {
            mapSearching()
            markerUtils.changeSelectedMarker(null)
        }

        setListViewOption()
    }

    private fun mapSearching() {
        list = JsonParser().listJsonParsing(GetLocationConnector().get(map!!.cameraPosition.target, map!!.cameraPosition.zoom), Location::class.java)
        adapter.output = list
        map!!.clear()
        for (l in list) {
            markerUtils.addLocationMarker(l, false)
        }
        adapter.notifyDataSetChanged()
        centerLatlng = map!!.cameraPosition.target
        centerZoom = map!!.cameraPosition.zoom
        appBarLayout.setExpanded(false)
    }

    override fun onStart() {
        if(mGoogleApiClient != null && !mGoogleApiClient!!.isConnected){

            Log.d(TAG, "onStart: mGoogleApiClient connect")
            mGoogleApiClient!!.connect()
        }

        super.onStart()
    }

    override fun onResume() {
        super.onResume()

        if (mGoogleApiClient!!.isConnected) {

            Log.d(TAG, "onResume : call startLocationUpdates")
            if (!mRequestingLocationUpdates) startLocationUpdates()
        }

    }

    override fun onStop() {
        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates")
            stopLocationUpdates()
        }

        if ( mGoogleApiClient!!.isConnected) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect")
            mGoogleApiClient!!.disconnect()
        }

        super.onStop()
    }
}
