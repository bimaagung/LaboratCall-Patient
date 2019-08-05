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
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.list_hasil_pemeriksaan_row.view.*

class ListHasilPemeriksaanAdapter (var context: Context, var list: ArrayList<DataListHasilPemeriksaan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.list_hasil_pemeriksaan_row, parent, false)
        return ListHasilPemeriksaan(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListHasilPemeriksaan).show(list[position].tanggal, list[position].id_pemeriksaan)
        holder.itemView.detailHasil.setOnClickListener{
            var i = Intent(context, HasilPemeriksaan::class.java)
            Connection.IdDetailPemeriksaan = list[position].id_pemeriksaan
            context.startActivity(i)
        }
    }

    class ListHasilPemeriksaan(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun show(tgl:String, id:String)
        {
            itemView.tx_tgl.text = tgl
        }
    }
}