package com.example.gameitemtrade

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


class ChoicePlaceActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap : GoogleMap
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42
    lateinit var location_center: LatLng
    lateinit var btn_notHere : Button
    lateinit var btn_selectHere : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_place)
        val mapFragment =  supportFragmentManager.findFragmentById(R.id.Layout_map_choice) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btn_notHere = findViewById(R.id.btn_notHere) as Button
        btn_notHere.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("latitude", 0.0)
            resultIntent.putExtra("longitude",0.0)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
        btn_selectHere = findViewById(R.id.btn_selectHere) as Button
        btn_selectHere.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("latitude",location_center.latitude)
            resultIntent.putExtra("longitude",location_center.longitude)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        mMap.setOnCameraChangeListener(object: GoogleMap.OnCameraChangeListener{
            override fun onCameraChange(arg0: CameraPosition) {
                location_center = arg0.target
                Log.d("test","location_center: ${location_center}")
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val marker = LatLng(location.latitude, location.longitude)
                        location_center = marker
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}