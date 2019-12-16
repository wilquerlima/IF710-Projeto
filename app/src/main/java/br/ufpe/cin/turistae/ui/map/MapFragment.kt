package br.ufpe.cin.turistae.ui.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.ufpe.cin.turistae.MainActivity
import br.ufpe.cin.turistae.R
import br.ufpe.cin.turistae.data.*
import com.beust.klaxon.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_map.*
import leakcanary.AppWatcher
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.uiThread
import org.jetbrains.anko.yesButton
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    var mainActivity: MainActivity? = null
    private var myLocation: Location? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var list = arrayListOf<BaseItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        AppWatcher.objectWatcher.watch(this)
        mapFragment.getMapAsync(this)
        getLastLocation()
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val latLongB = LatLngBounds.Builder()
        val polyLine = PolylineOptions().color(Color.RED).width(5f)
        myLocation?.let { location ->
            val origin = LatLng(location.latitude, location.longitude)
            val dest = marker!!.position
            val url = getURL(origin, dest)
            doAsync {
                val result = URL(url).readText()
                uiThread {

                    clearMapAndRecreate()
                    val parser = Parser()
                    val stringBuilder: StringBuilder = StringBuilder(result)
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject

                    val routes = json.array<JsonObject>("routes")
                    val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>

                    val polypts =
                        points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!) }

                    polyLine.add(origin)
                    latLongB.include(origin)
                    for (point in polypts) {
                        polyLine.add(point)
                        latLongB.include(point)
                    }
                    polyLine.add(dest)
                    latLongB.include(dest)
                    // build bounds
                    val bounds = latLongB.build()
                    // add polyline to the map
                    mMap.addPolyline(polyLine)
                    // show map with route centered
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

                }
            }
        }
    }

    private fun clearMapAndRecreate() {
        mMap.clear()
        putMarkers()
    }

    private fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val key = "key=" + getString(R.string.google_maps_key)
        val params = "$origin&$dest&$key&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun createMarker(id: Int, loc: LatLng, title: String) {
        val marker =
            MarkerOptions()
                .position(LatLng(loc.latitude, loc.longitude))
                .title(title)
                .icon(
                    generateBitmapDescriptorFromRes(
                        mainActivity!!.applicationContext,
                        id
                    )
                )
        mMap.addMarker(marker)
    }

    private fun putMarkers() {
        getAllPlaces()
        list.forEach {
            when (it) {
                is ShoppingType -> {
                    createMarker(
                        R.drawable.ic_shopping_marker,
                        LatLng(it.latitude, it.longitude), it.nome
                    )
                }
                is TeatroType -> {
                    createMarker(
                        R.drawable.ic_theater_marker,
                        LatLng(it.latitude, it.longitude),
                        it.nome
                    )
                }
                is MercadoType -> {
                    createMarker(
                        R.drawable.ic_supermarket_marker,
                        LatLng(it.latitude, it.longitude),
                        it.nome
                    )
                }
                is PonteType -> {
                    createMarker(
                        R.drawable.ic_bridge_marker,
                        LatLng(it.latitude, it.longitude),
                        it.nome
                    )
                }
                is MuseuType -> {
                    createMarker(
                        R.drawable.ic_museum_marker,
                        LatLng(it.latitude, it.longitude),
                        it.nome
                    )
                }
                is FeiraLivreType -> {
                    createMarker(
                        R.drawable.ic_groceries_marker,
                        LatLng(it.latitude, it.longitude),
                        it.nome
                    )
                }
            }
        }
    }

    private fun getAllPlaces() {
        for (i in 0..5) {
            when (i) {
                0 -> {
                    val jsonArray =
                        Gson().fromJson(readJson(i), Array<MercadoType>::class.java).toList()
                    list.addAll(jsonArray)
                }
                1 -> {
                    val jsonArray =
                        Gson().fromJson(readJson(i), Array<PonteType>::class.java).toList()
                    list.addAll(jsonArray)
                }
                2 -> {
                    val jsonArray =
                        Gson().fromJson(readJson(i), Array<FeiraLivreType>::class.java).toList()
                    list.addAll(jsonArray)
                }
                3 -> {
                    val jsonArray =
                        Gson().fromJson(readJson(i), Array<MuseuType>::class.java).toList()
                    list.addAll(jsonArray)
                }
                4 -> {
                    val jsonArray =
                        Gson().fromJson(readJson(i), Array<TeatroType>::class.java).toList()
                    list.addAll(jsonArray)
                }
                5 -> {
                    val jsonArray =
                        Gson().fromJson(readJson(i), Array<ShoppingType>::class.java).toList()
                    list.addAll(jsonArray)
                }
            }
        }
    }

    private fun readJson(pos: Int): String? =
        activity?.applicationContext?.assets?.open("${getJsonFileName(pos)}.json")?.bufferedReader()
            .use { it?.readText() }

    private fun getJsonFileName(pos: Int): String {
        return when (pos) {
            0 -> "mercadospublicos"
            1 -> "pontesdorecife"
            2 -> "feiraslivre"
            3 -> "museus"
            4 -> "teatros"
            else -> "shopping"
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
                        putMarkers()
                        mMap.isMyLocationEnabled = true
                        mMap.uiSettings.isZoomControlsEnabled = true
                        mMap.setOnInfoWindowClickListener(this)
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

    private fun generateBitmapDescriptorFromRes(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }
}