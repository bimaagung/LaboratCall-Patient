package com.medis.laboratcall

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.DetailPromoAdapter
import com.medis.laboratcall.Data.DataDetailPromo
import com.medis.laboratcall.Data.DataItemPesanan
import kotlinx.android.synthetic.main.activity_detail_promo.*

class DetailPromo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_promo)

        //Tombol Kemabali
        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        var id_promo = intent.getStringExtra("id_promo")
        var nama_promo = intent.getStringExtra("nama_promo")
        var deskripsi_promo = intent.getStringExtra("deskripsi_promo")
        var tanggal_mulai = intent.getStringExtra("tanggal_mulai")
        var tanggal_akhir = intent.getStringExtra("tanggal_akhir")
        var harga = intent.getStringExtra("harga")
        var harga_promo = intent.getStringExtra("harga_promo")

        //Implementasi in xml
        tx_nama_promo.text = nama_promo
        tx_deskripsi_promo.text = deskripsi_promo
        tx_tanggal_mulai.text = tanggal_mulai
        tx_tanggal_akhir.text = tanggal_akhir
        tx_harga_normal.text = harga
        tx_harga_promo.text = harga_promo


        //Intent tb_lanjut_promo
        tb_lanjut_promo.setOnClickListener{
            var i = Intent(this, Layanan::class.java)
            i.putExtra("layanan_promo","promo")
            i.putExtra("id_promo",id_promo)
            i.putExtra("harga_promo",harga_promo)
            startActivity(i)
            finish()
        }

        //Show Item Promo
        var list = ArrayList<DataDetailPromo>()


        var url=Connection.url+"admin/page_promo/getItemPromoById?id_promo=$id_promo"
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                for (x in 0..response.length()-1) {
                    list.add(
                        DataDetailPromo(response.getJSONObject(x).getString("item_promo"))
                    )
                }

                var adp = DetailPromoAdapter(this,list)
                rv_item_promo.layoutManager = LinearLayoutManager(this)
                rv_item_promo.adapter = adp

            },
            Response.ErrorListener{ error ->
                println(error.message)
                dialogKonfirmasi("Tidak ada koneksi internet")

            })
        rq.add(jar)

        //Tombol kembali
        tb_back.setOnClickListener {
            finish()
        }
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
