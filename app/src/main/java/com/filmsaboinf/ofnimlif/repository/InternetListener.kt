package com.filmsaboinf.ofnimlif.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData

class InternetListener(context: Context) {

    private val connectiveManager by lazy {
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    }

    fun checkForInternet(): Boolean {
        val network = connectiveManager.activeNetwork ?: return false
        val activeNetwork =
            connectiveManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || activeNetwork.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ) -> true

            // else return false
            else -> false

        }
    }

}

