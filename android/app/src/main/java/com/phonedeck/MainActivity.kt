package com.phonedeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import okhttp3.*
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PhoneDeckApp() }
    }
}

@Composable
fun PhoneDeckApp() {
    var ip by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Disconnected") }
    var socket by remember { mutableStateOf<WebSocket?>(null) }

    val commands = listOf(
        "play_pause" to "Play / Pause",
        "cut" to "Cut",
        "undo" to "Undo"
    )

    fun connect() {
        val cleanIp = ip.trim()
        if (cleanIp.isEmpty()) return

        socket?.close(1000, "Reconnect")
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://$cleanIp:8765")
            .build()

        socket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                status = "Connected"
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                status = "Disconnected"
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                status = "Connection failed"
            }
        })
    }

    fun send(id: String, label: String) {
        val payload = JSONObject()
            .put("type", "command")
            .put("id", id)
            .put("label", label)
        socket?.send(payload.toString())
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PhoneDeck")

        OutlinedTextField(
            value = ip,
            onValueChange = { ip = it },
            label = { Text("Mac IP address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { connect() }) { Text("Connect") }
            Text(status, modifier = Modifier.padding(top = 12.dp))
        }

        commands.forEach { (id, label) ->
            Button(
                onClick = { send(id, label) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(label)
            }
        }
    }
}
