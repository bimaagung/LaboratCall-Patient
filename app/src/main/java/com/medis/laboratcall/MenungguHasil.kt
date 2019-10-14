package com.medis.laboratcall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.medis.laboratcall.Data.DataItemPesanan
import kotlinx.android.synthetic.main.activity_menunggu_hasil.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class MenungguHasil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menunggu_hasil)

        text_label_menunggu_hasil.text = "Menunggu Hasil"
        text_menuggu_hasil.text = "Silahkan melakukan pembayaran dan tunggu sampai hasil pemeriksaan keluar. Untuk melihat hasil pemeriksaan silahkan pilih menu hasil pemeriksaan di menu utama"

        //Clear data pesanan
        DataItemPesanan.ItemPesanan.clear()

        tb_selesain.visibility = View.VISIBLE
        tb_selesain.setOnClickListener {

            var i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
