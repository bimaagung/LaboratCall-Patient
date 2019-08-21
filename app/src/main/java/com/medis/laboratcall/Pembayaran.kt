package com.medis.laboratcall

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_pembayaran.*
import android.content.Intent
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.FirebaseDatabase
import com.medis.laboratcall.Adapter.PesananAdapter
import com.medis.laboratcall.Data.DataNotifOncall
import com.medis.laboratcall.Data.DataPesanan
import java.io.Serializable
import java.text.DecimalFormat


class Pembayaran : AppCompatActivity() {

    var listview: ListView? = null

    var formatterHarga = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)


        //Get Token
        var token  = getSharedPreferences("id", Context.MODE_PRIVATE)
        var id_klinik =  token.getString("klinik","")
        var id_pasien =  token.getString("iduser","")

        var layanan_pasien:String = intent.getStringExtra("layanan")
        var item_pemeriksaan = intent.getSerializableExtra("item")
        var biaya_transportasi:Int = intent.getIntExtra("biaya_transportasi",0)
        var harga_pemeriksaan = intent.getStringExtra("harga")
        var id_pemeriksaan_oncall = intent.getStringExtra("id_pemeriksaan_oncall")
        Toast.makeText(this, biaya_transportasi.toString(),Toast.LENGTH_SHORT).show()

        tb_konfirm.visibility = View.VISIBLE
        progressBarPembayaran.visibility = View.INVISIBLE

        //Metode Layanan
        //metode_layanan(layanan_pasien,biaya_transportasi,harga_pemeriksaan.toInt())
        if(layanan_pasien == "offlocation")
        {
            tx_trasnportasi.visibility = View.INVISIBLE
            tx_harga_transportasi.visibility = View.INVISIBLE

            var format_harga = formatterHarga.format(harga_pemeriksaan.toInt())
            totalHarga.text = "Rp. "+format_harga.toString()

        }
        else if(layanan_pasien == "onlocation")
        {
            if(biaya_transportasi > 0)
            {
                tx_trasnportasi.visibility = View.VISIBLE
                tx_harga_transportasi.visibility = View.VISIBLE

                var total_harga_oncall:Int = biaya_transportasi.plus(harga_pemeriksaan.toInt())

                var format_harga_transportasi = formatterHarga.format(biaya_transportasi)
                tx_harga_transportasi.text = "Rp. "+format_harga_transportasi.toString()

                var format_harga = formatterHarga.format(total_harga_oncall)
                totalHarga.text = "Rp. "+format_harga.toString()

                //tx_harga_transportasi.text = biaya_transportasi.toString()
              //  totalHarga.text = total_harga_oncall.toString()



            }else if(biaya_transportasi == 0){
                tx_trasnportasi.visibility = View.INVISIBLE
                tx_harga_transportasi.visibility = View.INVISIBLE

                var format_harga = formatterHarga.format(harga_pemeriksaan.toInt())
                totalHarga.text = "Rp. "+format_harga.toString()
            }else{
                Toast.makeText(this, "metode layanan bagian logika transportasi error",Toast.LENGTH_SHORT).show()
            }

        }else{
            Log.d("metode layanan","metode layanan error")
            Toast.makeText(this, "metode layanan error",Toast.LENGTH_SHORT).show()
        }

        listview = findViewById(R.id.listPesanan)




        //adapter list pemesanan
        var adapter = PesananAdapter(this, generateData())
        listview?.adapter = adapter

        adapter.notifyDataSetChanged()

        //konfirmasi
        tb_konfirm.setOnClickListener{
           // var layanan_pasien:String = intent.getStringExtra("layanan")
            tb_konfirm.visibility = View.INVISIBLE
            progressBarPembayaran.visibility = View.VISIBLE

            val hashMap = intent.getSerializableExtra("item") as HashMap<String, Int>
            Log.d("coba", hashMap.keys.toString())
            var url=Connection.url + "admin/page_pemeriksaan/pesan_pemeriksaan?item="+hashMap.keys.toString()+"&totalHarga="+intent.getStringExtra("harga")+"&id_pasien="+token.getString("iduser","")+"&klinik="+id_klinik
//            var url=Connection.url + "admin/page_pemeriksaan/pesan_pemeriksaan/satu/dua"

            var rq= Volley.newRequestQueue(this)
            var sr= JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener{ response ->
                    var proses = response.getBoolean("proses")
                    var id_pemeriksaan = response.getString("id_pemeriksaan")

                    if(proses == true)
                    {
                        if(layanan_pasien == "offlocation")
                        {
                            var a= Intent(this,MenungguHasil::class.java)
                            startActivity(a)
                            finish()
                            //Toast.makeText(this, layanan_pasien,Toast.LENGTH_SHORT).show()
                        }else if(layanan_pasien == "onlocation")
                        {
                            if(biaya_transportasi != 0) {
                                after_oncall(id_pasien, id_pemeriksaan_oncall)
                                //Toast.makeText(this, layanan_pasien,Toast.LENGTH_SHORT).show()
                            }else if(biaya_transportasi == 0){
                                cek_analis(id_klinik, id_pasien, item_pemeriksaan, harga_pemeriksaan,id_pemeriksaan)
                            }else{
                                Log.d("Error", "Metode pembayaran")
                            }
                        }else{
                            Log.d("metode layanan","metode layanan error")
                           // Toast.makeText(this, "Error Layanan",Toast.LENGTH_SHORT).show()
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

    fun cek_analis(id_klinik:String, id_pasien:String, item_pemeriksaan:Serializable, harga_pemeriksaan:String, id_pemeriksaan:String)
    {
        var url_check_analis=Connection.url + "admin/page_pemeriksaan/check_permission_analis_byid_api?pasien="+
                             id_pasien+"&klinik="+id_klinik+"&id_pemeriksaan="+id_pemeriksaan

        var rq_check_analis = Volley.newRequestQueue(this)
        var sr_check_analis = JsonObjectRequest(Request.Method.GET,url_check_analis,null,
            Response.Listener{ response ->
                var aktif_analis = response.getBoolean("aktif_analis")
                var id_pemeriksaan_oncall = response.getString("id_oncall")

                if(aktif_analis == true)
                {

                    var a= Intent(this,MapPasien::class.java)
                    a.putExtra("id_pasien",id_pasien)
                    a.putExtra("item", item_pemeriksaan)
                    a.putExtra("harga", harga_pemeriksaan)
                    a.putExtra("id_pemeriksaan_oncall",id_pemeriksaan_oncall)
                    startActivity(a)
                    finish()

                }else if(aktif_analis == false){
                    var a= Intent(this,HomeActivity::class.java)
                    startActivity(a)
                    finish()
                    Toast.makeText(this, "Mohon maaf, analis lagi tidak melayani pemeriksaan",Toast.LENGTH_SHORT).show()
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
    }

    fun metode_layanan(layanan_pasien:String,biaya_transportasi:Int,harga_pemeriksaan:Int)
    {

    }

    fun after_oncall(id_pasien:String, id_pemeriksaan_oncall:String)
    {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference()
        reference.child("permintaanOncall").child(id_pasien).removeValue()

        var url_pemeriksaan_oncall = Connection.url+"admin/page_pemeriksaan/konfirmasi_menunggu_hasil_oncall/$id_pemeriksaan_oncall"
        var rq_pemeriksaan_oncall = Volley.newRequestQueue(this)
        var sr_pemeriksaan_oncall = JsonObjectRequest(Request.Method.GET, url_pemeriksaan_oncall,null,
            Response.Listener{ response ->
                var a= Intent(this,MenungguHasil::class.java)
                startActivity(a)
                finish()
            },Response.ErrorListener {error ->})
        rq_pemeriksaan_oncall.add(sr_pemeriksaan_oncall)

    }


}

