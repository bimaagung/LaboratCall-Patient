package com.medis.laboratcall

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_layanan.*

class Layanan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layanan)

        tbDitempat.setOnClickListener{
            var i =  Intent(this, PilihPemeriksaan::class.java)
            startActivity(i)
        }

        tbOncall.setOnClickListener{
            var a =  Intent(this, LocationActivity::class.java)
            startActivity(a)
        }
    }

}
