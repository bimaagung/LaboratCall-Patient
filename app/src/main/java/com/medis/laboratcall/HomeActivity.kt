package com.medis.laboratcall

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.Toast
import com.medis.laboratcall.Fragment.HomeFragment
import com.medis.laboratcall.Fragment.ProfilFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
//import android.R
import android.util.Log
import android.widget.TextView
import com.google.firebase.iid.FirebaseInstanceId
import com.medis.laboratcall.Data.DataItemPesanan
import com.medis.laboratcall.services.FirebaseMessagingService


class HomeActivity : AppCompatActivity() {
    private var content: FrameLayout? = null


    //kofigurasi navigasi bottom tab
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {  //koneksi id navigation_home pada menu/navigation.xml
                val fragment = HomeFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profil -> { //koneksi id navigation_profil pada menu/navigation.xml
                val fragment = ProfilFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //installl fragment
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener) //koneksi navigasi dan konfigurasi
        val fragment = HomeFragment() //tab muncul pertama
        addFragment(fragment)

        //Clear data pesanan
        DataItemPesanan.ItemPesanan.clear()

        var token  = getSharedPreferences("id", Context.MODE_PRIVATE)
    }
}
