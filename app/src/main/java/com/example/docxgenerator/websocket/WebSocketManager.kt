package com.example.docxgenerator.websocket

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.InetAddress

class WebSocketManager(private val context: Context) {
    private var server: DocxWebSocketServer? = null
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning
    
    private val _clientCount = MutableStateFlow(0)
    val clientCount: StateFlow<Int> = _clientCount
    
    private val _serverIp = MutableStateFlow("")
    val serverIp: StateFlow<String> = _serverIp
    
    private val _serverPort = MutableStateFlow(8080)
    val serverPort: StateFlow<Int> = _serverPort
    
    companion object {
        private const val TAG = "WebSocketManager"
        private const val DEFAULT_PORT = 8080
        
        @Volatile
        private var instance: WebSocketManager? = null
        
        fun getInstance(context: Context): WebSocketManager {
            return instance ?: synchronized(this) {
                instance ?: WebSocketManager(context.applicationContext).also { instance = it }
            }
        }
    }
    
    private val statusListener = object : DocxWebSocketServer.ServerStatusListener {
        override fun onClientCountChanged(count: Int) {
            _clientCount.value = count
            Log.d(TAG, "Client count changed: $count")
        }
        
        override fun onMessageReceived(message: String) {
            Log.d(TAG, "Message received: $message")
        }
        
        override fun onError(error: String) {
            Log.e(TAG, "Server error: $error")
        }
    }
    
    fun startServer(port: Int = DEFAULT_PORT): Boolean {
        return try {
            if (_isRunning.value) {
                Log.w(TAG, "Server already running")
                return true
            }
            
            server = DocxWebSocketServer(port, statusListener)
            server?.start()
            
            _serverPort.value = port
            _serverIp.value = getLocalIpAddress()
            _isRunning.value = true
            
            Log.d(TAG, "WebSocket server started on ${_serverIp.value}:$port")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start server", e)
            false
        }
    }
    
    fun stopServer() {
        try {
            server?.stop(1000)
            server = null
            _isRunning.value = false
            _clientCount.value = 0
            Log.d(TAG, "WebSocket server stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping server", e)
        }
    }
    
    fun broadcastDocumentUpdate(docId: Int, title: String, content: String) {
        server?.broadcastDocumentUpdate(docId, title, content)
    }
    
    private fun getLocalIpAddress(): String {
        return try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val ipAddress = wifiManager.connectionInfo.ipAddress
            
            if (ipAddress != 0) {
                String.format(
                    "%d.%d.%d.%d",
                    ipAddress and 0xff,
                    ipAddress shr 8 and 0xff,
                    ipAddress shr 16 and 0xff,
                    ipAddress shr 24 and 0xff
                )
            } else {
                "Not Connected"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting IP address", e)
            "Unknown"
        }
    }
}

