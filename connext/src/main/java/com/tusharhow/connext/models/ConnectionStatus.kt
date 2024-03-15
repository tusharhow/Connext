package com.tusharhow.connext.models

sealed class ConnectionStatus {
    data object Connected : ConnectionStatus()
    data object Disconnected : ConnectionStatus()
}