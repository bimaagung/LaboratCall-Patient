package com.medis.laboratcall.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medis.laboratcall.Data.DataHasilPemeriksaan
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.hasil_pemeriksaan_row.view.*


class HasilPemeriksaanAdapter(var context:Context, var list: ArrayList<DataHasilPemeriksaan>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.hasil_pemeriksaan_row, parent, false)
        return HasilPemeriksaan(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HasilPemeriksaan).show(list[position].jenis_pemeriksaan,list[position].item_pemeriksaan,
                                          list[position].hasil_pemeriksaan, list[position].keterangan, list[position].stabil, list[position].kategori)
    }

    class HasilPemeriksaan(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun show(jp:String, ip:String, hp:String, k:String, st:String, kg:String)
        {
            itemView.tx_item.text = ip
            itemView.tx_hasil.text = hp
            itemView.tx_jenis_pemeriksaan.text = jp
            itemView.tx_kategori.text = kg

            //logika string -
            if (k.equals("-"))
            {
                itemView.tx_satuan.text = " "
            }else{
                itemView.tx_satuan.text = k
            }

            //visibility kateogri
            if(kg.equals(" "))
            {
                itemView.tx_kategori.visibility = View.GONE
            }else{
                itemView.tx_kategori.visibility = View.VISIBLE
            }

            //Membedakan warna label
            if(jp.equals("Hematologi"))
            {
                itemView.layout_label.setBackgroundColor(Color.rgb(198, 255, 130))
                itemView.tx_jenis_pemeriksaan.setTextColor(Color.rgb(124, 179, 66))
                itemView.tx_kategori.setTextColor(Color.rgb(124, 179, 66))
            }else if(jp.equals("Kimia Klinik")){
                itemView.layout_label.setBackgroundColor(Color.rgb(255, 229, 127))
                itemView.tx_jenis_pemeriksaan.setTextColor(Color.rgb(251, 140, 0))
                itemView.tx_kategori.setTextColor(Color.rgb(251, 140, 0))
            }else if(jp.equals("Sereologi")){
                itemView.layout_label.setBackgroundColor(Color.rgb(132, 255, 255))
                itemView.tx_jenis_pemeriksaan.setTextColor(Color.rgb(0, 172, 193))
                itemView.tx_kategori.setTextColor(Color.rgb(0, 172, 193))
            }else if(jp.equals("Urine")){
                itemView.layout_label.setBackgroundColor(Color.rgb(219, 199, 253))
                itemView.tx_jenis_pemeriksaan.setTextColor(Color.rgb(94, 53, 177))
                itemView.tx_kategori.setTextColor(Color.rgb(94, 53, 177))
            }else if(jp.equals("Urine")){
                itemView.layout_label.setBackgroundColor(Color.rgb(248, 192, 187))
                itemView.tx_jenis_pemeriksaan.setTextColor(Color.rgb(229, 57, 53))
                itemView.tx_kategori.setTextColor(Color.rgb(229, 57, 53))
            }


            //Logika Stabil Hasil
            if(st.equals("1"))
            {
                itemView.stabil_ok.visibility = View.VISIBLE
                itemView.stabil_bad_up.visibility = View.GONE
                itemView.stabil_bad_down.visibility = View.GONE
                itemView.tx_hasil.setTypeface(Typeface.DEFAULT)
            }
            else if(st.equals("0"))
            {
                itemView.stabil_ok.visibility = View.GONE
                itemView.stabil_bad_up.visibility = View.GONE
                itemView.stabil_bad_down.visibility = View.VISIBLE
                itemView.tx_hasil.setTypeface(Typeface.DEFAULT_BOLD)
            }
            else
            {
                itemView.stabil_ok.visibility = View.GONE
                itemView.stabil_bad_up.visibility = View.VISIBLE
                itemView.stabil_bad_down.visibility = View.GONE
                itemView.tx_hasil.setTypeface(Typeface.DEFAULT_BOLD)
            }

        }
    }
}

