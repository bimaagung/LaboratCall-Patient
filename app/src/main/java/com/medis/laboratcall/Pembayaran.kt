package com.medis.laboratcall

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_pembayaran.*
import android.content.Intent
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.PemeriksaanAdapter
import com.medis.laboratcall.Adapter.PesananAdapter
import com.medis.laboratcall.Data.DataPesanan


class Pembayaran : AppCompatActivity() {

    var listview: ListView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

//        var item = intent.getStringArrayExtra("item")
//        Log.v("HashMapTest", hashMap.toString())

        totalHarga.text = intent.getStringExtra("harga")

        listview = findViewById(R.id.listPesanan)

        var adapter = PesananAdapter(this, generateData())
        listview?.adapter = adapter

        adapter.notifyDataSetChanged()

        tb_konfirm.setOnClickListener{
            val hashMap = intent.getSerializableExtra("item") as HashMap<String, Int>
            Log.d("coba", hashMap.keys.toString())
            var url=Connection.url + "admin/page_pemeriksaan/pesan_pemeriksaan?item="+hashMap.keys.toString()+"&totalHarga="+intent.getStringExtra("harga")
//            var url=Connection.url + "admin/page_pemeriksaan/pesan_pemeriksaan/satu/dua"

            var rq= Volley.newRequestQueue(this)
            var sr= JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener{ response ->
                    var proses = response.getBoolean("proses")

                    if(proses == true)
                    {
                        Toast.makeText(this, "Proses berhasil",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Proses Gagal",Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener{ error ->
                    Toast.makeText(this, error.message,
                        Toast.LENGTH_LONG).show()
                })

            rq.add(sr)

        }
    }


    private fun generateData():ArrayList<DataPesanan>{
        var result = ArrayList<DataPesanan>()
        val hashMap = intent.getSerializableExtra("item") as HashMap<String, Int>

        for (i in hashMap){
            var user = DataPesanan(i.key,i.value.toString())
            result.add(user)
        }

        return result
    }
}
