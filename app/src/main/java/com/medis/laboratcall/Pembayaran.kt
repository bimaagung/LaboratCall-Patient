package com.medis.laboratcall

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_pembayaran.*
import android.content.Intent
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.medis.laboratcall.Adapter.PesananAdapter
import com.medis.laboratcall.Data.DataPesanan


class Pembayaran : AppCompatActivity() {

    var listview: ListView? = null
    var metode_layanan:Boolean  = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

//        var item = intent.getStringArrayExtra("item")
//        Log.v("HashMapTest", hashMap.toString())

        var token  = getSharedPreferences("id", Context.MODE_PRIVATE)


        totalHarga.text = intent.getStringExtra("harga")

        listview = findViewById(R.id.listPesanan)

        var adapter = PesananAdapter(this, generateData())
        listview?.adapter = adapter

        adapter.notifyDataSetChanged()

        tb_konfirm.setOnClickListener{
            val hashMap = intent.getSerializableExtra("item") as HashMap<String, Int>
            Log.d("coba", hashMap.keys.toString())
            var url=Connection.url + "admin/page_pemeriksaan/pesan_pemeriksaan?item="+hashMap.keys.toString()+"&totalHarga="+intent.getStringExtra("harga")+"&id_pasien="+token.getString("iduser","")+"&klinik=2"
//            var url=Connection.url + "admin/page_pemeriksaan/pesan_pemeriksaan/satu/dua"

            var rq= Volley.newRequestQueue(this)
            var sr= JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener{ response ->
                    var proses = response.getBoolean("proses")

                    if(proses == true)
                    {

                        if(metode_layanan.equals(true)){
                            Toast.makeText(this, "Proses berhasil",Toast.LENGTH_SHORT).show()
                            var i =  Intent(this, MenungguHasil::class.java)
                            i.putExtra("layanan","offlocation")
                            startActivity(i)

                        }else if(metode_layanan.equals(false))
                        {
                            Toast.makeText(this, "Proses berhasil",Toast.LENGTH_SHORT).show()
                            var i =  Intent(this, MenungguHasil::class.java)
                            i.putExtra("layanan","onlocation")
                            startActivity(i)
                        }else{
                            Log.d("Metode layanan","Error")
                        }

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

        // ============================== Metode Layanan ==========================================

        var layanan_pasien:String = intent.getStringExtra("layanan")

        if(layanan_pasien.equals("offlocation"))
        {
            tx_trasnportasi.visibility = VISIBLE
            metode_layanan = true

        }else if(layanan_pasien.equals("onlocatioan"))
        {
            var id_klinik = 1
            kesediaan_analis(id_klinik)
            tx_trasnportasi.text = "1000"
            metode_layanan = false
        }else{
            Log.d("metode layanan","metode layanan error")
        }


//        ============================ Firebase ========================================

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference()
        reference.child("konfirm").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, "Database Error", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val childeren = dataSnapshot.children

                childeren.forEach{
                    Toast.makeText(applicationContext, it.getValue().toString(), Toast.LENGTH_LONG).show()
                }
            }

        })

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

    fun kesediaan_analis(id_klinik:Number)
    {
        //check kesediaan analis

        var url_check_analis=Connection.url + "admin/page_pemeriksaan/check_permission_analis_byid_api/1"

        var rq_check_analis = Volley.newRequestQueue(this)
        var sr_check_analis = JsonObjectRequest(Request.Method.GET,url_check_analis,null,
            Response.Listener{ response ->
                var kesediaan_analis = response.getBoolean("kesediaan_analis")

                if(kesediaan_analis == true)
                {
                    metode_layanan = true
                }else if(kesediaan_analis == false){
                    metode_layanan = false
                    Toast.makeText(this, "Mohon maaf, analis lagi tidak melayani OnCall",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Sistem kesediaan analis error",Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener{ error ->
                Toast.makeText(this, error.message,
                    Toast.LENGTH_LONG).show()
            })

        rq_check_analis.add(sr_check_analis)


        //======================
    }
}
