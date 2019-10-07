package com.itaewonproject.mainservice

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.RestrictionsManager
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.itaewonproject.*
import com.itaewonproject.MyLocationSetting.Companion.TAG
import com.itaewonproject.MyLocationSetting.Companion.con
import com.itaewonproject.MyLocationSetting.Companion.mGoogleApiClient
import com.itaewonproject.MyLocationSetting.Companion.mMoveMapByAPI
import com.itaewonproject.MyLocationSetting.Companion.mRequestingLocationUpdates
import com.itaewonproject.MyLocationSetting.Companion.map
import com.itaewonproject.R
import com.itaewonproject.model.receiver.Location
import java.util.*

class WishlistMapFragment : Fragment(), MyLocationSetting {

    private lateinit var mapView: MapView
    val list: ArrayList<Location>
        get() = (parentFragment as WishlistFragment).list
    lateinit var markerUtils: MarkerUtils
    private lateinit var autoCompleteButton: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.map) as MapView
        autoCompleteButton = view.findViewById(R.id.button_search) as ImageView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        con = context!!
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        Places.initialize(activity!!.applicationContext, context!!.getString(R.string.Web_key))
        Places.createClient(context!!)
        var intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)).build(context!!)
        autoCompleteButton.setOnClickListener({
            startActivityForResult(intent, 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                var place = Autocomplete.getPlaceFromIntent(data!!)
                if (place.latLng != null) {
                    map!!.clear()
                    map!!.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                }
            } else if (requestCode == RestrictionsManager.RESULT_ERROR) {
                var status = Autocomplete.getStatusFromIntent(data!!)
                Log.e(ContentValues.TAG, status.statusMessage!!)
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
        mMoveMapByAPI=true
        setMapReady()

        markerUtils = MarkerUtils(map!!, context!!)
        map!!.clear()
        map!!.setOnMapClickListener {
            markerUtils.changeSelectedMarker(null)
        }
        ///FIXME list 받아오기 구현


        for (l in list) {
            markerUtils.addLocationMarker(l, false)
        }
        if (list.size> 0) {
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(list[0].latlng(), 20f))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = view
        try {
            view = inflater.inflate(R.layout.fragment_wishlist_map, container, false)
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


        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        /*if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }  */
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
