package com.medis.laboratcall.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.medis.laboratcall.Connection
import com.medis.laboratcall.Data.DataHasilPemeriksaan
import com.medis.laboratcall.Data.DataPromo
import com.medis.laboratcall.DetailPromo
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.hasil_pemeriksaan_row.view.*
import kotlinx.android.synthetic.main.list_daftar_promo.view.*


//VIDEO TUTORIALL BAGIAN 3

class DaftarPromoAdapter(var context:Context, var list: ArrayList<DataPromo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.list_daftar_promo, parent, false)
        return DaftarPromo(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DaftarPromo).show(list[position].id,list[position].nama, list[position].deskripsi_promo, list[position].tanggal_mulai,
                                     list[position].tanggal_akhir,list[position].harga, list[position].harga_promo, list[position].foto)

        holder.itemView.card_daftar_promo.setOnClickListener{
            var i = Intent(context, DetailPromo::class.java)
            i.putExtra("id_promo", list[position].id)
            i.putExtra("nama_promo", list[position].nama)
            i.putExtra("deskripsi_promo", list[position].deskripsi_promo)
            i.putExtra("tanggal_mulai", list[position].tanggal_mulai)
            i.putExtra("tanggal_akhir", list[position].tanggal_akhir)
            i.putExtra("harga", list[position].harga)
            i.putExtra("harga_promo", list[position].harga_promo)
            context.startActivity(i)
            (context as Activity).finish()
        }
    }

    class DaftarPromo(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun show(id:String, nama:String, deskripsi_promo:String, tanggal_mulai:String, tanggal_akhir:String, harga:String, harga_promo:String, foto:String)
        {
            itemView.tx_nama_promo.text = nama
            itemView.tx_deskripsi_promo.text = deskripsi_promo
            itemView.tx_tanggal_mulai.text = tanggal_mulai
            itemView.tx_tanggal_akhir.text = tanggal_akhir
            itemView.tx_harga_normal.text = harga
            itemView.tx_harga_promo.text = harga_promo

            var option = 0
            try
            {
            Glide.with(itemView.context)
                .load(Connection.urlFoto + "assets/img/promo/"+foto)
                .apply(RequestOptions().override(270, 300))
                .into(itemView.foto_promo)

            }
            catch (e: Exception) {
                option = 1
            }
            finally {
                if(option == 1)
                {
                    println("Foto tidak terdeteksi")
                }
            }
        }
    }
}

