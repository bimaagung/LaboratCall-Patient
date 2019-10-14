package com.medis.laboratcall

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_pembayaran.*
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.FirebaseDatabase
import com.medis.laboratcall.Adapter.PesananAdapter
import com.medis.laboratcall.Data.DataItemPesanan
import com.medis.laboratcall.Data.DataNotifOncall
import com.medis.laboratcall.Data.DataPesanan
import kotlinx.android.synthetic.main.activity_map_pasien.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.indeterminateProgressDialog
import java.io.Serializable
import java.net.URLEncoder
import java.text.DecimalFormat


class Pembayaran : AppCompatActivity() {

    var listview: ListView? = null

    var formatterHarga = DecimalFormat("#,###")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        //Tombol Kemabali
        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.dismiss()

        //Get Token
        var token  = getSharedPreferences("id", Context.MODE_PRIVATE)
        var id_klinik =  token.getString("klinik","")
        var id_pasien =  token.getString("iduser","")

        var layanan_pasien:String = intent.getStringExtra("layanan")
        //var item_pemeriksaan = intent.getSerializableExtra("item")
        var item_pemeriksaan = DataItemPesanan.ItemPesanan

        var biaya_transportasi:Int = intent.getIntExtra("biaya_transportasi",0)
        var harga_pemeriksaan = intent.getStringExtra("harga")
        var id_pemeriksaan_oncall = intent.getStringExtra("id_pemeriksaan_oncall")
       // Toast.makeText(this, biaya_transportasi.toString(),Toast.LENGTH_SHORT).show()


        tb_konfirm.visibility = View.VISIBLE
        layout_dokter_pengirim.visibility = View.GONE

        //Metode Layanan
        //metode_layanan(layanan_pasien,biaya_transportasi,harga_pemeriksaan.toInt())
        if(layanan_pasien == "offlocation")
        {

//            tx_trasnportasi.visibility = View.INVISIBLE
//            tx_harga_transportasi.visibility = View.INVISIBLE
            layout_transportasi.visibility = View.GONE
            layout_dokter_pengirim.visibility = View.VISIBLE


            var format_harga = formatterHarga.format(harga_pemeriksaan.toInt())
            totalHarga.text = "Rp. "+format_harga.toString()

        }
        else if(layanan_pasien == "onlocation")
        {
            if(biaya_transportasi > 0)
            {

                tb_back.visibility = View.GONE
                tx_trasnportasi.visibility = View.VISIBLE
                tx_harga_transportasi.visibility = View.VISIBLE
                layout_dokter_pengirim.visibility = View.GONE

                var total_harga_oncall:Int = biaya_transportasi.plus(harga_pemeriksaan.toInt())

                var format_harga_transportasi = formatterHarga.format(biaya_transportasi)
                tx_harga_transportasi.text = "Rp. "+format_harga_transportasi.toString()

                var format_harga = formatterHarga.format(total_harga_oncall)
                totalHarga.text = "Rp. "+format_harga.toString()

                //tx_harga_transportasi.text = biaya_transportasi.toString()
              //  totalHarga.text = total_harga_oncall.toString()

            }else if(biaya_transportasi == 0){ //Layanan Offlocation
//                tx_trasnportasi.visibility = View.INVISIBLE
//                tx_harga_transportasi.visibility = View.INVISIBLE
                layout_transportasi.visibility = View.GONE
                layout_dokter_pengirim.visibility = View.VISIBLE

                var format_harga = formatterHarga.format(harga_pemeriksaan.toInt())
                totalHarga.text = "Rp. "+format_harga.toString()
            }else{
               // Toast.makeText(this, "metode layanan bagian logika transportasi error",Toast.LENGTH_SHORT).show()
                dialogKonfirmasi("metode layanan bagian logika transportasi error")
            }

        }else{
            Log.d("metode layanan","metode layanan error")
            dialogKonfirmasi("metode layanan error")
        }

        //List View Pesanan dan Harga
        listview = findViewById(R.id.listPesanan)

        //adapter list pemesanan
        var adapter = PesananAdapter(this, generateData())
        listview?.adapter = adapter

        adapter.notifyDataSetChanged()

