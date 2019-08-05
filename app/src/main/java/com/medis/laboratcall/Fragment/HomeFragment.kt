package com.medis.laboratcall.Fragment


import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medis.laboratcall.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import android.content.SharedPreferences
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference



/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // koneksi pada layout
//        return  inflater.inflate(R.layout.fragment_home, container, false)


        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        var token = this.activity!!.getSharedPreferences("id", Context.MODE_PRIVATE)

        view.tx_nama.text = token.getString("nama","")

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

            var id_user = token.getString("iduser","")
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val reference = firebaseDatabase.getReference()
            reference.child("pemeriksaan").child(id_user).removeValue()

            var k =  Intent(getActivity(), RekapHasil::class.java)
            startActivity(k)
        }

//        view.tb_location.setOnClickListener{
//            var l =  Intent(getActivity(), lablocation::class.java)
//            startActivity(l)
//        }


        var id_user = token.getString("iduser","")

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val reference = firebaseDatabase.getReference()

        //val query = reference.child("konfirm").orderByChild("id_pemeriksaan").equalTo(id_user)
        val query = reference.child("pemeriksaan").child(id_user.toString()).orderByChild("id_pasien").equalTo(id_user.toString())


        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("message_debug","Berhasil Firebase1")
                    //visible notif_badge
                    view.badge_notif.visibility = View.VISIBLE

                    //Margin 16 liniear layout
                    val param1 = view.linearLayout3.layoutParams as ConstraintLayout.LayoutParams
                    param1.setMargins(0,40,0,0)
                    view.linearLayout3.layoutParams = param1

                    //Margin 0 tb_report
                    val param2 = view.tb_report.layoutParams as RelativeLayout.LayoutParams
                    param2.setMargins(0,10,0,0)
                    view.tb_report.layoutParams = param2
                }else{
                    Log.d("message_debug","Error Firebase1")
                    //visible notif_badge
                    view.badge_notif.visibility = View.INVISIBLE

                    //Margin 16 liniear layout
                    val param1 = view.linearLayout3.layoutParams as ConstraintLayout.LayoutParams
                    param1.setMargins(0,40,0,0)
                    view.linearLayout3.layoutParams = param1

                    //Margin 0 tb_report
                    val param2 = view.tb_report.layoutParams as RelativeLayout.LayoutParams
                    param2.setMargins(0,0,0,0)
                    view.tb_report.layoutParams = param2
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
               Log.d("message_debug","Error Firebase")
            }
        })

        return view



    }




}// Required empty public constructor
