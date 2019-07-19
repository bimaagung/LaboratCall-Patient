package com.medis.laboratcall.Data

class DataPesanan{
    var item: String = ""
    var harga: String = ""

    constructor(){}

    constructor(item:String, harga:String)
    {
        this.item = item
        this.harga = harga
    }
}