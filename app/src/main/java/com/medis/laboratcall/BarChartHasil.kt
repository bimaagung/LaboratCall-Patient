package com.medis.laboratcall

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_bar_chart_hasil.*
import com.github.mikephil.charting.components.YAxis
import org.jetbrains.anko.indeterminateProgressDialog


class BarChartHasil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart_hasil)

        //Tombol Kemabali
        tb_back.setOnClickListener {
            var back = Intent(this,ListItemPemeriksaan::class.java)
            startActivity(back)
            finish()
        }

        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        var jenkel = token.getString("jenkel","")
        var id_pasien = token.getString("iduser","")
        var id_item = intent.getStringExtra("id_item")
        var jenis_pemeriksaan = intent.getStringExtra("jenis_pemeriksaan")
        var item_pemeriksaan = intent.getStringExtra("item_pemeriksaan")
        var nl = intent.getStringExtra("nl")
        var np = intent.getStringExtra("np")
        var keterangan = intent.getStringExtra("keterangan")
        var kategori = intent.getStringExtra("kategori")

        //Text View
        tx_jenis_pemeriksaan_barchart.text = jenis_pemeriksaan
        tx_item_pemeriksaan_barchart.text = item_pemeriksaan
        tx_nl_barchart.text = nl
        tx_np_barchart.text = np
        tx_keterangan_barchart_1.text = keterangan
        tx_keterangan_barchart_2.text = keterangan
        tx_kategori_barchart.text = kategori

        var option = 0

        try
        {
            setBarChart(jenkel, id_pasien, id_item)
        } catch (e: Exception) {
            option = 1
        }
        finally {
            if(option == 1)
            {
                dialogKonfirmasi("Handling Error")
            }else{
               // dialogKonfirmasi("Tidak ada koneksi internet")
            }
        }

    }

    private fun setBarChart(jenkel:String, id_pasien:String, id_item:String) {
        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.show()



        var url=Connection.url+"admin/page_pemeriksaan/get_api_hasil_chart?id_pasien=$id_pasien&id_item=$id_item"
        var rq = Volley.newRequestQueue(this)
        var jar = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                loading.dismiss()

                val entries = ArrayList<BarEntry>()
                var dataHasil = 0

                for (x in 0..response.length()-1) {
                    dataHasil = response.getJSONObject(x).getInt("hasil_pemeriksaan")
                    entries.add(BarEntry( dataHasil.toFloat(), x))
                }

                val barDataSet = BarDataSet(entries, "Cells")

                val Date = ArrayList<String>()

                for (x in 0..response.length()-1) {
                    Date.add(response.getJSONObject(x).getString("tanggal"))
                }

                val data = BarData(Date, barDataSet)

                barChart.data = data // set the data and list of lables into chart

                barChart.setDescription("Hasil Pemeriksaan")  // set the description

                barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                // barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(12f);
                val y = barChart.getAxisLeft()

                var min = 0
                var max = 0

                if(jenkel.equals("L"))
                {
                    for (x in 0 until response.length()-1) {
                        min = response.getJSONObject(1).getInt("min_lk")
                        max = response.getJSONObject(1).getInt("max_lk")
                        y.setAxisMaxValue(max.toFloat())
                        y.setAxisMinValue(min.toFloat())
                    }
                }else{
                    for (x in 0 until response.length()-1) {
                        min = response.getJSONObject(1).getInt("min_np")
                        max = response.getJSONObject(1).getInt("max_np")
                        y.setAxisMaxValue(max.toFloat())
                        y.setAxisMinValue(min.toFloat())
                    }
                }

                barChart.animateY(2000)

            },
            Response.ErrorListener{ error ->
                loading.dismiss()
                dialogKonfirmasi("Tidak ada koneksi internet")
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
}
