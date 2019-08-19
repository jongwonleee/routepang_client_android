package com.itaewonproject.B_Mypage

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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.itaewonproject.APIs
import com.itaewonproject.R
import java.util.*

class RouteMapFragment : Fragment(),OnMapReadyCallback {

    private lateinit var mMap:GoogleMap
    private lateinit var mapView:MapView
    private lateinit var autoCompleteButton:ImageView
    private lateinit var buttonMap:ImageView

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
                    mMap.clear()
                    Log.i("!!",place.name)
                    mMap.addMarker(APIs.getMarkerOption(context!!,place.latLng!!))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
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
        mMap=googleMap

        mMap.setOnMapClickListener(GoogleMap.OnMapClickListener(){
            mMap.clear()
            //mMap.addMarker(it)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(it))

        })
        /*mMap.setOnMarkerClickListener { it: Marker? ->
            var intent = Intent(this, LocationListActivity::class.java)

            if (it != null) {
                intent.putExtra("LatLng",it.position);
                intent.putExtra("Altitude",mMap.cameraPosition.zoom)
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
