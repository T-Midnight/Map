package com.example.tatiana.map.presentation.map


import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tatiana.map.App
import com.example.tatiana.map.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {
    override fun onConnected(p0: Bundle?) {
        createMapView()
    }

    private var mMap: GoogleMap? = null
    private val PERMISSION_ACCESS_LOCATION = 0
    private var mLocationPermissionGranted = false
    private var mLastKnownLocation: Location? = null
    var mCameraPosition: CameraPosition? = null
    var mDefaultLocation: LatLng = LatLng((-34).toDouble(), 151.toDouble())
    var mGoogleApiClient: GoogleApiClient? = null
    var listLocations = ArrayList<LatLng?>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSave.setOnClickListener {
            rememberMyLocation()
        }

        buttonDraw.setOnClickListener {
            drawLine()
        }

        mGoogleApiClient = GoogleApiClient.Builder(App.context)
            .enableAutoManage(
                this.activity!!,
                this
            )
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient!!.connect()
    }

    private fun createMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(context, "ON MAP READY", Toast.LENGTH_SHORT).show()
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(
                App.context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ACCESS_LOCATION
            )
        } else {
            mLocationPermissionGranted = true
            updateLocationUI()
            getDeviceLocation()
        }
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }

        if (mLocationPermissionGranted) {
            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
        } else {
            mMap!!.isMyLocationEnabled = false
            mMap!!.uiSettings.isMyLocationButtonEnabled = false
            mLastKnownLocation = null
        }
    }

    private fun getDeviceLocation() {
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient)
        }
        val DEFAULT_ZOOM = 15
        when {
            mCameraPosition != null -> mMap!!.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    mCameraPosition
                )
            )
            mLastKnownLocation != null -> mMap!!.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mLastKnownLocation!!.latitude,
                        mLastKnownLocation!!.longitude
                    ), DEFAULT_ZOOM.toFloat()
                )
            )
            else -> {
                Log.d("Map", "Current location is null. Using defaults.")
                mMap!!.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        mDefaultLocation,
                        DEFAULT_ZOOM.toFloat()
                    )
                )
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
            }
        }
    }

    private fun rememberMyLocation() {
        getDeviceLocation()
        saveMyLocation()
    }

    private fun saveMyLocation() {
        var latLng = LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)
        mMap!!.addMarker(MarkerOptions().position(latLng).title("$latLng"))
        listLocations.add(latLng)
    }

    private fun drawLine() {
        mMap!!.addPolyline(
            PolylineOptions().addAll(listLocations)
                .width(4.toFloat())
                .color(Color.GREEN)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ACCESS_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mLocationPermissionGranted = true
                    updateLocationUI()
                    getDeviceLocation()
                } else {
                    Toast.makeText(App.context, "Your location disabled", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}
