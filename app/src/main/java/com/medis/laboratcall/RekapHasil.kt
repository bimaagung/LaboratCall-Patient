package com.medis.laboratcall

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.ListHasilPemeriksaanAdapter
import com.medis.laboratcall.Data.DataListHasilPemeriksaan
import kotlinx.android.synthetic.main.activity_rekap_hasil.*

class RekapHasil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekap_hasil)

        //Token Id User
        var token = getSharedPreferences("id", Context.MODE_PRIVATE)

        var list = ArrayList<DataListHasilPemeriksaan>()


        var url=Connection.url+"admin/page_pemeriksaan/get_pemeriksaan_ajax_byid_pasien/"+ token.getString("iduser","")
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                for (x in 0..response.length()-1)
                    list.add(
                        DataListHasilPemeriksaan(response.getJSONObject(x).getString("id_pemeriksaan"),
                            response.getJSONObject(x).getString("tanggal"))
                    )


                var adp = ListHasilPemeriksaanAdapter(this,list)
                layoutListHasilPemeriksaan.layoutManager = LinearLayoutManager(this)
                layoutListHasilPemeriksaan.adapter = adp

            },
            Response.ErrorListener{ error ->
                Toast.makeText(this, error.message,
                    Toast.LENGTH_LONG).show()
            })
        rq.add(jar)
    }
}
