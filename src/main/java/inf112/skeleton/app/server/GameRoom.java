package inf112.skeleton.app.server;

import com.google.gson.Gson;
import inf112.skeleton.app.core.board.Board;
import inf112.skeleton.app.core.cards.IProgramCard;
import inf112.skeleton.app.core.player.Player;
import org.java_websocket.WebSocket;

import java.util.ArrayDeque;
import java.util.HashMap;

public class GameRoom {

    private static boolean selectionDone;
    private String roomId;
    private Board board;
    private static HashMap<Player, WebSocket> connections; //TODO update to keep the same players
    private static HashMap<WebSocket, ArrayDeque<IProgramCard>> collectiveCards;
    private int totalConnections;

    public GameRoom(String roomId) {
        collectiveCards = new HashMap<>();
        connections = new HashMap<>();
        selectionDone = false;
        this.roomId = roomId;
        board = new Board("empty", 10, 10);
    }


    public HashMap<Player, ArrayDeque<IProgramCard>> getResponseMap() {
        HashMap<Player, ArrayDeque<IProgramCard>> finalMap = new HashMap<>();
        for (WebSocket key : connections.values())
            finalMap.put(getPlayer(key), collectiveCards.get(key));
        return finalMap;
    }

    private Player getPlayer(WebSocket conn) {
        for (Player player : connections.keySet())
            if (connections.get(player).equals(conn))
                return player;
        return null;
    }

    public boolean getStatus() {
        return selectionDone;
    }

    public static HashMap<WebSocket, ArrayDeque<IProgramCard>> getCollectiveCards() {
        return collectiveCards;
    }

    public String getRoomId() {
        return roomId;
    }


    // client sends cards to server.
    //server sends list of players and their chosen cards to the client
    //client executes animation for each player
    //board is updated at server
    //repeat
    public void addChosenCards(WebSocket web, ArrayDeque<IProgramCard> cards) {
        collectiveCards.put(web, cards);
        if (collectiveCards.keySet().size() == totalConnections)
            selectionDone = true;
    }

    public void updateBoard(Board board) {
        this.board = board;
        System.out.println("board updated");
    }

    public void clearCards() {
        for (Player p : connections.keySet())
            p.removeAllCards();
        collectiveCards.clear();
    }

    public void setTotalConnections(int n) {
        totalConnections = n;
    }

    public void setStatus(boolean b) {
        this.selectionDone = false;
    }

    public void addPlayer(WebSocket web) {
        System.out.println("adding player: " + web.getRemoteSocketAddress().toString());
        connections.put(new Player(web.getRemoteSocketAddress().toString()), web); // temporary, until startGame(); is in use
    }
}
