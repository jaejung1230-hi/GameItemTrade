package com.example.gameitemtrade.Main_Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.gameitemtrade.DetailItemInfoActivity
import com.example.gameitemtrade.Data.ItemInfomation
import com.example.gameitemtrade.R
import com.example.gameitemtrade.Tasks.GetItemTask
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.json.JSONArray
import kotlin.math.pow

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap : GoogleMap
    lateinit var marker_view :View
    lateinit var tv_marker : TextView
    lateinit var btn_searchLocation : Button
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var location_center: LatLng
    lateinit var userId: String
    lateinit var gameTitle: String
    lateinit var itemInfoList: ArrayList<ItemInfomation>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.Layout_map) as SupportMapFragment

        userId = arguments?.getString("userId").toString()
        gameTitle = arguments?.getString("gameTitle").toString()

        mapFragment.getMapAsync(this)
        btn_searchLocation = v.findViewById(R.id.btn_searchLocation) as Button
        btn_searchLocation.setOnClickListener {
            val dis = getBoundsWithoutSpacing()
            if(dis>= 0.12){
                Toast.makeText(requireContext(),"검색 범위가 너무 큽니다!!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val getItemTask = GetItemTask()
            val result = getItemTask.execute(location_center.latitude.toString(), location_center.longitude.toString(), dis.toString(),"Get_ItemList_Location").get()
            if (!result.equals("[]")) {
                val itemListArray = JSONArray(result)
                val itemInfoList: ArrayList<ItemInfomation> = arrayListOf<ItemInfomation>()
                for (i in 0..(itemListArray.length()-1)){
                    itemInfoList.add(ItemInfomation(itemListArray.getJSONObject(i)))
                }

                for(itemInfo in itemInfoList){
                    setCustomMarkerView(itemInfo)
                }
            }
        }
        return v
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()

        mMap.setOnCameraChangeListener(object: GoogleMap.OnCameraChangeListener{
            override fun onCameraChange(arg0: CameraPosition) {
                location_center = arg0.target
                Log.d("test","location_center: ${location_center}")
            }
        })

        mMap.setOnMapClickListener (object: GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng?) {
                Toast.makeText(requireContext(),"맵 클릭",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setCustomMarkerView(latlngdata: ItemInfomation): Marker? {
        var markerOptions = MarkerOptions()
        marker_view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_marker, null)
        tv_marker = marker_view.findViewById(R.id.tv_marker) as TextView

        markerOptions.position(LatLng(latlngdata.latitude!!.toDouble(), latlngdata.longitude!!.toDouble()))
        markerOptions.title(latlngdata.productId)
        tv_marker.setText(latlngdata.title)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(requireContext(), marker_view)))

        mMap.setOnMarkerClickListener(object: GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                var intent = Intent(requireContext(), DetailItemInfoActivity::class.java)
                lateinit var item_this : ItemInfomation
                for(itemInfo in itemInfoList){
                    if(itemInfo.productId.equals(marker.title)){item_this = itemInfo ;  }
                }
                intent.putExtra("item",item_this)
                requireContext().startActivity(intent)
                return true
            }
        })
        return mMap.addMarker(markerOptions)
    }

    private fun addMarker(latlngdata: ItemInfomation): Marker {
        var markerOptions = MarkerOptions()
        marker_view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_marker, null)
        tv_marker = marker_view.findViewById(R.id.tv_marker) as TextView

        markerOptions.position(LatLng(36.241615, 128.695587))
        tv_marker.setText(latlngdata.title)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(requireContext(), marker_view)))
        return mMap.addMarker(markerOptions)
    }

    private fun createDrawableFromView(context: Context, view: View): Bitmap {
        val displayMetrics = DisplayMetrics()
        (context as Activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        view.setLayoutParams(
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        )
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.buildDrawingCache()
        val bitmap: Bitmap = Bitmap.createBitmap(
                view.getMeasuredWidth(),
                view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun getBoundsWithoutSpacing(): Double {
        val exponent : Double = mMap.getCameraPosition().zoom -1.0 as Double
        val dis = 122.88/(2.0).pow(exponent)
        return dis
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireContext() as Activity) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val marker = LatLng(location.latitude, location.longitude)
                        location_center = marker
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))

                        Toast.makeText(requireContext(), location.latitude.toString()+location.longitude.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
        var locationManager: LocationManager = getContext()?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireContext() as Activity,
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