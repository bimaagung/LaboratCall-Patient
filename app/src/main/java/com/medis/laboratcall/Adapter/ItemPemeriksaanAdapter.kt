package com.medis.laboratcall.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medis.laboratcall.BarChartHasil
import com.medis.laboratcall.Data.DataHasilPemeriksaan
import com.medis.laboratcall.Data.DataItemPemeriksaan
import com.medis.laboratcall.Data.DataPromo
import com.medis.laboratcall.DetailPromo
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.hasil_pemeriksaan_row.view.*
import kotlinx.android.synthetic.main.list_daftar_promo.view.*
import kotlinx.android.synthetic.main.list_item_pemeriksaan_row.view.*


//VIDEO TUTORIALL BAGIAN 3

class ItemPemeriksaanAdapter(var context:Context, var list: ArrayList<DataItemPemeriksaan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.list_item_pemeriksaan_row, parent, false)
        return ItemPemeriksaan(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemPemeriksaan).show(list[position].jenis_pemeriksaan,list[position].item_pemeriksaan, list[position].kategori, list[position].type_data_float)

        holder.itemView.cv_list_item_pemeriksaan.setOnClickListener{
            var i = Intent(context, BarChartHasil::class.java)
            i.putExtra("id_item", list[position].id_item)
            i.putExtra("jenis_pemeriksaan", list[position].jenis_pemeriksaan)
            i.putExtra("item_pemeriksaan", list[position].item_pemeriksaan)
            i.putExtra("nl", list[position].nl)
            i.putExtra("np", list[position].np)
            i.putExtra("keterangan", list[position].keterangan)
            i.putExtra("kategori", list[position].kategori)
            context.startActivity(i)
            (context as Activity).finish()
        }
    }

    class ItemPemeriksaan(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun show(jenis_pemeriksaan:String,item_pemeriksaan:String, kategori:String, type_data_float:Boolean)
        {
            itemView.tx_list_item_pemeriksaan.text = item_pemeriksaan
            itemView.tx_list_jenis_pemeriksaan.text = jenis_pemeriksaan
            itemView.tx_list_kategori.text = kategori

            //Cek tampil apabila sudah ada hasil
            if(type_data_float.equals(true))
            {
                itemView.cv_list_item_pemeriksaan.visibility = View.VISIBLE
            }else{
                itemView.cv_list_item_pemeriksaan.visibility = View.GONE
            }
        }
    }
}

