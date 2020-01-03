package com.medis.laboratcall

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.medis.laboratcall.Pendaftaran.PendaftaranActivity
import org.jetbrains.anko.indeterminateProgressDialog


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var loading = this.indeterminateProgressDialog("Silahkan Menunggu")
        loading.apply {
            setCancelable(false)
        }
        loading.dismiss()


        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        var tokenNotif = ""

        if (token.getString("iduser", " ") != " ") {
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
           // println(token.getString("iduser", " "))
        }

        //        Token Notification
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    tokenNotif = task.result!!.token
                } else {

                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }

        //FirebaseMessaging.getInstance().subscribeToTopic("test");

        tb_login.setOnClickListener {

            loading.show()

            var url = Connection.url + "apiclient/apiuser/login/" + tx_username.text.toString() + "/" + tx_password.text.toString() + "/" + tokenNotif
           // var url = Connection.url + "apiclient/apiuser/login/bima/bima/"+ tokenNotif

            var rq = Volley.newRequestQueue(this)
            var sr = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    loading.dismiss()
                    var login = response.getBoolean("login")

                    if (login == true) {
                        var id = response.getString("id")
                        var user = response.getString("nama")
                        var jenkel = response.getString("jenis_kelamin")
                        var umur = response.getString("tanggal_lahir")
                        var foto = response.getString("foto")
                        var klinik = response.getString("id_klinik")

                        //Pindah ke activity home
                        var i = Intent(this, HomeActivity::class.java)
                        intent.putExtra("id", id)


                        //session token
                        var editor = token.edit()
                        editor.putString("iduser", id)
                        editor.putString("nama", user)
                        editor.putString("jenkel", jenkel)
                        editor.putString("klinik", klinik)
                        editor.commit()

                        startActivity(i)
                        finish()

                    } else if (login == false) {
                        Toast.makeText(
                            this,
                            "Login gagal, silahkan mendaftar ke laboratorium modern terdekat",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "Sistem login error", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(
                        this, error.message,
                        Toast.LENGTH_LONG
                    ).show()
                })

            rq.add(sr)
        }

        tb_pendaftaran.setOnClickListener {
            loading.dismiss()
            var i = Intent(this, PendaftaranActivity::class.java)
            startActivity(i)
        }

    }
}
