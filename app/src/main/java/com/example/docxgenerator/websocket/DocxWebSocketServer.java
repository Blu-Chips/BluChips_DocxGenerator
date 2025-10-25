package com.example.docxgenerator.websocket;

import android.util.Log;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class DocxWebSocketServer extends WebSocketServer {
    private static final String TAG = "DocxWebSocketServer";
    private Set<WebSocket> clients = new HashSet<>();
    private ServerStatusListener statusListener;

    public interface ServerStatusListener {
        void onClientCountChanged(int count);
        void onMessageReceived(String message);
        void onError(String error);
    }

    public DocxWebSocketServer(int port, ServerStatusListener listener) {
        super(new InetSocketAddress(port));
        this.statusListener = listener;
        setReuseAddr(true);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        Log.d(TAG, "New client connected. Total clients: " + clients.size());
        if (statusListener != null) {
            statusListener.onClientCountChanged(clients.size());
        }
        
        // Send welcome message
        try {
            JSONObject welcome = new JSONObject();
            welcome.put("type", "welcome");
            welcome.put("message", "Connected to BluChips Document Server");
            conn.send(welcome.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error sending welcome message", e);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        Log.d(TAG, "Client disconnected. Total clients: " + clients.size());
        if (statusListener != null) {
            statusListener.onClientCountChanged(clients.size());
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, "Message received: " + message);
        if (statusListener != null) {
            statusListener.onMessageReceived(message);
        }
        
        // Broadcast to all other clients
        broadcast(message, conn);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.e(TAG, "WebSocket error", ex);
        if (statusListener != null) {
            statusListener.onError(ex.getMessage());
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "WebSocket server started successfully");
    }

    public void broadcast(String message, WebSocket sender) {
        for (WebSocket client : clients) {
            if (client != sender && client.isOpen()) {
                client.send(message);
            }
        }
    }

    public void broadcastDocumentUpdate(int docId, String title, String content) {
        try {
            JSONObject update = new JSONObject();
            update.put("type", "document_update");
            update.put("docId", docId);
            update.put("title", title);
            update.put("content", content);
            update.put("timestamp", System.currentTimeMillis());
            
            String message = update.toString();
            for (WebSocket client : clients) {
                if (client.isOpen()) {
                    client.send(message);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error broadcasting document update", e);
        }
    }

    public int getClientCount() {
        return clients.size();
    }
}

