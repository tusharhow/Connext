package com.tusharhow.connext.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.tusharhow.connext.models.ConnectionStatus
import com.tusharhow.connext.models.NetworkType

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val Context.currentConnectivityStatus: ConnectionStatus

    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as  ConnectivityManager
        return getCurrentConnectivityStatus(connectivityManager)
    }

private fun getCurrentConnectivityStatus(connectivityManager: ConnectivityManager): ConnectionStatus {
    val network = connectivityManager.activeNetwork ?: return ConnectionStatus.Disconnected

    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return ConnectionStatus.Disconnected

    return if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
        ConnectionStatus.Connected
    } else {
        ConnectionStatus.Disconnected
    }
}

fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val callback = NetworkCallback {connectionState -> trySend(connectionState)}

    val networkRequest  = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    val currentState = getCurrentConnectivityStatus(connectivityManager)
    trySend(currentState)

    awaitClose {connectivityManager.unregisterNetworkCallback(callback)}

}

private fun NetworkCallback (callback: (ConnectionStatus) -> Unit) : NetworkCallback{
    return object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionStatus.Connected)
        }
        override fun onLost(network:  Network) {
            callback(ConnectionStatus.Disconnected)
        }
    }
}

@Composable
fun connectivityStatus(): State<ConnectionStatus> {
    val context = LocalContext.current
    return produceState(initialValue = context.currentConnectivityStatus ){
        context.observeConnectivityAsFlow().collect{
            value = it
        }
    }
}


@Composable
fun CheckConnectivityStatus(
    connectedContent : @Composable () -> Unit = {},
    disconnectedContent : @Composable () -> Unit = {}
) {
    val connection = connectivityStatus()
    when (connection.value) {
        ConnectionStatus.Connected -> {
            connectedContent()
        }

        ConnectionStatus.Disconnected -> {
            disconnectedContent()
        }
    }
}

fun Context.getNetworkType(): NetworkType {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return NetworkType.NONE
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return NetworkType.NONE
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
        else -> NetworkType.NONE
    }
}
