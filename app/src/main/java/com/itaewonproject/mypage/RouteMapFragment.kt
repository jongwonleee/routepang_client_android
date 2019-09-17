package com.itaewonproject.mypage

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
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
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.itaewonproject.*
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.player.LocationConnector
import java.util.*

class RouteMapFragment : Fragment(),OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var mapView:MapView
    private lateinit var autoCompleteButton:ImageView
    private lateinit var buttonEditList:ImageView
    private lateinit var buttonEdit:ImageView
    private lateinit var textTitle: TextView
    private lateinit var editTitle: TextView
    private lateinit var routeUtils: RouteUtils
    var list:ArrayList<Location>
    var wishlist:ArrayList<Location>
    private var editMode=false


    init{
        list = arrayListOf()
        wishlist= arrayListOf()
    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isResumed && isVisibleToUser){
            val temp = JsonParser().listJsonParsing(LocationConnector().get(LatLng(41.374902, 2.170370),14f),Location::class.java)
            for(i in 0 .. temp.size-1){
                if(i<temp.size/2)list.add(temp[i])
                else wishlist.add(temp[i])
            }
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(list[0].latlng(),15f))
            Log.i("!!!","isvisible now")
            routeUtils.setList()
            routeUtils.addLine()
            routeUtils.setBoundary(list)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        buttonEdit = view.findViewById(R.id.image_edit)
        textTitle = view.findViewById(R.id.text_title)
        editTitle = view.findViewById(R.id.edit_title)

        buttonEdit.setOnClickListener({
            editMode=!editMode
                if(editMode){
                    textTitle.visibility=View.INVISIBLE
                    editTitle.visibility=View.VISIBLE
                    buttonEditList.visibility=View.GONE
                }else
                {
                    textTitle.visibility=View.VISIBLE
                    editTitle.visibility=View.INVISIBLE
                    buttonEditList.visibility=View.VISIBLE
                    routeUtils.setBoundary(list)

                }
            routeUtils.editMode=editMode
        })
        textTitle.visibility=View.VISIBLE
        editTitle.visibility=View.INVISIBLE
        buttonEditList = view.findViewById(R.id.image_map)
        buttonEditList.setOnClickListener({
            if(!editMode)(parentFragment as RouteFragment).toFragment(false)
        })

        autoCompleteButton = view.findViewById(R.id.button_search) as ImageView

        Places.initialize(activity!!.applicationContext,context!!.getString(R.string.Web_key))
        var placesClient = Places.createClient(context!!) as PlacesClient
        var intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG)).build(context!!)
       autoCompleteButton.setOnClickListener({
            startActivityForResult(intent,1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1){
            if(resultCode==RESULT_OK)
            {
                var place  = Autocomplete.getPlaceFromIntent(data!!)
                if (place.latLng != null) {
                    map.clear()
                    map.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    map.animateCamera(CameraUpdateFactory.zoomTo(15f))
                }
            }
            else if(requestCode==RESULT_ERROR){
                var status = Autocomplete.getStatusFromIntent(data!!)
                Log.e(TAG,status.statusMessage)
            }
        }
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
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
        map=googleMap
        routeUtils = RouteUtils(map,this)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=view
        try {
            view = inflater.inflate(R.layout.fragment_route_map, container, false)
        }catch (e: InflateException){
            e.printStackTrace()
        }
        return view
    }
}
