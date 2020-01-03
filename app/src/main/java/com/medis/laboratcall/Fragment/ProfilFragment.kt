package com.medis.laboratcall.Fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_profil.*
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
        var token_id  = this.activity!!.getSharedPreferences("id", Context.MODE_PRIVATE)
        var id_pasien  = token_id.getString("iduser","")

        var nama:String = ""
        var jenis_kelamin:String = ""
        var tmp_lahir:String = ""
        var tanggal_lahir:String = ""
        var alamat:String = ""
        var nama_klinik_pasien:String = ""
        var id_klinik_pasien:String=""
        var no_wa_pasien:String = ""
        var username:String = ""
        var password:String = ""
        var foto:String = ""


        view.tb_logout.setOnClickListener{

            //Clear sharedpreference
            var editor_username = token.edit()
            editor_username.clear()
            editor_username.commit()

            var editor_id = token_id.edit()
            editor_id.clear()
            editor_id.commit()

            var intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        //GET DATA PROFIL PASIEN
        var url = Connection.url + "apiclient/apiuser/get_pasien_id?id_pasien=$id_pasien"

       // println(url)

        var rq=Volley.newRequestQueue(context)
        var sr=JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener{ response ->
                nama = response.getString("nama")
                jenis_kelamin = response.getString("jenis_kelamin")
                tanggal_lahir = response.getString("tanggal_lahir")
                tmp_lahir = response.getString("tmp_lahir")
                alamat = response.getString("alamat")
                id_klinik_pasien = response.getString("id_klinik_pasien")
                nama_klinik_pasien = response.getString("nama_laboratorium")
                no_wa_pasien = response.getString("no_wa_pasien")
                username = response.getString("username")
                password = response.getString("password")
                foto = response.getString("foto")

                //TEXT VIEW
                view.tx_nama_profil.text = nama
                view.tx_jenkel_profil.text = jenis_kelamin
                view.tx_tmp_lahir_profil.text = tmp_lahir+" ,"
                view.tx_tgl_lahir_profil.text = tanggal_lahir
                view.tx_alamat_profil.text = alamat
                view.tx_klinik_profil.text = "Laboratorium Klinik "+nama_klinik_pasien
                view.tx_no_wa_profil.text = "+"+no_wa_pasien
                view.tx_username_profil.text = username
//                view.tx_pass_profil.text = password

                // TOMBOL EDIT
//                view.tb_edit_foto.setOnClickListener {
//                    var i =  Intent(getActivity(), EditProfil::class.java)
//                    i.putExtra("action", "edit_foto")
//                    startActivity(i)
//                }


                view.tb_edit_profil.setOnClickListener {
                    var i =  Intent(getActivity(), EditProfil::class.java)
                    i.putExtra("action", "edit_profil")
                    i.putExtra("id_pasien", id_pasien)
                    i.putExtra("nama", nama)
                    i.putExtra("jenis_kelamin", jenis_kelamin)
                    i.putExtra("tmp_lahir", tmp_lahir)
                    i.putExtra("tanggal_lahir", tanggal_lahir)
                    i.putExtra("alamat", alamat)
                    i.putExtra("id_klinik_pasien", id_klinik_pasien)
                    i.putExtra("nama_klinik_pasien", nama_klinik_pasien)
                    i.putExtra("no_wa_pasien", no_wa_pasien)
                    startActivity(i)
                }

                view.tb_edit_akun.setOnClickListener {
                    var i =  Intent(getActivity(), EditProfil::class.java)
                    i.putExtra("action", "edit_akun")
                    i.putExtra("id_pasien", id_pasien)
                    i.putExtra("username", username)
                    i.putExtra("password", password)
                    startActivity(i)
                }
            },
            Response.ErrorListener{ error ->
               println(error.message.toString())
            })

        rq.add(sr)

        return view

    }

}// Required empty public constructor
