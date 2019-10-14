package com.medis.laboratcall

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.HasilPemeriksaanAdapter
import com.medis.laboratcall.Connection.Companion.url
import com.medis.laboratcall.Data.DataHasilPemeriksaan
import kotlinx.android.synthetic.main.activity_hasil_pemeriksaan.*
import org.jetbrains.anko.indeterminateProgressDialog
import java.util.jar.Manifest


class HasilPemeriksaan : AppCompatActivity() {

    private val STORAGE_PERMISSION_CODE: Int = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_pemeriksaan)

        //Tombol Kemabali
        tb_back.setOnClickListener {
            var back = Intent(this,RekapHasil::class.java)
            startActivity(back)
            finish()
        }

        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.show()


        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        var jenkel = token.getString("jenkel","")

        var list = ArrayList<DataHasilPemeriksaan>()

        //Show Info Klinik
        var url_klinik = Connection.url+"admin/page_pemeriksaan/get_pemeriksaan_byid_api/"+Connection.IdDetailPemeriksaan
        var rq_klinik = Volley.newRequestQueue(this)
        var sr_klink = JsonObjectRequest(Request.Method.GET, url_klinik,null,
            Response.Listener{ response ->
                tx_nama_klinik.text = "Klinik Lab "+response.getString("nama_laboratorium")
                tx_alamat_klinik.text = response.getString("alamat_laboratorium")

            },Response.ErrorListener {error ->
                dialogKonfirmasi("Tidak ada koneksi internet")
            })
        rq_klinik.add(sr_klink)


        //Show Hasil Pemeriksaan
        var url=Connection.url+"admin/page_pemeriksaan/hasil_pemeriksaan_pasien_api/"+Connection.IdDetailPemeriksaan
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(Request.Method.GET, url, null,
                   Response.Listener { response ->
                       loading.dismiss()

                       if(jenkel.equals("L"))
                       {
                           for (x in 0..response.length()-1)
                               list.add(DataHasilPemeriksaan(
                                   response.getJSONObject(x).getString("jenis_pemeriksaan"),
                                   response.getJSONObject(x).getString("item_pemeriksaan"),
                                   response.getJSONObject(x).getString("hasil_pemeriksaan"),
                                   response.getJSONObject(x).getString("keterangan"),
                                   response.getJSONObject(x).getString("stabilnl"),
                                   response.getJSONObject(x).getString("kategori")
                               ))

                       }else if(jenkel.equals("P"))
                       {
                           for (x in 0..response.length()-1)
                               list.add(DataHasilPemeriksaan(
                                   response.getJSONObject(x).getString("jenis_pemeriksaan"),
                                   response.getJSONObject(x).getString("item_pemeriksaan"),
                                   response.getJSONObject(x).getString("hasil_pemeriksaan"),
                                   response.getJSONObject(x).getString("keterangan"),
                                   response.getJSONObject(x).getString("stabilnp"),
                                   response.getJSONObject(x).getString("kategori")
                               ))
                       }else
                       {
                           for (x in 0..response.length()-1)
                               list.add(DataHasilPemeriksaan(
                                   response.getJSONObject(x).getString("jenis_pemeriksaan"),
                                   response.getJSONObject(x).getString("item_pemeriksaan"),
                                   response.getJSONObject(x).getString("hasil_pemeriksaan"),
                                   response.getJSONObject(x).getString("keterangan"),
                                   response.getJSONObject(x).getString("stabilnl"),
                                   response.getJSONObject(x).getString("kategori")
                               ))
                       }


                       var adp = HasilPemeriksaanAdapter(this,list)
                       layoutHasilPemeriksaan.layoutManager = LinearLayoutManager(this)
                       layoutHasilPemeriksaan.adapter = adp
                   },
                    Response.ErrorListener {error ->
                        loading.dismiss()
                        dialogKonfirmasi("Tidak ada koneksi internet")
                    })
        rq.add(jar)


        v_download_hasil.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
            {
                if(checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                {
                    requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                }else{
                    startDownload()
                }
            }else{
                startDownload()
            }
        }
    }

    private fun startDownload() {
        Toast.makeText(this,"Pengunduhan dimulai",Toast.LENGTH_SHORT).show()
        var nama = intent.getStringExtra("nama")
        var tanggal = intent.getStringExtra("tanggal")
        val request = DownloadManager.Request(Uri.parse(Connection.url+"admin/print_hasil?id_pemeriksaan="+Connection.IdDetailPemeriksaan))
       // val request = DownloadManager.Request(Uri.parse( "https://media.neliti.com/media/publications/209664-pengaruh-model-pembelajaran-kooperatif-t.pdf"))

        //println(Connection.IdDetailPemeriksaan)

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(Connection.IdDetailPemeriksaan+"_Hasil Pemeriksaan_"+tanggal+"_"+nama)
        request.setDescription("Silahkan menunggun sampai pengunduhan selesai...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}")

        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                 startDownload()
                }else {
                    Toast.makeText(this, "Akses gagal!", Toast.LENGTH_LONG).show()
                }
            }
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
