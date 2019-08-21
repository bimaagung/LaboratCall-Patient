package com.medis.laboratcall.Data

class DataNotifOncall(val id_analis:String, val id_pasien:String, var waktu:String, var lat_pasien:Double, var lng_pasien:Double,
                      var lat_analis:Double, var lng_analis:Double, val lat_origin:Double, val lng_origin:Double,
                      val lat_destination:Double, val lng_destination:Double, val konfirmasiOncall:String, val aktif_oncall:String)