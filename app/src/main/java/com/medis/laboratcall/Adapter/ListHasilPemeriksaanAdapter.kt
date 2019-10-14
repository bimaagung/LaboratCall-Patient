package com.medis.laboratcall.Adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medis.laboratcall.Connection
import com.medis.laboratcall.Data.DataListHasilPemeriksaan
import com.medis.laboratcall.HasilPemeriksaan
import kotlinx.android.synthetic.main.list_hasil_pemeriksaan_row.view.*
import android.app.Activity



class ListHasilPemeriksaanAdapter (var context: Context, var list: ArrayList<DataListHasilPemeriksaan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(com.medis.laboratcall.R.layout.list_hasil_pemeriksaan_row, parent, false)
        return ListHasilPemeriksaan(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListHasilPemeriksaan).show(list[position].tanggal, list[position].id_pemeriksaan, list[position].cek_hasil)
        holder.itemView.detailHasil.setOnClickListener{
            var i = Intent(context, HasilPemeriksaan::class.java)
            i.putExtra("tanggal",list[position].tanggal)
            i.putExtra("nama",list[position].nama)
            Connection.IdDetailPemeriksaan = list[position].id_pemeriksaan
            context.startActivity(i)
            (context as Activity).finish()
        }
    }

    class ListHasilPemeriksaan(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun show(tgl:String, id:String, cek_hasil:Boolean)
        {
            itemView.tx_tgl.text = tgl

            if(cek_hasil.equals(true))
            {
                itemView.detailHasil.visibility = View.VISIBLE
            }else{
                itemView.detailHasil.visibility = View.GONE
            }
        }
    }
}