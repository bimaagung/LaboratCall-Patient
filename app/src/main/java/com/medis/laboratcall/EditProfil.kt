package com.medis.laboratcall

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Fragment.ProfilFragment
import kotlinx.android.synthetic.main.activity_edit_profil.*



class EditProfil : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)



        var action = intent.getStringExtra("action")
        var id_pasien = intent.getStringExtra("id_pasien")
        var nama = intent.getStringExtra("nama")
        var jenis_kelamin = intent.getStringExtra("jenis_kelamin")
        var jenis_kelamin_update = intent.getStringExtra("jenis_kelamin")
        var tmp_lahir = intent.getStringExtra("tmp_lahir")
        var tanggal_lahir = intent.getStringExtra("tanggal_lahir")
        var alamat = intent.getStringExtra("alamat")
        //var id_klinik_pasien = intent.getStringExtra("id_klinik_pasien")
        var nama_klinik_pasien = intent.getStringExtra("nama_klinik_pasien")
        var no_wa_pasien = intent.getStringExtra("no_wa_pasien")
        var username = intent.getStringExtra("username")
        var password_lama:String = ""
        var foto = intent.getStringExtra("foto")
        var password_baru:String=""
        var retype_password:String=""




        if(action.equals("edit_foto"))
        {

        }else if(action.equals("edit_profil"))
        {
            v_edit_profil.visibility = View.VISIBLE
            v_edit_akun.visibility = View.GONE

            // SPINNER KLINIK
            val linearLayout = findViewById<LinearLayout>(R.id.rootContainer)

            val spinner = Spinner(this)
            spinner.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            val personNames = arrayOf("Devaria Medika")
            val arrayAdapter = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, personNames)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    nama_klinik_pasien = spinner.getSelectedItem().toString().replace(" ","+")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }

            // Add Spinner to LinearLayout
            linearLayout?.addView(spinner)

            //SPINNER JENKEL
            val adapter = ArrayAdapter.createFromResource(this,R.array.jenis_kelamin, android.R.layout.simple_spinner_item)
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            tx_edit_jenkel.adapter = adapter

            val spinnerPosition = adapter.getPosition(jenis_kelamin)
            tx_edit_jenkel.setSelection(spinnerPosition)

            tx_edit_jenkel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    jenis_kelamin_update = tx_edit_jenkel.getSelectedItem().toString().replace(" ","+")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }

            tb_batal_edit.setOnClickListener {
                finish()
            }

            tx_edit_nama.setText(nama)
            tx_edit_tmp_lahir.setText(tmp_lahir)
            tx_edit_tgl_lahir.setText(tanggal_lahir)
            tx_edit_alamat.setText(alamat)
            tx_edit_no_wa.setText(no_wa_pasien)




            tb_simpan_profil.setOnClickListener{
                simpan_profil(id_pasien, nama, jenis_kelamin, tmp_lahir, tanggal_lahir, alamat,
                    nama_klinik_pasien, no_wa_pasien)
            }

        }else if(action.equals("edit_akun"))
        {
            v_edit_profil.visibility = View.GONE
            v_edit_akun.visibility = View.VISIBLE

            tx_edit_username.setText(username)

            tb_simpan_profil.setOnClickListener{
                simpan_akun(id_pasien, username, password_lama, password_baru, retype_password)
            }
        }else{
            println("Respon action error")
        }

    }

    fun simpan_profil(id_pasien:String, nama:String, jenis_kelamin_update:String, tmp_lahir:String, tanggal_lahir:String, alamat:String,
                      nama_klinik_pasien:String, no_wa_pasien:String)
    {
        var token = getSharedPreferences("id", Context.MODE_PRIVATE)

        var nama = tx_edit_nama.text.toString().replace(" ","+")
        var tmp_lahir = tx_edit_tmp_lahir.text.toString().replace(" ","+")
        var tanggal_lahir = tx_edit_tgl_lahir.text.toString()
        var alamat = tx_edit_alamat.text.toString().replace(" ","+")
        var no_wa_pasien_non_fix = tx_edit_no_wa.text.toString()
        var no_wa_pasien = "62" + no_wa_pasien_non_fix.substring(1)


        var url=Connection.url + "apiclient/apiuser/update_pasien_api?id_pasien=$id_pasien&nama=$nama&" +
                "jenis_kelamin=$jenis_kelamin_update&tmp_lahir=$tmp_lahir&tanggal_lahir=$tanggal_lahir&alamat=$alamat&" +
                "no_wa_pasien=$no_wa_pasien&nama_klinik_pasien=$nama_klinik_pasien"

        println(url)

        var rq= Volley.newRequestQueue(this)
        var sr= JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener{ response ->
                var proses = response.getBoolean("proses")
                var id_klinik = response.getString("id_klinik")

                if(proses == true)
                {
                    println("Update profil berhasil")
                    dialogKonfirmasi("Update profil berhasil")

                    //session token
                    var editor = token.edit()
                    editor.putString("nama", nama.replace("+"," "))
                    editor.putString("jenkel", jenis_kelamin_update)
                    editor.putString("klinik", id_klinik)
                    editor.commit()

                }else if(proses == false){
                    println("Update profil gagal")
                    dialogKonfirmasi("Update profil gagal")
                }
                else{
                    dialogKonfirmasi("Penyimpanan profil error")
                }
            },
            Response.ErrorListener{ error ->
                dialogKonfirmasi("Penyimpanan profil error, silahkan periksa koneksi internet anda")
            })

        rq.add(sr)
    }

    fun simpan_akun(id_pasien:String, username:String, password_lama:String, password_baru:String, retype_password:String)
    {
        var username = tx_edit_username.text.toString().replace(" ","+")
        var password_lama = tx_password_lama.text.toString().replace(" ","+")
        var password_baru = tx_password_baru.text.toString().replace(" ","+")
        var retype_password = tx_retype_password.text.toString().replace(" ","+")

        var url=Connection.url + "apiclient/apiuser/update_akun_pasien_api?id_pasien=$id_pasien&username=$username&" +
                "password_lama=$password_lama&password_baru=$password_baru&retype_password=$retype_password"

        println(url)

        var rq= Volley.newRequestQueue(this)
        var sr= JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener{ response ->
                var username = response.getBoolean("username")
                var password = response.getBoolean("password")

                if(username == true && password == true)
                {
                    dialogKonfirmasi("Update password  berhasil")
                    println("Update password  berhasil")
                }else if(username == true && password == false){

                    dialogKonfirmasi("Update username  berhasil")
                    println("Update username  berhasil")

                }else if(username == false && password == false){
                    dialogKonfirmasi("Update username dan password gagal")
                    println("Update username dan password gagal")

                }
                else{
                    dialogKonfirmasi("Penyimpanan akun error, silahkan periksa koneksi internet anda")
                    println("Update akun error")
                }

            },
            Response.ErrorListener{ error ->
                dialogKonfirmasi("Penyimpanan akun error, silahkan periksa koneksi internet anda")
            })

        rq.add(sr)
    }

    fun dialogKonfirmasi(message:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Kembali",
                DialogInterface.OnClickListener { dialog, id ->
                    var i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                })
        // Create the AlertDialog object and return it
        val alert = builder.create()
        alert.show()
    }
}


