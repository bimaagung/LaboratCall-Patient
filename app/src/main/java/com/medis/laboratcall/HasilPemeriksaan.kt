package com.medis.laboratcall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.HasilPemeriksaanAdapter
import com.medis.laboratcall.Connection.Companion.url
import com.medis.laboratcall.Data.DataHasilPemeriksaan
import kotlinx.android.synthetic.main.activity_hasil_pemeriksaan.*







class HasilPemeriksaan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_pemeriksaan)

        var list = ArrayList<DataHasilPemeriksaan>()

        //Show Info Klinik
        var url_klinik = Connection.url+"admin/page_pemeriksaan/get_pemeriksaan_byid_api/"+Connection.IdDetailPemeriksaan
        var rq_klinik = Volley.newRequestQueue(this)
        var sr_klink = JsonObjectRequest(Request.Method.GET, url_klinik,null,
            Response.Listener{ response ->
                tx_nama_klinik.text = response.getString("nama_laboratorium")
                tx_alamat_klinik.text = response.getString("alamat_laboratorium")

            },Response.ErrorListener {error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            })
        rq_klinik.add(sr_klink)


        //Show Hasil Pemeriksaan
        var url=Connection.url+"admin/page_pemeriksaan/hasil_pemeriksaan_pasien_api/"+Connection.IdDetailPemeriksaan
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET, url, null,
                   Response.Listener { response ->
                       for (x in 0..response.length()-1)
                           list.add(DataHasilPemeriksaan(response.getJSONObject(x).getString("item_pemeriksaan"),
                               response.getJSONObject(x).getString("hasil_pemeriksaan"),
                               response.getJSONObject(x).getString("keterangan")))

                       var adp = HasilPemeriksaanAdapter(this,list)
                       layoutHasilPemeriksaan.layoutManager = LinearLayoutManager(this)
                       layoutHasilPemeriksaan.adapter = adp
                   },
                    Response.ErrorListener {  })
        rq.add(jar)


    }
}
