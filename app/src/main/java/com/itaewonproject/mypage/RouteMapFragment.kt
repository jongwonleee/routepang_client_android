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
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
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
import com.itaewonproject.rests.get.GetLocationConnector
import com.itaewonproject.maputils.MyLocationSetting.Companion.TAG
import com.itaewonproject.maputils.MyLocationSetting.Companion.con
import com.itaewonproject.maputils.MyLocationSetting.Companion.mGoogleApiClient
import com.itaewonproject.maputils.MyLocationSetting.Companion.mMoveMapByAPI
import com.itaewonproject.maputils.MyLocationSetting.Companion.mRequestingLocationUpdates
import com.itaewonproject.customviews.CustomMapView
import com.itaewonproject.maputils.CategoryIcon
import com.itaewonproject.maputils.MyLocationSetting
import com.itaewonproject.maputils.RouteUtils
import com.itaewonproject.model.receiver.Product
import com.itaewonproject.rests.put.PutRouteConnector
import java.util.*

class RouteMapFragment : Fragment(), AdapterMarkerList.OnStartDragListener,
    MyLocationSetting {


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

    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var imageCategory: ImageView
    private lateinit var usedTime: TextView
    private lateinit var layoutDetail : CardView

    private lateinit var adapter: AdapterMarkerList
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var callback: MarkerItemTouchHelperCallback

    var list: ArrayList<Product>
    var wishlist: ArrayList<Product>
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

                wishlist =(activity!!.application as Routepang).wishlist

                routeUtils.setList()
                routeUtils.addLine()
                routeUtils.setBoundary(list)


                adapter.list = list
                adapter.notifyDataSetChanged()
                Log.i("!!list size","${wishlist.size}")
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

        title = view.findViewById(R.id.textView_title) as TextView
        rating = view.findViewById(R.id.ratingBar_location) as RatingBar
        imageCategory = view.findViewById(R.id.image_category) as ImageView
        usedTime = view.findViewById(R.id.text_used_time) as TextView
        layoutDetail = view.findViewById(R.id.layout_location_info)

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
                    layoutDetail.visibility=View.GONE
                    textTitle.visibility = View.INVISIBLE
                    editTitle.visibility = View.VISIBLE
                    buttonEditor.visibility = View.GONE
                } else {
                    layoutMarkerList.visibility = View.GONE
                    layoutDetail.visibility=View.GONE
                    textTitle.visibility = View.VISIBLE
                    editTitle.visibility = View.INVISIBLE
                    buttonEditor.visibility = View.VISIBLE
                    routeUtils.setBoundary(list)
                    for( p in list){
                        val ret = PutRouteConnector().put(p.toSenderModel(),(parentFragment as RouteFragment).route.routeId)
                        if(ret.responceCode==200 || ret.responceCode==201)
                        {
                            Toast.makeText(con,"루트에 반영되었습니다.",Toast.LENGTH_LONG).show()
                        }else
                        {
                            Toast.makeText(con,"루트에 반영 실패했습니다.",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            routeUtils.editMode = editMode
        })
        textTitle.visibility = View.VISIBLE
        editTitle.visibility = View.INVISIBLE
        layoutMarkerList.visibility = View.GONE
        layoutDetail.visibility=View.GONE

        buttonEditor = view.findViewById(R.id.image_editor)
        buttonEditor.setOnClickListener({
            if (!editMode)(parentFragment as RouteFragment).toFragment(false)
        })
        buttonBack.setOnClickListener({
            (parentFragment as RouteFragment).toListFragment()
        })

        autoCompleteButton = view.findViewById(R.id.button_search) as CardView

        Places.initialize(activity!!.applicationContext, context!!.getString(R.string.google_key))
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

    fun showDetail(location:Location?){
        if(location==null){
            Log.i("details!!","NULL")
            layoutDetail.visibility =View.GONE
        }else
        {
            layoutDetail.visibility =View.VISIBLE
            this.title.text = location.name
            this.rating.rating = location.rating
            this.imageCategory.setImageResource(CategoryIcon.get(location.category!!))
            this.usedTime.text = "${APIs.secToString(location.usedTime.toInt())}"
        }
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
