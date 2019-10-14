package com.medis.laboratcall.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.medis.laboratcall.Data.DataDetailPromo
import com.medis.laboratcall.Data.DataItemPesanan
import com.medis.laboratcall.R
import kotlinx.android.synthetic.main.list_item_promo.view.*


class DetailPromoAdapter(var context:Context, var list: ArrayList<DataDetailPromo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        var v = LayoutInflater.from(context).inflate(R.layout.list_item_promo, parent, false)
        return DetailPromo(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DetailPromo).show(list[position].item_promo)

    }

    class DetailPromo(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        fun show(item_promo:String)
        {
            itemView.tx_item_promo.text = item_promo
        }

    }
}

