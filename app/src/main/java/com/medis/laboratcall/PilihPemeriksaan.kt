package com.medis.laboratcall

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.medis.laboratcall.Adapter.DetailPromoAdapter
import com.medis.laboratcall.Data.DataDetailPromo
import com.medis.laboratcall.Data.DataItemPesanan
import com.medis.laboratcall.Fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_paket_promo.*
import kotlinx.android.synthetic.main.activity_pilih_pemeriksaan.*
import kotlinx.android.synthetic.main.activity_pilih_pemeriksaan.tb_back


class PilihPemeriksaan : AppCompatActivity() {

    //var item = HashMap<String,Int>()
    var harga: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_pemeriksaan)

        //Tonbol Kemabali
        tb_back.setOnClickListener {
            var i = Intent(this, HomeActivity::class.java)
            startActivity(i)
            finish()
        }

        var layanan_promo: String? = intent.getStringExtra("layanan_promo")
        var token = getSharedPreferences("id", Context.MODE_PRIVATE)
        var id_klinik = token.getString("klinik", " ")


        //Cek no wa analis
        var option = 0
        try
        {
            get_no_wa_analis(id_klinik)
        }
        catch (e: Exception) {
            option = 1
        }
        finally {
            if(option == 1)
            {
                println("Error get nomer wa analis")
            }
        }




        if (layanan_promo.equals("promo")) //LAYANAN PROMO
        {

            //SET VISIBILITY
            item_pemeriksaan.visibility = View.GONE
            item_promo.visibility = View.VISIBLE

            var id_promo: String = intent.getStringExtra("id_promo")
            var harga_promo = intent.getStringExtra("harga_promo")

            //UPDATE HARGA PROMO
            harga += harga_promo.toInt()
            updateharga(harga)

            //TAMBAH ITEM
            tb_tambah_promo.setOnClickListener {
                item_pemeriksaan.visibility = View.VISIBLE
            }

            //SHOW ITEM PROMO
            var list = ArrayList<DataDetailPromo>()


            var url = Connection.url + "admin/page_promo/getItemPromoById?id_promo=$id_promo"
            var rq = Volley.newRequestQueue(this)
            var jar = JsonArrayRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    for (x in 0..response.length() - 1) {
                        list.add(
                            DataDetailPromo(response.getJSONObject(x).getString("item_promo"))
                        )

                        DataItemPesanan.ItemPesanan.put(response.getJSONObject(x).getString("item_promo"), 0)
                    }

                    var adp = DetailPromoAdapter(this, list)
                    rv_item_promo_pilihan.layoutManager = LinearLayoutManager(this)
                    rv_item_promo_pilihan.adapter = adp

                },
                Response.ErrorListener { error ->
                    println(error.message)
                    dialogKonfirmasi("Koneksi internet error")

                })
            rq.add(jar)


        } else {
            //SET CLEAR HASH MAP DATA PESANAN
            DataItemPesanan.ItemPesanan.clear()

            item_pemeriksaan.visibility = View.VISIBLE
            item_promo.visibility = View.GONE
        }


        //PILIHAN PEMERIKSAAN



        h1.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("Hemoglobin", 15000)
                updateharga(harga)
            } else {
                harga -= 15000
                DataItemPesanan.ItemPesanan.remove("Hemoglobin")
                totalHarga.text = harga.toString()
                updateharga(harga)
            }
        })

        h2.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("Hematokrit", 15000)
                updateharga(harga)
            } else {
                harga -= 15000
                DataItemPesanan.ItemPesanan.remove("Hematokrit")
                updateharga(harga)
            }
        })

        h3.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("Eritrosit", 15000)
                updateharga(harga)

            } else {
                harga -= 15000
                DataItemPesanan.ItemPesanan.remove("Eritrosit")
                updateharga(harga)
            }
        })

        h4.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("Lekosit", 15000)
                updateharga(harga)

            } else {
                harga -= 15000
                DataItemPesanan.ItemPesanan.remove("Lekosit")
                updateharga(harga)
            }
        })

        h5.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("Trombosit", 15000)
                updateharga(harga)

            } else {
                harga -= 15000
                DataItemPesanan.ItemPesanan.remove("Trombosit")
                updateharga(harga)
            }
        })

        h6.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                ly_h1.visibility = View.GONE
                ly_h2.visibility = View.GONE
                ly_h3.visibility = View.GONE
                ly_h4.visibility = View.GONE
                ly_h5.visibility = View.GONE
                ly_h8.visibility = View.GONE

                harga += 50000
                DataItemPesanan.ItemPesanan.put("Hemoglobin", 50000) // Belum fix
                DataItemPesanan.ItemPesanan.put("Hematokrit", 0)
                DataItemPesanan.ItemPesanan.put("Eritrosit", 0)
                DataItemPesanan.ItemPesanan.put("Lekosit", 0)
                DataItemPesanan.ItemPesanan.put("Trombosit", 0)


                updateharga(harga)

            } else {
                ly_h1.visibility = View.VISIBLE
                ly_h2.visibility = View.VISIBLE
                ly_h3.visibility = View.VISIBLE
                ly_h4.visibility = View.VISIBLE
                ly_h5.visibility = View.VISIBLE
                ly_h8.visibility = View.VISIBLE

                harga -= 50000
                DataItemPesanan.ItemPesanan.remove("Hemoglobin") // Belum fix
                DataItemPesanan.ItemPesanan.remove("Hematokrit")
                DataItemPesanan.ItemPesanan.remove("Eritrosit")
                DataItemPesanan.ItemPesanan.remove("Lekosit")
                DataItemPesanan.ItemPesanan.remove("Trombosit")

                updateharga(harga)
            }
        })

        h8.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("Golongan_darah_abo", 15000)
                updateharga(harga)

            } else {
                harga -= 15000
                DataItemPesanan.ItemPesanan.remove("Golongan_darah_abo")
                updateharga(harga)
            }
        })

        k1.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 20000
                DataItemPesanan.ItemPesanan.put("Gula_darah_sewaktu", 20000)
                DataItemPesanan.ItemPesanan.put("Gula_darah_puasa", 0)
                DataItemPesanan.ItemPesanan.put("Gula_darah_2_jam_pp", 0)
                updateharga(harga)

            } else {
                harga -= 20000
                DataItemPesanan.ItemPesanan.remove("Gula_darah_sewaktu")
                DataItemPesanan.ItemPesanan.remove("Gula_darah_puasa")
                DataItemPesanan.ItemPesanan.remove("Gula_darah_2_jam_pp")
                updateharga(harga)
            }
        })

        k2.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 30000
                DataItemPesanan.ItemPesanan.put("Cholesterol_total", 30000)
                updateharga(harga)

            } else {
                harga -= 30000
                DataItemPesanan.ItemPesanan.remove("Cholesterol_total")
                updateharga(harga)
            }
        })

        k3.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 30000
                DataItemPesanan.ItemPesanan.put("Triglyseride", 30000)
                updateharga(harga)

            } else {
                harga -= 30000
                DataItemPesanan.ItemPesanan.remove("Triglyseride")
                updateharga(harga)
            }
        })

        k4.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 25000
                DataItemPesanan.ItemPesanan.put("HDL-cholesterol", 25000)
                DataItemPesanan.ItemPesanan.put("LDL-cholesterol", 0)
                updateharga(harga)

            } else {
                harga -= 25000
                DataItemPesanan.ItemPesanan.remove("HDL-cholesterol")
                DataItemPesanan.ItemPesanan.remove("LDL-cholesterol")
                updateharga(harga)
            }
        })

        k5.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 25000
                DataItemPesanan.ItemPesanan.put("Asam_urat", 25000)
                updateharga(harga)

            } else {
                harga -= 25000
                DataItemPesanan.ItemPesanan.remove("Asam_urat")
                updateharga(harga)
            }
        })

        k6.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 25000
                DataItemPesanan.ItemPesanan.put("Ureum", 25000)
                updateharga(harga)

            } else {
                harga -= 25000
                DataItemPesanan.ItemPesanan.remove("Ureum")
                updateharga(harga)
            }
        })

        k7.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 25000
                DataItemPesanan.ItemPesanan.put("Creatinine", 25000)
                updateharga(harga)

            } else {
                harga -= 25000
                DataItemPesanan.ItemPesanan.remove("Creatinine")
                updateharga(harga)
            }
        })

        k8.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 25000
                DataItemPesanan.ItemPesanan.put("SGOT", 25000)
                updateharga(harga)

            } else {
                harga -= 25000
                DataItemPesanan.ItemPesanan.remove("SGOT")
                updateharga(harga)
            }
        })

        k9.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 25000
                DataItemPesanan.ItemPesanan.put("SGPT", 25000)
                updateharga(harga)

            } else {
                harga -= 25000
                DataItemPesanan.ItemPesanan.remove("SGPT")
                updateharga(harga)
            }
        })

        s1.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 40000
                DataItemPesanan.ItemPesanan.put("Salmonella_typhi_o", 40000)
                DataItemPesanan.ItemPesanan.put("Salmonella_typhi_h", 0)
                DataItemPesanan.ItemPesanan.put("Salmonella_paratyhi_ah", 0)
                DataItemPesanan.ItemPesanan.put("Salmonella_paratyhi_bh", 0)
                updateharga(harga)

            } else {
                harga -= 40000
                DataItemPesanan.ItemPesanan.remove("Salmonella_typhi_o")
                DataItemPesanan.ItemPesanan.remove("Salmonella_typhi_h")
                DataItemPesanan.ItemPesanan.remove("Salmonella_paratyhi_ah")
                DataItemPesanan.ItemPesanan.remove("Salmonella_paratyhi_bh")
                updateharga(harga)
            }
        })

        s2.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 40000
                DataItemPesanan.ItemPesanan.put("HbsAg", 40000)
                updateharga(harga)

            } else {
                harga -= 40000
                DataItemPesanan.ItemPesanan.remove("HbsAg")
                updateharga(harga)
            }
        })

        s3.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("HCG", 15000)
                updateharga(harga)

            } else {
                harga -= 50000
                DataItemPesanan.ItemPesanan.remove("HCG")
                updateharga(harga)
            }
        })


        u1.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 25000
                DataItemPesanan.ItemPesanan.put("Warna_urine", 25000)
                DataItemPesanan.ItemPesanan.put("Kekeruhan", 0)
                DataItemPesanan.ItemPesanan.put("pH", 0)
                DataItemPesanan.ItemPesanan.put("Berat_jenis", 0)
                DataItemPesanan.ItemPesanan.put("Glucosa", 0)
                DataItemPesanan.ItemPesanan.put("Keton", 0)
                DataItemPesanan.ItemPesanan.put("Billirubin", 0)
                DataItemPesanan.ItemPesanan.put("Urobillinogen", 0)
                DataItemPesanan.ItemPesanan.put("Nitrit", 0)
                DataItemPesanan.ItemPesanan.put("Eritrosit_urine_macro", 0)
                DataItemPesanan.ItemPesanan.put("Lekosit_urine_macro", 0)
                DataItemPesanan.ItemPesanan.put("Ascorbic_acid", 0)
                DataItemPesanan.ItemPesanan.put("Epitel", 0)
                DataItemPesanan.ItemPesanan.put("Eritrosit_urine_micro", 0)
                DataItemPesanan.ItemPesanan.put("Lekosit_urine_micro", 0)
                DataItemPesanan.ItemPesanan.put("Silinder", 0)
                DataItemPesanan.ItemPesanan.put("Kristal", 0)
                DataItemPesanan.ItemPesanan.put("Bakteri", 0)
                updateharga(harga)

            } else {
                harga -= 25000
                DataItemPesanan.ItemPesanan.remove("Warna_urine")
                DataItemPesanan.ItemPesanan.remove("Kekeruhan")
                DataItemPesanan.ItemPesanan.remove("pH")
                DataItemPesanan.ItemPesanan.remove("Berat_jenis")
                DataItemPesanan.ItemPesanan.remove("Glucosa")
                DataItemPesanan.ItemPesanan.remove("Keton")
                DataItemPesanan.ItemPesanan.remove("Billirubin")
                DataItemPesanan.ItemPesanan.remove("Urobillinogen")
                DataItemPesanan.ItemPesanan.remove("Nitrit")
                DataItemPesanan.ItemPesanan.remove("Eritrosit_urine_macro")
                DataItemPesanan.ItemPesanan.remove("Lekosit_urine_macro")
                DataItemPesanan.ItemPesanan.remove("Ascorbic_acid")
                DataItemPesanan.ItemPesanan.remove("Epitel")
                DataItemPesanan.ItemPesanan.remove("Eritrosit_urine_micro")
                DataItemPesanan.ItemPesanan.remove("Lekosit_urine_micro")
                DataItemPesanan.ItemPesanan.remove("Silinder")
                DataItemPesanan.ItemPesanan.remove("Kristal")
                DataItemPesanan.ItemPesanan.remove("Bakteri")
                updateharga(harga)
            }
        })

        u2.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 15000
                DataItemPesanan.ItemPesanan.put("Protein", 15000)
                updateharga(harga)

            } else {
                harga -= 15000
                DataItemPesanan.ItemPesanan.remove("Protein")
                updateharga(harga)
            }
        })


        f1.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                harga += 20000
                DataItemPesanan.ItemPesanan.put("Warna_feces", 20000)
                DataItemPesanan.ItemPesanan.put("Konsistensi", 0)
                DataItemPesanan.ItemPesanan.put("Bau", 0)
                DataItemPesanan.ItemPesanan.put("Lendir", 0)
                DataItemPesanan.ItemPesanan.put("Darah_samar", 0)
                DataItemPesanan.ItemPesanan.put("Parasit", 0)
                DataItemPesanan.ItemPesanan.put("Lekosit_feces", 0)
                DataItemPesanan.ItemPesanan.put("Eritrosit_feces", 0)
                DataItemPesanan.ItemPesanan.put("Protozoa", 0)
                DataItemPesanan.ItemPesanan.put("Telur_cacing", 0)
                DataItemPesanan.ItemPesanan.put("Epitel_feces", 0)
                DataItemPesanan.ItemPesanan.put("Kristal_feces", 0)
                DataItemPesanan.ItemPesanan.put("Sisa_makanan", 0)
                updateharga(harga)

            } else {
                harga -= 20000
                DataItemPesanan.ItemPesanan.remove("Warna")
                DataItemPesanan.ItemPesanan.remove("Konsistensi")
                DataItemPesanan.ItemPesanan.remove("Bau")
                DataItemPesanan.ItemPesanan.remove("Lendir")
                DataItemPesanan.ItemPesanan.remove("Darah_samar")
                DataItemPesanan.ItemPesanan.remove("Parasit")
                DataItemPesanan.ItemPesanan.remove("Lekosit_feces")
                DataItemPesanan.ItemPesanan.remove("Eritrosit_feces")
                DataItemPesanan.ItemPesanan.remove("Protozoa")
                DataItemPesanan.ItemPesanan.remove("Telur_cacing")
                DataItemPesanan.ItemPesanan.remove("Epitel_feces")
                DataItemPesanan.ItemPesanan.remove("Kristal_feces")
                DataItemPesanan.ItemPesanan.remove("Sisa_makanan")
                updateharga(harga)
            }
        })

        //TOMBOL LANJUT

        tb_lanjut.setOnClickListener {
            //mengambil value dari layanan
            val layananPemeriksaan = intent.getStringExtra("layanan")

            //logika eksekusi tergantung metode layanan
            if (layananPemeriksaan.equals("offlocation")) {
                var pembayaran = Intent(this, Pembayaran::class.java)
                // pembayaran.putExtra("item",item)
                pembayaran.putExtra("harga", harga.toString())
                pembayaran.putExtra("layanan", "offlocation")
                startActivity(pembayaran)
//                finish()

            } else if (layananPemeriksaan.equals("onlocation")) {
                var pembayaran = Intent(this, Pembayaran::class.java)
                //pembayaran.putExtra("item",item)
                pembayaran.putExtra("harga", harga.toString())
                pembayaran.putExtra("layanan", "onlocation")
                startActivity(pembayaran)
                finish()

            } else {
                Log.d("Error Pilih Pemeriksaan", "Metode Pembayaran")
            }

            //Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show()
        }

        tb_back.setOnClickListener {
            finish()
        }
    }

    fun dialogKonfirmasi(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("Keluar",
                DialogInterface.OnClickListener { dialog, id ->
                    finish()
                })
            .setNegativeButton("Tunggu",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        // Create the AlertDialog object and return it
        val alert = builder.create()
        alert.show()
    }

    fun updateharga(harga: Int) {
        totalHarga.text = harga.toString()
    }

    fun get_no_wa_analis(id_klinik:String)
    {
        pg_tanya_analis.visibility = View.VISIBLE
        tb_tanya_analis.visibility = View.GONE
        layout_info_pemeriksaan.visibility = View.VISIBLE

        var url = Connection.url + "admin/page_pemeriksaan/get_number_analis?id_klinik=$id_klinik"

        var rq = Volley.newRequestQueue(this)
        var sr = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                var no_wa_analis = response.getString("no_wa_analis")

                if (no_wa_analis != "" ) {

                    tb_tanya_analis.setOnClickListener{
                        val openURL = Intent(android.content.Intent.ACTION_VIEW)
                        openURL.data = Uri.parse("https://wa.me/$no_wa_analis")
                        startActivity(openURL)
                    }

                    pg_tanya_analis.visibility = View.GONE
                    tb_tanya_analis.visibility = View.VISIBLE
                    layout_info_pemeriksaan.visibility = View.VISIBLE

                } else {
                    layout_info_pemeriksaan.visibility = View.GONE
                }
            },
            Response.ErrorListener { error ->
                layout_info_pemeriksaan.visibility = View.GONE
            })

        rq.add(sr)
    }



}
