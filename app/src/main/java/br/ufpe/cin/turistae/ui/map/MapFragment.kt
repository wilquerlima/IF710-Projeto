package br.ufpe.cin.turistae.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import br.ufpe.cin.turistae.MainActivity
import br.ufpe.cin.turistae.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var mainActivity: MainActivity? = null
    private var myLocation: Location? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location = myLocation
        location?.let {
            val latlng = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(latlng).title("You are here"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.toFloat()))
        } ?: run {
            val default = LatLng(-8.063169, -34.871139)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(default, 15.toFloat()))
        }
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mainActivity!!.mFusedLocationClient.lastLocation.addOnCompleteListener(mainActivity!!) { task ->
                    val location: Location? = task.result
                    myLocation = location
                    location?.let {
                        val latlng = LatLng(location.latitude, location.longitude)
                        mMap.addMarker(MarkerOptions().position(latlng).title("You are here"))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.toFloat()))
                    } ?: run {
                        requestNewLocationData()
                        val default = LatLng(-8.063169, -34.871139)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(default, 15.toFloat()))
                    }
                }
            } else {
                alert("Por favor, ative seu GPS") {
                    yesButton {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                    noButton { }
                }.show()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity!!)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            myLocation = location
            val latlng = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(latlng).title("You are here"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15.toFloat()))
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                mainActivity!!.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                mainActivity!!.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            mainActivity!!,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            mainActivity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
    }
}