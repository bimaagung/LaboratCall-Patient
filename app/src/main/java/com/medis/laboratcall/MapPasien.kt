package com.medis.laboratcall



import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.activity_map_pasien.*

class MapPasien : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    var lat_analis:Double = 0.0
    var lng_analis:Double = 0.0
    var lat_pasien:Double = 0.0
    var lng_pasien:Double = 0.0
    var lat_origin:Double = 0.0
    var lng_origin:Double = 0.0
    var lat_destination:Double = 0.0
    var lng_destination:Double = 0.0
    lateinit var now: Marker


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_pasien)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tx_analis.text = ""
        tx_waktu.text = ""
        tx_jarak.text = ""

        //Firebase on analis or no

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference()
        val query = reference.child("permintaanOncall").child("2")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    lat_analis = dataSnapshot.child("lat_analis").getValue() as Double
                    lng_analis = dataSnapshot.child("long_analis").getValue() as Double

                    lat_pasien = dataSnapshot.child("lat_pasien").getValue() as Double
                    lng_pasien = dataSnapshot.child("long_pasien").getValue() as Double

                    lat_origin = dataSnapshot.child("lat_origin").getValue() as Double
                    lng_origin = dataSnapshot.child("lng_origin").getValue() as Double

                    lat_destination = dataSnapshot.child("lat_destination").getValue() as Double
                    lng_destination = dataSnapshot.child("lng_destination").getValue() as Double

                    getDirection(lat_origin,lng_origin,lat_destination, lng_destination, lat_analis, lng_analis)

                }else{
                    Log.d("message_debug","Gagal Firebase oncall")
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("message_debug","Error Firebase Konfirm Oncall Analis")
            }
        })

        //========================

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
                        val pospasien = LatLng( p0.latitude, p0.longitude)
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pospasien,16f))
                        mMap.setMyLocationEnabled(true)
                        mMap.getUiSettings().setMyLocationButtonEnabled(true)
                        mMap.getUiSettings().setZoomGesturesEnabled(true)

                            //Marker Analis
                        if(now != null){
                            now.remove()
                        }
                        var latLng = LatLng(lat_analis, lng_analis);
                        now = mMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.motor)))

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

    fun getDirection(lat_origin:Double, lng_origin:Double, lat_destination:Double, lng_destination:Double, lat_analis:Double, lng_analis:Double)
    {
        val latAnalis =  LatLng(lat_analis, lng_analis)
        val latLngOrigin = LatLng(lat_origin, lng_origin) //pasien
        val latLngDestination = LatLng(lat_destination, lng_destination) //analis
        mMap.addMarker(MarkerOptions().position(latLngOrigin).title("Pasien"))
        mMap.addMarker(MarkerOptions().position(latLngDestination).title("Analis"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latAnalis, 17.0f))


        val path: MutableList<List<LatLng>> = ArrayList()
        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin="+lat_origin+","+lng_origin+"&destination="+lat_destination+","+lng_destination+"&key=AIzaSyDs91q4g9okkhZweA86z-QbQIOp1Xdil6g"

        //Log.d("serverGoogleMap",urlDirections)

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
                mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.BLUE))
            }
        },
            Response.ErrorListener{ error ->
                //Toast.makeText(this, error.message,Toast.LENGTH_LONG).show()
                Log.d("error query", error.message)
            })

        rq.add(sr)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var latLng = LatLng(lat_analis, lng_analis);
        now = mMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.motor)))


    }

}
