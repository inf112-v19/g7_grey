package inf112.skeleton.app.libgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import inf112.skeleton.app.core.board.IBoard;
import inf112.skeleton.app.core.cards.IProgramCard;
import inf112.skeleton.app.core.interfaces.IAction;
import inf112.skeleton.app.libgdx.states.GameStateManager;
import inf112.skeleton.app.libgdx.states.MenuState;
import inf112.skeleton.app.server.API;
import inf112.skeleton.app.server.RemoteServerHandler;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Queue;

public class RobotDemo extends ApplicationAdapter {

    private SpriteBatch batch;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "RoboRally";
    private GameStateManager gsm = new GameStateManager();

    private API serverHandler = new RemoteServerHandler(new Actions());

    public RobotDemo() throws URISyntaxException {
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        gsm.push(new MenuState(gsm));
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // wipes the screen clear
        gsm.update(Gdx.graphics.getDeltaTime()); //Difference between the render times
        gsm.render(batch);
    }

    private class Actions implements IAction {

        @Override
        public void handleCards(List<IProgramCard> cards) {

        }

        @Override
        public void handleMoves(Queue<List<Move>> moves) {

        }

        @Override
        public void handleERROR(String message) {

        }

        @Override
        public void handleWARNING(String message) {

        }

        @Override
        public void handleINFO(String message) {

        }

        @Override
        public void handleBoard(IBoard board) {

        }

        @Override
        public void handleROOM(String roomId) {

        }
    }
}