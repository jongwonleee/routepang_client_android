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
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.itaewonproject.APIs
import com.itaewonproject.JsonParser
import com.itaewonproject.MarkerUtils
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.player.LocationConnector
import java.util.*

class RouteMapFragment : Fragment(),OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var mapView:MapView
    private lateinit var autoCompleteButton:ImageView
    private lateinit var buttonMap:ImageView
    private lateinit var markerUtils: MarkerUtils
    private lateinit var list:ArrayList<Location>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        buttonMap = view.findViewById(R.id.image_map)
        buttonMap.setOnClickListener({
            (parentFragment as RouteFragment).toFragment(false)
        })

        autoCompleteButton = view.findViewById(R.id.button_search) as ImageView

        Places.initialize(activity!!.applicationContext,"AIzaSyCQBy7WzSBK-kamsMKt6Yk1XpxirVKiW8A")
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
        markerUtils = MarkerUtils(map,context!!)
        list = JsonParser().listJsonParsing(LocationConnector().get(LatLng(41.374902, 2.170370),14f),Location::class.java)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(list[0].latlng(),15f))
        for(l in list){
            markerUtils.addEditMarker(l,false)
        }
        markerUtils.addLine(list)
        map.setOnMapClickListener(GoogleMap.OnMapClickListener(){
            map.clear()
            map.animateCamera(CameraUpdateFactory.newLatLng(it))
            markerUtils.changeSelectedMarker(null,true)

        })
        /*map.setOnMarkerClickListener { it: Marker? ->
            var intent = Intent(this, LocationActivity::class.java)

            if (it != null) {
                intent.putExtra("LatLng",it.position);
                intent.putExtra("Altitude",map.cameraPosition.zoom)
            }
            startActivity(intent)

            return@setOnMarkerClickListener false
        }*/

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
