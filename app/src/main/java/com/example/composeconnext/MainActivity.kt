package com.example.composeconnext


import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composeconnext.ui.theme.ComposeConnextTheme
import com.tusharhow.connext.helper.CheckConnectivityStatus
import com.tusharhow.connext.helper.connectivityStatus
import com.tusharhow.connext.helper.currentConnectivityStatus
import com.tusharhow.connext.helper.getNetworkType
import com.tusharhow.connext.models.ConnectionStatus
import com.tusharhow.connext.models.NetworkType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeConnextTheme {
                val context = LocalContext.current
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CheckConnectivityStatus(
                        connectedContent = {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Connected")
                            }
                        },
                        disconnectedContent = {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Disconnected")
                            }
                        }
                    )

                    val connection by connectivityStatus()
                    val isConnected = connection === ConnectionStatus.Connected

                     LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isConnected) Icons.Default.Close else Icons.Default.Close,
                                            contentDescription = "Wifi",
                                            tint = if (isConnected) Color.Green else Color.Red,
                                            modifier = Modifier.size(48.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = if (isConnected) "Connected to: ${context.getNetworkType()}" else "Disconnected",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                            items(10) {
                                Text("Item $it")
                            }
                        }
                     )


                }
            }
        }
    }
}





