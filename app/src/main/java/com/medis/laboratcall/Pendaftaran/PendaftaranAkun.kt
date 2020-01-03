package com.medis.laboratcall.Pendaftaran

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.iid.FirebaseInstanceId
import com.medis.laboratcall.Connection
import com.medis.laboratcall.MainActivity
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.activity_pendaftaran_akun.*
import org.jetbrains.anko.indeterminateProgressDialog




class PendaftaranAkun : AppCompatActivity() {

    var nama_pasien_pendaftaran:String = ""
    var jenis_kelamin:String = ""
    var tmp_lahir:String = ""
    var tanggal_lahir:String = ""
    var alamat:String = ""
    var no_wa_pasien:String = ""
    var username:String = ""
    var password:String = ""
    var retype_password:String = ""
    var nama_klinik_pasien:String = ""
    var tokenNotif:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran_akun)

        tb_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }

        loading.dismiss()

        nama_pasien_pendaftaran = intent.getStringExtra("nama")
        jenis_kelamin = intent.getStringExtra("jenis_kelamin")
        tmp_lahir = intent.getStringExtra("tmp_lahir")
        tanggal_lahir = intent.getStringExtra("tanggal_lahir")
        alamat = intent.getStringExtra("alamat")


        // SPINNER KLINIK
        val linearLayout = findViewById<LinearLayout>(R.id.rootContainerKlinik)

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

        tb_simpan_pendaftaran.setOnClickListener {

            loading.show()


            no_wa_pasien = tx_no_wa.text.toString()
            username = tx_username.text.toString().replace(" ","+")
            password= tx_password.text.toString().replace(" ","+")
            retype_password = tx_retype_password.text.toString().replace(" ","+")

            //        Token Notification

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        tokenNotif = task.result!!.token
                    } else {

                        Toast.makeText(this, task.exception!!.message,Toast.LENGTH_SHORT).show()
                    }
                }

            println(tokenNotif)

            //Logika input tidak diisi
            if(nama_pasien_pendaftaran == "" || jenis_kelamin == "" || tmp_lahir == "" || tanggal_lahir == "" || alamat == "" ||
                no_wa_pasien == "" || username == "" || password == "" || retype_password == "" || nama_klinik_pasien == "")
            {
                loading.dismiss()
                dialogKonfirmasiUsername("Silahkan isi formulir tersebut dengan benar!")
            }else{
                var no_wa_pasien_fix = "62" + no_wa_pasien.substring(1)

                var url= Connection.url + "apiclient/apiuser/register_pasien_api?nama=$nama_pasien_pendaftaran&" +
                        "jenis_kelamin=$jenis_kelamin&tmp_lahir=$tmp_lahir&tanggal_lahir=$tanggal_lahir&alamat=$alamat&" +
                        "token_pasien=$tokenNotif&nama_klinik_pasien=$nama_klinik_pasien&no_wa_pasien=$no_wa_pasien_fix&username=$username" +
                        "&password=$password&retype_password=$retype_password"


                var rq= Volley.newRequestQueue(this)
                var sr= JsonObjectRequest(
                    Request.Method.GET,url,null,
                    Response.Listener{ response ->
                        loading.dismiss()

                        var proses = response.getBoolean("proses")

                        var username = response.getBoolean("username")

                        if(proses == true)
                        {
                            dialogKonfirmasi("Pendaftaran berhasil berhasil, silahkan masuk memalalui login")
                        }
                        else if(proses == false && username == true){
                            dialogKonfirmasiUsername("Username sudah digunakan, silahkan gunakan yang lain")
                        }
                        else if(proses == false){
                            dialogKonfirmasi("Pendaftaran gagal, pastikan semua benar")
                        }else{
                            dialogKonfirmasi("Pendaftaran error")
                        }
                    },
                    Response.ErrorListener{ error ->
                        loading.dismiss()
                        dialogKonfirmasi("Pendaftaran error, silahkan periksa koneksi internet anda")
                    })

                rq.add(sr)
            }
        }
    }

    fun dialogKonfirmasi(message:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Lanjut",
                DialogInterface.OnClickListener { dialog, id ->
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                })
        // Create the AlertDialog object and return it
        val alert = builder.create()
        alert.show()
    }

    fun dialogKonfirmasiUsername(message:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Mengerti",
                DialogInterface.OnClickListener { dialog, id ->
                   dialog.dismiss()
                })
        // Create the AlertDialog object and return it
        val alert = builder.create()
        alert.show()
    }
}
