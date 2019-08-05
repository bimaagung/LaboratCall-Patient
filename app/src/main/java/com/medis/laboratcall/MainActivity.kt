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


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var token  = getSharedPreferences("id", Context.MODE_PRIVATE)
        var tokenNotif = ""

        if(token.getString("iduser"," ") != " ")
        {
            var intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        //        Token Notification
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    tokenNotif = task.result!!.token
                } else {

                    Toast.makeText(this, task.exception!!.message,Toast.LENGTH_SHORT).show()
                }
            }



        //FirebaseMessaging.getInstance().subscribeToTopic("test");

        tb_login.setOnClickListener {

            var url=Connection.url + "apiclient/apiuser/login/"+tx_username.text.toString()+"/"+tx_password.text.toString()+"/"+tokenNotif

            var rq=Volley.newRequestQueue(this)
            var sr=JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener{ response ->
                         var login = response.getBoolean("login")

                        if(login == true)
                        {
                            var id = response.getString("id")
                            var user = response.getString("nama")
                            var umur = response.getString("tanggal_lahir")
                            var foto = response.getString("foto")

                            //Pindah ke activity home
                            var i= Intent(this,HomeActivity::class.java)
                            intent.putExtra("id", id)


                            //session token
                            var editor = token.edit()
                            editor.putString("iduser", id)
                            editor.putString("nama", user)
                            editor.commit()

                            startActivity(i)

                        }else if(login == false){
                            Toast.makeText(this, "Login gagal, silahkan mendaftar ke laboratorium modern terdekat",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this, "Sistem login error",Toast.LENGTH_SHORT).show()
                        }
                },
                Response.ErrorListener{ error ->
                    Toast.makeText(this, error.message,
                        Toast.LENGTH_LONG).show()
                })

            rq.add(sr)
        }

    }
}
