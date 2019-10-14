package com.medis.laboratcall

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.ListHasilPemeriksaanAdapter
import com.medis.laboratcall.Data.DataListHasilPemeriksaan
import kotlinx.android.synthetic.main.activity_rekap_hasil.*
import org.jetbrains.anko.indeterminateProgressDialog

class RekapHasil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekap_hasil)

        //Tombol Kemabali
        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.show()

        //Token Id User
        var token = getSharedPreferences("id", Context.MODE_PRIVATE)

        var list = ArrayList<DataListHasilPemeriksaan>()


        var url=Connection.url+"admin/page_pemeriksaan/get_pemeriksaan_ajax_byid_pasien/"+ token.getString("iduser","")
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                loading.dismiss()

                for (x in 0..response.length()-1)
                    list.add(
                        DataListHasilPemeriksaan(response.getJSONObject(x).getString("id_pemeriksaan"),
                            response.getJSONObject(x).getString("tanggal"),
                            response.getJSONObject(x).getString("nama"),
                            response.getJSONObject(x).getBoolean("cek_hasil"))
                    )


                var adp = ListHasilPemeriksaanAdapter(this,list)
                layoutListHasilPemeriksaan.layoutManager = LinearLayoutManager(this)
                layoutListHasilPemeriksaan.adapter = adp

            },
            Response.ErrorListener{ error ->
                loading.dismiss()
                dialogKonfirmasi("Tidak ada koneksi internet")
            })

        rq.add(jar)

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
}
