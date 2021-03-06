package com.itaewonproject.mypage

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
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.itaewonproject.*
import com.itaewonproject.maputils.MyLocationSetting.Companion.TAG
import com.itaewonproject.maputils.MyLocationSetting.Companion.con
import com.itaewonproject.maputils.MyLocationSetting.Companion.mGoogleApiClient
import com.itaewonproject.maputils.MyLocationSetting.Companion.mMoveMapByAPI
import com.itaewonproject.maputils.MyLocationSetting.Companion.mRequestingLocationUpdates
import com.itaewonproject.maputils.MyLocationSetting.Companion.map
import com.itaewonproject.R
import com.itaewonproject.customviews.CustomMapView
import com.itaewonproject.customviews.RoundedImageView
import com.itaewonproject.maputils.CategoryIcon
import com.itaewonproject.maputils.MarkerUtils
import com.itaewonproject.maputils.MyLocationSetting
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.sender.Customer
import com.squareup.picasso.Picasso
import java.lang.NullPointerException
import java.util.*

class WishlistMapFragment: Fragment(), MyLocationSetting {

    private lateinit var mapView: CustomMapView
    private var list: ArrayList<Location> = arrayListOf()
    private lateinit var markerUtils: MarkerUtils
    private lateinit var autoCompleteButton: CardView
    private lateinit var title: TextView
    private lateinit var rating: RatingBar
    private lateinit var imageCategory: ImageView
    private lateinit var imagePreview: RoundedImageView
    private lateinit var usedTime: TextView
    private lateinit var layoutDetail : CardView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = view.findViewById(R.id.map) as CustomMapView
        title = view.findViewById(R.id.textView_title) as TextView
        rating = view.findViewById(R.id.ratingBar_location) as RatingBar
        imageCategory = view.findViewById(R.id.image_category) as ImageView
        imagePreview = view.findViewById(R.id.image_preview) as RoundedImageView
        usedTime = view.findViewById(R.id.text_used_time) as TextView
        layoutDetail = view.findViewById(R.id.layout_location_info)

        layoutDetail.visibility =View.GONE
        mapView.isInRoute=false
        autoCompleteButton = view.findViewById(R.id.button_search)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        con = context!!
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        Places.initialize(activity!!.applicationContext, context!!.getString(R.string.google_key))
        Places.createClient(context!!)
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY,
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        ).build(context!!)
        autoCompleteButton.setOnClickListener {
            startActivityForResult(intent, 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                if (place.latLng != null) {
                    map!!.clear()
                    map!!.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                    map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                }
            } else if (requestCode == RestrictionsManager.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
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
        mMoveMapByAPI=false
        setMapReady()

        markerUtils = MarkerUtils(map!!, this.context!!)
        markerUtils.isWishlist=true
        markerUtils.fragment=this

        map!!.clear()
        map!!.setOnMapClickListener {
            markerUtils.changeSelectedMarker(null)
            showDetail(null)
        }

    }
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        try{
            if (isVisibleToUser && isResumed) {
                list = (parentFragment as WishlistFragment).list
                for (l in list) {
                    markerUtils.addLocationMarker(l, false)
                }
                if (list.size> 0) {
                    map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(list[0].latlng(), 20f))
                }
            }
        }catch (e: NullPointerException){
            e.printStackTrace()
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
        if(mGoogleApiClient != null && !mGoogleApiClient!!.isConnected){

            Log.d(TAG, "onStart: mGoogleApiClient connect")
            mGoogleApiClient!!.connect()
        }

        super.onStart()
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
            this.usedTime.text = "평균 소요 시간: ${APIs.secToString(location.usedTime.toInt())}"
            if(location.images.size>0){
                imagePreview.visibility=View.VISIBLE
                Picasso.with(context)
                    .load(location.images[0])
                    .placeholder(R.drawable.box_empty_location)
                    .transform(RatioTransformation(300))
                    .into(imagePreview)
            }else
            {
                imagePreview.visibility=View.GONE
            }
        }
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
        mMoveMapByAPI=false
        if (mGoogleApiClient!!.isConnected) {

            Log.d(TAG, "onResume : call startLocationUpdates")
            if (!mRequestingLocationUpdates) startLocationUpdates()
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
