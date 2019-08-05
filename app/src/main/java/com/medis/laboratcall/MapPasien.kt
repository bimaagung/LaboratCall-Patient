package com.medis.laboratcall




import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import java.util.jar.Manifest

class MapPasien : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_pasien)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 123)




    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==123)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                var manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


                var listener = object: LocationListener {
                    override fun onLocationChanged(p0: Location) {
//                        val pospasien = LatLng( p0.latitude, p0.longitude)
//                        //mMap.addMarker(MarkerOptions().position(pospasien).title("Marker in Pasien"))
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pospasien,16f))
//                        mMap.setMyLocationEnabled(true)
//                        mMap.getUiSettings().setMyLocationButtonEnabled(true)

                        val latLngOrigin = LatLng(10.3181466, 123.9029382) // Ayala
                        val latLngDestination = LatLng(10.311795,123.915864) // SM City
                        mMap.addMarker(MarkerOptions().position(latLngOrigin).title("Ayala"))
                        mMap.addMarker(MarkerOptions().position(latLngDestination).title("SM City"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 14.5f))

                        val path: MutableList<List<LatLng>> = ArrayList()
                        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=10.3181466,123.9029382&destination=10.311795,123.915864&key=AIzaSyDs91q4g9okkhZweA86z-QbQIOp1Xdil6g"

                        var rq=Volley.newRequestQueue(this@MapPasien)
                        val sr = JsonObjectRequest(Request.Method.GET,urlDirections,null,Response.Listener {
                                response ->
                            //val jsonResponse = response
                            // Get routes
                            val routes = response.getJSONArray("routes")
                            val legs = routes.getJSONObject(0).getJSONArray("legs")
                            val steps = legs.getJSONObject(0).getJSONArray("steps")
                            for (i in 0 until steps.length()) {
                                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                                path.add(PolyUtil.decode(points))
                            }
                            for (i in 0 until path.size) {
                                mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
                            }
                        },
                            Response.ErrorListener{ error ->
                                //Toast.makeText(this, error.message,Toast.LENGTH_LONG).show()
                                Log.d("error query", error.message)
                            })

                        rq.add(sr)



                    }

                    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

                    }

                    override fun onProviderEnabled(p0: String?) {

                    }

                    override fun onProviderDisabled(p0: String?) {

                    }
                }

                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, listener)




            }
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
       // mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE



    }





}
