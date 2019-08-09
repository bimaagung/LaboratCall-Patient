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
import kotlinx.android.synthetic.main.activity_menunggu_hasil.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class MenungguHasil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menunggu_hasil)

        tb_selesain.visibility = View.INVISIBLE

        var layanan_pasien:String = intent.getStringExtra("layanan")

        if(layanan_pasien.equals("offlocation"))
        {
            text_label_menunggu_hasil.text = "Menunggu Hasil"
            text_menuggu_hasil.text = "Silahkan melakukan pembayaran dan tunggu sampai hasil pemeriksaan keluar. Untuk melihat hasil pemeriksaan silahkan pilih menu hasil pemeriksaan di menu utama"

            tb_selesain.visibility = View.VISIBLE
            tb_selesain.setOnClickListener{
                intent_to_home()
            }
        }else if(layanan_pasien.equals("onlocation"))
        {
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val reference = firebaseDatabase.getReference()

            //val query = reference.child("konfirm").orderByChild("id_pemeriksaan").equalTo(id_user)
            val query = reference.child("pemeriksaanOncall").child("2").orderByChild("id_permintaan_oncall").equalTo("2")

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("message_debug","Berhasil Firebase oncall")

                        text_label_menunggu_hasil.text = "Konfirmasi Analis"
                        text_menuggu_hasil.text = "Silahkan menunggu analis sampai analis konfirmasi pemesanan pemeriksaan oncall anda, jikan lebih dari 15 menit tidak ada konfirmasi, analis tidak melayani pemeriksaan oncall"

                        tb_selesain.visibility = View.VISIBLE
                        tb_selesain.setOnClickListener{
                            intent_to_mapPasien()
                        }

                    }else{
                        Log.d("message_debug","Gagal Firebase oncall")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("message_debug","Error Firebase Konfirm Oncall Analis")
                }
            })

        }else{
            Log.d("Menuggu hasil","layanan pasien error")
        }

    }

    fun intent_to_mapPasien()
    {
        var a= Intent(this,MapPasien::class.java)
        startActivity(a)
    }

    fun intent_to_home()
    {
        var i= Intent(this,HomeActivity::class.java)
        startActivity(i)
    }
}
