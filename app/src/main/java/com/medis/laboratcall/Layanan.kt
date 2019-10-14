package com.medis.laboratcall

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationSettingsResult
import com.medis.laboratcall.Fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_layanan.*
import org.jetbrains.anko.indeterminateProgressDialog
import com.google.android.gms.location.LocationSettingsStatusCodes
import android.content.IntentSender
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback


class Layanan : AppCompatActivity() {

    var id_promo:String? = ""
    var harga_promo:String? = ""

    protected val REQUEST_CHECK_SETTINGS = 0x1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan)

        //Tombol Kemabali
        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        ly_lab_tutup.visibility = View.GONE
        v_pilihan_layanan.visibility = View.GONE

        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        var id_klinik = token.getString("klinik", " ")
        var layanan_promo:String? = intent.getStringExtra("layanan_promo")

        cekLaboratoriumKlinik(id_klinik)

       if(layanan_promo.equals("promo"))
       {
           id_promo = intent.getStringExtra("id_promo")
           harga_promo = intent.getStringExtra("harga_promo")

           tbDitempat.setOnClickListener{
               var i =  Intent(this, PilihPemeriksaan::class.java)
               i.putExtra("layanan", "offlocation")
               i.putExtra("layanan_promo","promo")
               i.putExtra("id_promo",id_promo)
               i.putExtra("harga_promo",harga_promo)

               startActivity(i)
               finish()
           }

           tbOncall.setOnClickListener{

               ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 123)


           }
       }else{
           tbDitempat.setOnClickListener{
               var i =  Intent(this, PilihPemeriksaan::class.java)
               i.putExtra("layanan", "offlocation")
               startActivity(i)
               finish()
           }

           tbOncall.setOnClickListener{
               ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 124)
               displayLocationSettingsRequest(this)

           }
       }

        tb_back.setOnClickListener {
            finish()
        }

    }

    fun cekLaboratoriumKlinik(id_klinik:String)
    {
        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.dismiss()

        var url = Connection.url + "admin/page_pemeriksaan/cek_laboratorium_klinik?id_klinik=$id_klinik"

        var rq = Volley.newRequestQueue(this)
        var sr = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                loading.dismiss()
                var konfirmasi = response.getBoolean("konfirmasi")
                var nama_laboratorium = response.getString("nama_laboratorium")

                if(konfirmasi == true) {
                    ly_lab_tutup.visibility = View.GONE
                    v_pilihan_layanan.visibility = View.VISIBLE

                } else {
                    ly_lab_tutup.visibility = View.VISIBLE
                    v_pilihan_layanan.visibility = View.GONE

                    tx_nama_laboratorium.text = "Laboratorium Klinik $nama_laboratorium Tutup"
                    tx_keterangan_open.text = "Silahkan menunggu buka laboratorium klinik $nama_laboratorium dan analis dapat melayani anda"
                }
            },
            Response.ErrorListener { error ->
                loading.dismiss()
                dialogKonfirmasi("Tidak ada koneksi internet")
            })

        rq.add(sr)
    }

    fun dialogKonfirmasi(message:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Keluar",
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })
            .setNegativeButton("Tunggu",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        // Create the AlertDialog object and return it
        val alert = builder.create()
        alert.show()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayLocationSettingsRequest(this)

                    var a =  Intent(this, PilihPemeriksaan::class.java)
                    a.putExtra("layanan", "onlocation")
                    a.putExtra("layanan_promo","promo")
                    a.putExtra("id_promo",id_promo)
                    a.putExtra("harga_promo",harga_promo)

                    startActivity(a)
                    finish()

            } else {
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else if(requestCode == 124) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayLocationSettingsRequest(this)

                    var a = Intent(this, PilihPemeriksaan::class.java)
                    a.putExtra("layanan", "onlocation")
                    startActivity(a)
                    finish()

            } else {
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun displayLocationSettingsRequest(context: Context) {


        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val TAG = Layanan::class.java!!.getSimpleName()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = (10000 / 2).toLong()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback(object : ResultCallback<LocationSettingsResult> {

             override fun onResult(result: LocationSettingsResult) {
                val status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> Log.i(

                        "message",
                        "All location settings are satisfied."
                    )
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            "message",
                            "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                        )

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(this@Layanan, REQUEST_CHECK_SETTINGS)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.i("message", "PendingIntent unable to execute request.")
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                        "message",
                        "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                    )
                }
            }
        })
    }


}
