package com.medis.laboratcall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menunggu_hasil.*

class MenungguHasil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menunggu_hasil)

        tb_selesain.setOnClickListener{
            var i= Intent(this,HomeActivity::class.java)
            startActivity(i)
        }

    }
}
