package com.itaewonproject.maputils

import android.Manifest
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.location.LocationServices
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED
import java.lang.IllegalStateException


interface MyLocationSetting : OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener{

    companion object{
        val TAG = "myLocation:"
        var map: GoogleMap?=null

        var con: Context? = null

        var mGoogleApiClient: GoogleApiClient? = null

        val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002
        val UPDATE_INTERVAL_MS = 1000  // 1초
        val FASTEST_UPDATE_INTERVAL_MS = 500 // 0.5초

        var mRequestingLocationUpdates = false
        var mCurrentLocatiion: Location? = null
        var mMoveMapByUser = true
        var mMoveMapByAPI = true
        var currentPosition: LatLng? = null

        var locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS.toLong())
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS.toLong())
    }

    


    override fun onConnected(p0: Bundle?) {
        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    con!!,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions((con as Activity),
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                    );

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    map!!.setMyLocationEnabled(true);
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                map!!.setMyLocationEnabled(true);
            }
        }else
        {
           /* Log.d(TAG, "onConnected : call startLocationUpdates");
            startLocationUpdates();
            map!!.setMyLocationEnabled(true);*/
        }
    }

    override fun onConnectionSuspended(cause: Int) {
        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST){
            Log.e(TAG, "onConnectionSuspended(): Google Play services " + "connection lost.  Cause: network lost.");

        }
        else if (cause == CAUSE_SERVICE_DISCONNECTED){
            Log.e(TAG, "onConnectionSuspended():  Google Play services " + "connection lost.  Cause: service disconnected");
        }
    }

    fun setMapReady() {
        setDefaultLocation()

        map!!.getUiSettings().setMyLocationButtonEnabled(true)
        map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
        map!!.setOnMyLocationButtonClickListener(GoogleMap.OnMyLocationButtonClickListener {
            Log.d(TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화")
            mMoveMapByAPI = true
            true
        })

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d(TAG, "onConnectionFailed")
        //setDefaultLocation()
    }

    override fun onLocationChanged(location: Location?) {
        currentPosition = LatLng( location!!.getLatitude(), location!!.getLongitude());


        Log.d(TAG, "onLocationChanged : ");

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location);

        mCurrentLocatiion = location;
    }


    fun startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting")
            //showDialogForLocationServiceSetting()
        } else {

            if (ActivityCompat.checkSelfPermission(
                    con!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    con!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음")
                return
            }


            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates")
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                locationRequest,
                this
            )
            mRequestingLocationUpdates = true

            map!!.setMyLocationEnabled(true)

        }

    }


    fun stopLocationUpdates() {
        try{

            Log.d(TAG, "stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates")
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this)
            mRequestingLocationUpdates = false
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }
    }




    fun checkLocationServicesStatus(): Boolean {
        val locationManager = con!!.getSystemService(LOCATION_SERVICE) as LocationManager?

        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    fun setCurrentLocation(location: Location) {


        val currentLatLng = LatLng(location.latitude, location.longitude)
        if (mMoveMapByAPI) {

            Log.d(
                TAG, "setCurrentLocation :  map moveCamera "
                        + location.latitude + " " + location.longitude
            )
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            val cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
            map!!.moveCamera(cameraUpdate)
        }
        mMoveMapByAPI =false
    }


    fun setDefaultLocation() {

        mMoveMapByUser = true
        //디폴트 위치, Seoul
        val DEFAULT_LOCATION = LatLng(37.56, 126.97)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15f)
        map!!.moveCamera(cameraUpdate)

    }




}