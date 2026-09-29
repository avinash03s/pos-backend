package game.engine;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    // Thread-safe maps player sessions aur positions ko store karne ke liye
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PlayerState> players = new ConcurrentHashMap<>();

    // Thread-safe Inbox jahan saare network threads player inputs ko drop karenge
    private final ConcurrentLinkedQueue<String> inputQueue = new ConcurrentLinkedQueue<>();

    public GameWebSocketHandler() {
        // BACKGROUND THREAD: Ek separate thread jo bina ruke har 33ms (30 FPS) par tick() chalayega
        ScheduledExecutorService gameLoop = Executors.newSingleThreadScheduledExecutor();
        gameLoop.scheduleAtFixedRate(this::tick, 0, 33, TimeUnit.MILLISECONDS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String id = session.getId();
        sessions.put(id, session);
        // Naye player ko screen par random coordinates (X, Y) par spawn karna
        players.put(id, new PlayerState(100 + (int)(Math.random() * 200), 100 + (int)(Math.random() * 200)));
        System.out.println("New Friend Joined! Player ID: " + id);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String id = session.getId();
        sessions.remove(id);
        players.remove(id);
        System.out.println("Friend Left! Player ID: " + id);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // NETWORK THREAD: Input aate hi use queue mein daal kar turant free ho jata hai
        String payload = session.getId() + ":" + message.getPayload();
        inputQueue.add(payload);
    }

    // GAME LOOP TIMELINE BLOCK (Isme koi network thread interfere nahi kar sakta)
    private void tick() {
        // Phase 1: Queue se saare inputs nikal kar sequential order mein process karna
        while (!inputQueue.isEmpty()) {
            String rawInput = inputQueue.poll();
            if (rawInput == null) continue;

            String[] parts = rawInput.split(":");
            String playerId = parts[0];
            String command = parts[1];

            PlayerState player = players.get(playerId);
            if (player != null) {
                if (command.equals("UP")) player.y -= 8;
                if (command.equals("DOWN")) player.y += 8;
                if (command.equals("LEFT")) player.x -= 8;
                if (command.equals("RIGHT")) player.x += 8;
            }
        }

        // Phase 2: Saare players ki nayi locations ka Text (JSON) snapshot banana
        String jsonState = buildStateJson();

        // Phase 3: Naya snapshot sabhi connected browsers ko network par wapas bhej dena
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonState));
                } catch (IOException ignored) {}
            }
        }
    }

    private String buildStateJson() {
        StringBuilder json = new StringBuilder("{");
        players.forEach((id, state) -> json.append(String.format("\"%s\":{\"x\":%d,\"y\":%d},", id, state.x, state.y)));
        if (!players.isEmpty()) json.setLength(json.length() - 1); // Extra trailing comma hatane ke liye
        json.append("}");
        return json.toString();
    }

    private static class PlayerState {
        int x, y;
        PlayerState(int x, int y) { this.x = x; this.y = y; }
    }
}