        //Konfirmasi pemesanan offlocation
        tb_konfirm.setOnClickListener{
            tb_konfirm.visibility = View.INVISIBLE

            //Proses Menunggu
            loading.show()

            //Input Dokter Pengirim
            var dokter_pengirim = tx_dokter_pengirim.text.toString().replace(" ","+")

            //val hashMap = intent.getSerializableExtra("item") as HashMap<String,Int>
            val hashMap = DataItemPesanan.ItemPesanan

            var url = Connection.url + "admin/page_pemeriksaan/pesan_pemeriksaan?item="+hashMap.keys.toString().replace(" ","")+"&totalHarga="+intent.getStringExtra("harga")+"&id_pasien="+id_pasien+"&klinik="+id_klinik+"&dokter_pengirim="+dokter_pengirim

            //var url=Connection.url+"admin/page_pemeriksaan/pesan_pemeriksaan?item=[Hematologi,Hematokrit]&totalHarga=10000&id_pasien="+token.getString("iduser","")+"&klinik="+id_klinik
            var rq= Volley.newRequestQueue(this)
            var sr= JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener{ response ->
                    //clear array hashmap pemesanan
                    loading.dismiss()


                    var proses = response.getBoolean("proses")
                    var id_pemeriksaan = response.getString("id_pemeriksaan")

                    if(proses == true)
                    {
                        if(layanan_pasien == "offlocation")
                        {
                            var a= Intent(this,MenungguHasil::class.java)
                            startActivity(a)
                            finish()
                        }else if(layanan_pasien == "onlocation") //Layanan Onlocation
                        {
                            if(biaya_transportasi != 0) {
                                after_oncall(id_pasien, id_pemeriksaan_oncall) // Sesudah Onlocation
                            }else if(biaya_transportasi == 0){
                                cek_analis(id_klinik, id_pasien, item_pemeriksaan, harga_pemeriksaan,id_pemeriksaan) //Pra Onlocation
                            }else{
                                dialogKonfirmasi("Error Metode pembayaran")
                            }
                        }else{
                            dialogKonfirmasi("Error Metode pembayaran")
                        }


                    }else{
                       // DataItemPesanan.ItemPesanan.clear()
                        dialogKonfirmasi("Proses Gagal")
                    }
                },
                Response.ErrorListener{ error ->
                    //clear array hashmap pemesanan
                    loading.dismiss()
                  //  DataItemPesanan.ItemPesanan.clear()
                    dialogKonfirmasi("Koneksi internet error")
                })
            sr.setRetryPolicy(DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
            rq.add(sr)
        }

        tb_back.setOnClickListener {
            finish()
        }

    }

    //Get data pesanan
    private fun generateData():ArrayList<DataPesanan>{
        var result = ArrayList<DataPesanan>()
       // val hashMap = intent.getSerializableExtra("item") as HashMap<String, Int>
        val hashMap = DataItemPesanan.ItemPesanan

        for (i in hashMap){
            var user = DataPesanan(i.key,i.value.toString())
            result.add(user)
        }

        return result
    }

    //Cek pelayanan oncall analis
    fun cek_analis(id_klinik:String, id_pasien:String, item_pemeriksaan:Serializable, harga_pemeriksaan:String, id_pemeriksaan:String)
    {
        tb_konfirm.text = "Lanjut"
        var url_check_analis=Connection.url + "admin/page_pemeriksaan/check_permission_analis_byid_api?pasien="+id_pasien+"&klinik="+id_klinik+"&id_pemeriksaan="+id_pemeriksaan


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
                    dialogKonfirmasi("Mohon maaf, analis lagi tidak melayani pemeriksaan")
                }
                else{
                    dialogKonfirmasi("Sistem kesediaan analis error")
                }
            },
            Response.ErrorListener{ error ->
                dialogKonfirmasi("Koneksi internet error")
            })

        sr_check_analis.setRetryPolicy(DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        rq_check_analis.add(sr_check_analis)
    }


    //Setelah melakukan oncall
    fun after_oncall(id_pasien:String, id_pemeriksaan_oncall:String)
    {
        //Tombol konfim hilang
        tb_konfirm.visibility = View.GONE

        var url_pemeriksaan_oncall = Connection.url+"admin/page_pemeriksaan/konfirmasi_menunggu_hasil_oncall/$id_pemeriksaan_oncall"
        var rq_pemeriksaan_oncall = Volley.newRequestQueue(this)
        var sr_pemeriksaan_oncall = JsonObjectRequest(Request.Method.GET, url_pemeriksaan_oncall,null,
            Response.Listener{ response ->
                var proses = response.getBoolean("proses")

                if(proses.equals(true)){
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val reference = firebaseDatabase.getReference()
                    reference.child("permintaanOncall").child(id_pasien).removeValue()

                    var a= Intent(this,MenungguHasil::class.java)
                    startActivity(a)
                    finish()
                }else{
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val reference = firebaseDatabase.getReference()
                    reference.child("permintaanOncall").child(id_pasien).removeValue()

                    var a= Intent(this,MenungguHasil::class.java)
                    startActivity(a)
                    finish()
                }


            },Response.ErrorListener {error ->
                dialogKonfirmasi("Koneksi internet error")
                //Toast.makeText(this, error.message.toString(),Toast.LENGTH_LONG).show()
            })
        rq_pemeriksaan_oncall.add(sr_pemeriksaan_oncall)

    }

    fun dialogKonfirmasi(message:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Kembali",
                DialogInterface.OnClickListener { dialog, id ->
                    var a= Intent(this,HomeActivity::class.java)
                    startActivity(a)
                    finish()
                })
        // Create the AlertDialog object and return it
        val alert = builder.create()
        alert.show()
    }



}

