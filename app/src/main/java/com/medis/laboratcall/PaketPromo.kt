package com.medis.laboratcall

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.DaftarPromoAdapter
import com.medis.laboratcall.Data.DataPromo
import kotlinx.android.synthetic.main.activity_paket_promo.*
import org.jetbrains.anko.indeterminateProgressDialog

class PaketPromo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paket_promo)

        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.show()

        //Tombol Kemabali
        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }


        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        var id_klinik = token.getString("klinik","")
        //Show Daftar Promo
        var list = ArrayList<DataPromo>()

        //show info klinik
        showInfoKlinik(id_klinik)


        var url=Connection.url+"admin/page_promo/getPromoByIdKlinik?id_klinik=$id_klinik"
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                loading.dismiss()

                for (x in 0..response.length()-1)
                    list.add(
                        DataPromo(response.getJSONObject(x).getString("id"),
                            response.getJSONObject(x).getString("nama"),
                            response.getJSONObject(x).getString("deskripsi_promo"),
                            response.getJSONObject(x).getString("tanggal_mulai"),
                            response.getJSONObject(x).getString("tanggal_akhir"),
                            response.getJSONObject(x).getString("harga"),
                            response.getJSONObject(x).getString("harga_promo"),
                            response.getJSONObject(x).getString("foto"))
                    )


                var adp = DaftarPromoAdapter(this,list)
                rv_daftar_promo.layoutManager = LinearLayoutManager(this)
                rv_daftar_promo.adapter = adp

            },
            Response.ErrorListener{ error ->
                println(error.message)
                loading.dismiss()
                dialogKonfirmasi("Koneksi internet error")

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

    fun showInfoKlinik(id_klinik:String){
        //Show Info Klinik
        var url_klinik = Connection.url+"admin/page_pemeriksaan/get_pemeriksaan_byid_api/"+id_klinik
        var rq_klinik = Volley.newRequestQueue(this)
        var sr_klink = JsonObjectRequest(Request.Method.GET, url_klinik,null,
            Response.Listener{ response ->
                tx_nama_klinik.text = "Klinik Lab"+response.getString("nama_laboratorium")
                tx_alamat_klinik.text = response.getString("alamat_laboratorium")

            },Response.ErrorListener {error ->
                dialogKonfirmasi("Tidak ada koneksi internet")
            })
        rq_klinik.add(sr_klink)
    }
}
