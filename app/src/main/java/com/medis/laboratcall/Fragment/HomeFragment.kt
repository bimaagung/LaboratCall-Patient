package com.medis.laboratcall.Fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.medis.laboratcall.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import android.content.SharedPreferences




/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // koneksi pada layout
//        return  inflater.inflate(R.layout.fragment_home, container, false)


        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        var token = this.activity!!.getSharedPreferences("username", Context.MODE_PRIVATE)

        view.tx_nama.text = token.getString("loginusername","")

        view.tx_asuser.text = "Pasien"

        view.tb_promo.setOnClickListener{
            var i =  Intent(getActivity(), PaketPromo::class.java)
            startActivity(i)
        }

        view.tb_pemeriksaan.setOnClickListener{
            var j =  Intent(getActivity(), Layanan::class.java)
            startActivity(j)
        }

        view.tb_report.setOnClickListener{
            var k =  Intent(getActivity(), RekapHasil::class.java)
            startActivity(k)
        }

        view.tb_location.setOnClickListener{
            var l =  Intent(getActivity(), lablocation::class.java)
            startActivity(l)
        }

        return view

    }


}// Required empty public constructor
