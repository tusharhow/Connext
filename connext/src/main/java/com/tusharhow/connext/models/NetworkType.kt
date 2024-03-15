package com.tusharhow.connext.models

sealed class NetworkType {
    data object WIFI : NetworkType()
    data object CELLULAR : NetworkType()
    data object NONE : NetworkType()

}