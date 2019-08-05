package com.medis.laboratcall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_layanan.*

class Layanan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan)

        tbDitempat.setOnClickListener{
            var i =  Intent(this, PilihPemeriksaan::class.java)
            i.putExtra("layanan", "offlocation")
            startActivity(i)
        }

        tbOncall.setOnClickListener{
            var a =  Intent(this, PilihPemeriksaan::class.java)
            a.putExtra("layanan", "onlocation")
            startActivity(a)
        }
    }

}
