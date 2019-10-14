package com.medis.laboratcall.Pendaftaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.activity_pendaftaran.*
import kotlinx.android.synthetic.main.activity_pendaftaran.tb_back

class PendaftaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran)

        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        var nama:String = ""
        var jenis_kelamin:String = ""
        var tmp_lahir:String = ""
        var tanggal_lahir:String = ""
        var alamat:String = ""

        //SPINNER JENKEL
        val adapter = ArrayAdapter.createFromResource(this,R.array.jenis_kelamin, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tx_jenkel.adapter = adapter

        tx_jenkel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                jenis_kelamin = tx_jenkel.getSelectedItem().toString().replace(" ","+")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        tb_lanjut.setOnClickListener{
            nama = tx_nama.text.toString().replace(" ","+")
            tmp_lahir = tx_tmp_lahir.text.toString().replace(" ","+")
            tanggal_lahir = tx_tgl_lahir.text.toString()
            alamat = tx_alamat.text.toString().replace(" ","+")

            var i = Intent(this, PendaftaranAkun::class.java)
            i.putExtra("nama", nama)
            i.putExtra("jenis_kelamin", jenis_kelamin)
            i.putExtra("tmp_lahir", tmp_lahir)
            i.putExtra("tanggal_lahir", tanggal_lahir)
            i.putExtra("alamat", alamat)
            startActivity(i)
            finish()
        }



    }
}
