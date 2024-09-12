package com.llyod.data.common

interface NetworkStatus {

    /**
     * Returns true if the device is connected to internet
     */
    fun isOnline() : Boolean
}