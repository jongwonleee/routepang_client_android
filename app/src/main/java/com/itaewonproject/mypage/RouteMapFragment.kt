package com.itaewonproject.mypage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.RestrictionsManager.RESULT_ERROR
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.itaewonproject.*
import com.itaewonproject.R
import com.itaewonproject.adapter.AdapterMarkerList
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.player.LocationConnector
import com.itaewonproject.MyLocationSetting.Companion.TAG
import com.itaewonproject.MyLocationSetting.Companion.con
import com.itaewonproject.MyLocationSetting.Companion.mGoogleApiClient
import com.itaewonproject.MyLocationSetting.Companion.mMoveMapByAPI
import com.itaewonproject.MyLocationSetting.Companion.mRequestingLocationUpdates
import com.itaewonproject.customviews.CustomMapView
import java.util.*

class RouteMapFragment : Fragment(), AdapterMarkerList.OnStartDragListener,MyLocationSetting {


    private lateinit var map: GoogleMap
    private lateinit var mapView: CustomMapView
    private lateinit var autoCompleteButton: CardView
    private lateinit var buttonEditor: ImageView
    private lateinit var buttonEdit: ImageView
    private lateinit var buttonBack: ImageView
    private lateinit var textTitle: TextView
    private lateinit var editTitle: TextView
    private lateinit var layoutMarkerList: CardView
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: AdapterMarkerList
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var callback: MarkerItemTouchHelperCallback

    var list: ArrayList<Location>
    var wishlist: ArrayList<Location>
    lateinit var routeUtils: RouteUtils

    private var editMode = false

    init {
        list = arrayListOf()
        wishlist = arrayListOf()
    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isResumed && isVisibleToUser) {
            try{
                val temp = JsonParser().listJsonParsing(LocationConnector().get(LatLng(41.374902, 2.170370), 14f), Location::class.java)
                for (i in 0..temp.size - 1) {
                    // XXX:
                    if (i <temp.size / 2) {
                        list.add(temp[i])
                    }
                    else {
                        wishlist.add(temp[i])
                    }
                }
                // map.moveCamera(CameraUpdateFactory.newLatLngZoom(list[0].latlng(),15f))

                routeUtils.setList()
                routeUtils.addLine()
                routeUtils.setBoundary(list)


                adapter.list = list
                adapter.notifyDataSetChanged()
                Log.i("!!list size","${adapter.itemCount}")
            }catch (e : UninitializedPropertyAccessException){
                e.printStackTrace()
            }

        }
    }

    override fun OnStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private fun setListViewOption(view: View){
        adapter = AdapterMarkerList(view.context,this)
        adapter.list=list
        callback = MarkerItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.map) as CustomMapView
        mapView.isInRoute=true
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        layoutMarkerList = view.findViewById(R.id.layout_markerList) as CardView
        recyclerView = view.findViewById(R.id.recyclerView_marker) as RecyclerView
        buttonEdit = view.findViewById(R.id.image_edit)
        buttonBack = view.findViewById(R.id.button_back)
        textTitle = view.findViewById(R.id.text_title)
        editTitle = view.findViewById(R.id.edit_title)

        con = context!!
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()


        buttonEdit.setOnClickListener({
            editMode = !editMode
            if (editMode) {
                    layoutMarkerList.visibility = View.VISIBLE
                    textTitle.visibility = View.INVISIBLE
                    editTitle.visibility = View.VISIBLE
                    buttonEditor.visibility = View.GONE
                } else {
                    layoutMarkerList.visibility = View.GONE
                    textTitle.visibility = View.VISIBLE
                    editTitle.visibility = View.INVISIBLE
                    buttonEditor.visibility = View.VISIBLE
                    routeUtils.setBoundary(list)
                }
            routeUtils.editMode = editMode
        })
        textTitle.visibility = View.VISIBLE
        editTitle.visibility = View.INVISIBLE
        layoutMarkerList.visibility = View.GONE

        buttonEditor = view.findViewById(R.id.image_editor)
        buttonEditor.setOnClickListener({
            if (!editMode)(parentFragment as RouteFragment).toFragment(false)
        })
        buttonBack.setOnClickListener({
            (parentFragment as RouteFragment).toListFragment()
        })

        autoCompleteButton = view.findViewById(R.id.button_search) as CardView

        Places.initialize(activity!!.applicationContext, context!!.getString(R.string.Web_key))
        Places.createClient(context!!)
        var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)).build(context!!)
       autoCompleteButton.setOnClickListener({
            startActivityForResult(intent, 1)
        })

        setListViewOption(view)
    }

    fun setAdapterList() {
        adapter.list = list
        adapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                var place = Autocomplete.getPlaceFromIntent(data!!)
                if (place.latLng != null) {
                    map.clear()
                    map.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    map.animateCamera(CameraUpdateFactory.zoomTo(15f))
                }
            } else if (requestCode == RESULT_ERROR) {
                var status = Autocomplete.getStatusFromIntent(data!!)
                Log.e(TAG, status.statusMessage)
            }
        }
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(this.activity)

        map = googleMap
        routeUtils = RouteUtils(map, this)
        mMoveMapByAPI=false
        setMapReady()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = view
        try {
            view = inflater.inflate(R.layout.fragment_route_map, container, false)
        } catch (e: InflateException) {
            e.printStackTrace()
        }
        return view
    }



    override fun onStart() {
        if(mGoogleApiClient != null && mGoogleApiClient!!.isConnected== false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient!!.connect();
        }

        super.onStart();
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume();
        mMoveMapByAPI=true
        if (mGoogleApiClient!!.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }

    }

    override fun onStop() {
        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient!!.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient!!.disconnect();
        }

        super.onStop();
    }
}
