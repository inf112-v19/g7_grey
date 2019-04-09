package inf112.skeleton.app.core.interfaces;

import inf112.skeleton.app.core.board.IBoard;
import inf112.skeleton.app.core.cards.IDeck;
import inf112.skeleton.app.core.cards.IProgramCard;
import inf112.skeleton.app.core.player.IPlayer;
import inf112.skeleton.app.libgdx.Move;
import inf112.skeleton.app.server.API;

import java.util.List;
import java.util.Queue;

/**
 * Handler for server responses. {@link API} implementation
 * should call these methods when it receives a response
 * from the server.
 *
 * Client object should have a non-static, internal
 * implementation of this interface that handles the server
 * responses and changes the client state.
 */
public interface IAction {

    void handleERROR(String message);

    void handleWARNING(String message);

    void handleINFO(String message);

    void handleROOM(String roomId);

    void handleCards(List<IProgramCard> cards);

    void handleBoard(IBoard board);

    void handleMoves(Queue<List<Move>> moves);

    void handlePlayer(IPlayer player);
}
