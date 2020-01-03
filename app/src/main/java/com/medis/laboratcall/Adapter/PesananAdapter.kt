package com.medis.laboratcall.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.BaseAdapter
import android.widget.TextView
import com.medis.laboratcall.Data.DataPesanan
import com.medis.laboratcall.R
import java.text.DecimalFormat

class PesananAdapter(private var activity: Activity, private var items: ArrayList<DataPesanan>): BaseAdapter(){

    private class ViewHolder(row: View?) {
        var txtName: TextView? = null
        var txtComment: TextView? = null

        init {
            this.txtName = row?.findViewById<TextView>(R.id.tx_item)
            this.txtComment = row?.findViewById<TextView>(R.id.tx_harga)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_pemeriksaan_row, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var dataPesanan = items[position]
        viewHolder.txtName?.text = dataPesanan.item
        var formatterHarga = DecimalFormat("#,###")
        var daftar_harga = formatterHarga.format(dataPesanan.harga.toInt())

        if(daftar_harga.toString().equals("1"))
        {
            viewHolder.txtComment?.text = "Darah Rutin/50.000"
        }
        else if(daftar_harga.toString().equals("2"))
        {
            viewHolder.txtComment?.text = "Gula Darah/20.000"
        }
        else if(daftar_harga.toString().equals("3"))
        {
            viewHolder.txtComment?.text = "HDL/LDL / 2.5000"
        }
        else if(daftar_harga.toString().equals("4"))
        {
            viewHolder.txtComment?.text = "Widal/40.000"
        }
        else if(daftar_harga.toString().equals("5"))
        {
            viewHolder.txtComment?.text = "Urin Rutin/25.000"
        }
        else if(daftar_harga.toString().equals("6"))
        {
            viewHolder.txtComment?.text = "Feces Lengkap/20.000"
        }
        else
        {
            viewHolder.txtComment?.text = "Rp. "+daftar_harga.toString()
        }

        return view as View
    }

    override fun getItem(i: Int): Any {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

}