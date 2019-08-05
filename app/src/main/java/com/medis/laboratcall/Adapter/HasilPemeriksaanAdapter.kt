package com.medis.laboratcall.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medis.laboratcall.Data.DataHasilPemeriksaan
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.hasil_pemeriksaan_row.view.*


//VIDEO TUTORIALL BAGIAN 3

class HasilPemeriksaanAdapter(var context:Context, var list: ArrayList<DataHasilPemeriksaan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.hasil_pemeriksaan_row, parent, false)
        return HasilPemeriksaan(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HasilPemeriksaan).show(list[position].item_pemeriksaan,list[position].hasil_pemeriksaan, list[position].keterangan)
    }

    class HasilPemeriksaan(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun show(ip:String, hp:String, k:String)
        {
            itemView.tx_item.text = ip
            itemView.tx_hasil.text = hp
            itemView.tx_satuan.text = k

        }
    }
}

