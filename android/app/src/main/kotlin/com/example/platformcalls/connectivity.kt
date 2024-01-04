package com.example.platformcalls

import android.net.ConnectivityManager




class Connectivity {
    val CONNECTIVITY_NONE = "none"
    val CONNECTIVITY_WIFI = "wifi"
    val CONNECTIVITY_MOBILE = "mobile"
    val CONNECTIVITY_ETHERNET = "ethernet"
    val CONNECTIVITY_BLUETOOTH = "bluetooth"
    val CONNECTIVITY_VPN = "vpn"
    private val connectivityManager: ConnectivityManager? = null
}

data class BuildInfoModelTestModel(
        val id: Int,
        val name: String,
        val build1: String,
        val build2: String,


)
