commitpackage com.example.docxgenerator.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class DocxWebSocketServer extends WebSocketServer {
    private Map<String, WebSocket> documents = new HashMap<>();

    public DocxWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // Handle new connection
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // Handle connection close
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // Handle incoming messages
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // Handle errors
    }

    @Override
    public void onStart() {
        // Server started
    }
}

