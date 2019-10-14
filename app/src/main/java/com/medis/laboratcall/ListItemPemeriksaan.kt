package com.medis.laboratcall

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.ItemPemeriksaanAdapter
import com.medis.laboratcall.Data.DataItemPemeriksaan
import kotlinx.android.synthetic.main.activity_list_item_pemeriksaan.*
import kotlinx.android.synthetic.main.activity_rekap_hasil.tb_back
import org.jetbrains.anko.indeterminateProgressDialog

class ListItemPemeriksaan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item_pemeriksaan)

        //Tombol Kemabali
        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }


        //Proses Loading
        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.show()

        var list = ArrayList<DataItemPemeriksaan>()

        var url=Connection.url+"admin/page_pemeriksaan/get_list_item_pemeriksaan"
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                loading.dismiss()

                for (x in 0..response.length()-1)
                    list.add(
                        DataItemPemeriksaan(response.getJSONObject(x).getString("id"),
                            response.getJSONObject(x).getString("jenis_pemeriksaan"),
                            response.getJSONObject(x).getString("item_pemeriksaan"),
                            response.getJSONObject(x).getString("nl"),
                            response.getJSONObject(x).getString("np"),
                            response.getJSONObject(x).getString("keterangan"),
                            response.getJSONObject(x).getString("kategori"),
                            response.getJSONObject(x).getBoolean("type_data_float")
                        )
                    )


                var adp = ItemPemeriksaanAdapter(this,list)
                rv_daftar_pemeriksaan.layoutManager = LinearLayoutManager(this)
                rv_daftar_pemeriksaan.adapter = adp

            },
            Response.ErrorListener{ error ->
                loading.dismiss()
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
