package com.example.docxgenerator

import okhttp3.*
import org.json.JSONObject

class WebSocketClient {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connect(documentId: String, userId: String) {
        val request = Request.Builder().url("ws://your-server-url:8080").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // Connection established
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val data = JSONObject(text)
                val receivedDocumentId = data.getString("documentId")
                val receivedUserId = data.getString("userId")
                val action = data.getString("action")
                val content = data.getString("content")

                // Update the document UI based on the received message
                updateDocumentUI(receivedDocumentId, receivedUserId, action, content)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // Connection closing
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Connection failed
            }
        })
    }

    fun sendMessage(documentId: String, userId: String, action: String, content: String) {
        val message = JSONObject()
        message.put("documentId", documentId)
        message.put("userId", userId)
        message.put("action", action)
        message.put("content", content)
        webSocket.send(message.toString())
    }

    private fun updateDocumentUI(documentId: String, userId: String, action: String, content: String) {
        // Update the document UI based on the received message
    }
}