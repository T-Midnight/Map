package com.example.tatiana.map.presentation.map


import android.content.pm.PackageManager
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
import com.example.tatiana.testapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_map, container, false)
//        val mapFragment = activity!!.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
//        Log.d("Map", "I'm null pointer method")
//        mapFragment?.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //map.getMapAsync(this)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        Log.d("Map", "I'm null pointer method")
        mapFragment.getMapAsync(this)
        //mapFragment?.getMapAsync(this)
    }

//    fun createMapView() {
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
//        Log.d("Map", "I'm null pointer method")
//        mapFragment?.getMapAsync(this)
//    }

    override fun onMapReady(googleMap: GoogleMap) {
        Toast.makeText(context, "ON MAP READY", Toast.LENGTH_SHORT).show()
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng((-34).toDouble(), 151.toDouble())
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        if (ContextCompat.checkSelfPermission(
                App.context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            ActivityCompat.requestPermissions(
                this.activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS
            )

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            // Permission has already been granted
            mMap.isMyLocationEnabled = true
        }

//        if (ContextCompat.checkSelfPermission(App.context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
//            PackageManager.PERMISSION_GRANTED &&
//            ContextCompat.checkSelfPermission(App.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
//            PackageManager.PERMISSION_GRANTED) {
//            mMap.isMyLocationEnabled = true
//            mMap.uiSettings.isMyLocationButtonEnabled = true
//        } else {
//            Toast.makeText(App.context, "You don't have permission", Toast.LENGTH_LONG).show()
//        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mMap.isMyLocationEnabled = true
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
