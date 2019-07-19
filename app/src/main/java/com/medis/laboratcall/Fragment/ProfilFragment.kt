package com.medis.laboratcall.Fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medis.laboratcall.HomeActivity
import com.medis.laboratcall.MainActivity
import com.medis.laboratcall.PaketPromo
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_profil.view.*


/**
 * A simple [Fragment] subclass.
 */
class ProfilFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // koneksi pada layout
        val view: View = inflater.inflate(R.layout.fragment_profil, container, false)

        var token = this.activity!!.getSharedPreferences("username", Context.MODE_PRIVATE)

        view.tb_logout.setOnClickListener{
            var editor = token.edit()
            editor.putString("loginusername"," ")
            editor.commit()

            var intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        return view

    }

}// Required empty public constructor
