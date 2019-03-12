package inf112.skeleton.app.libgdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import inf112.skeleton.app.core.cards.IProgramCard;
import inf112.skeleton.app.core.cards.ProgramDeck;
import inf112.skeleton.app.libgdx.RobotDemo;
import inf112.skeleton.app.libgdx.VisualBoardLoader;


import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class RoundState extends State {
    private ProgramDeck deck;
    private boolean confirmed;
    private Stage stage;
    private Texture tileTexture;
    private Texture cardBackground;
    private IProgramCard[] availableRoundCard;
    private ArrayDeque<IProgramCard> chosenCards;
    private BitmapFont font;
    private GlyphLayout[] visualCardSequencing;
    private ArrayList<Integer> selectedCardPosX;
    private VisualBoardLoader visualBoardLoader;


    private CustomImageButton confirm;
    private CustomImageButton reset;
    private Texture boardBackground;
    public static final int CARD_WIDTH = 110 ;
    public static final int CARD_HEIGHT = 220;

    //TODO code quality, remove unnecessary stuff
    public RoundState(GameStateManager gsm) throws IOException {
        super(gsm);
        chosenCards = new ArrayDeque();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        selectedCardPosX = new ArrayList();
        visualBoardLoader = new VisualBoardLoader("src/main/resources/boards/sampleboard1.txt");

        initializeTextures();
        makeCardButtons();
        makeConfirmationButtons();
        makeDeck();
    }

    //Creates unique glyph layouts for each card
    public void createVisualCardSequencing() {
        visualCardSequencing = new GlyphLayout[5];
        for (int i = 0; i < 5; i++) {
            font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
            font.getData().setScale(0.5f, 0.5f);
            font.setColor(90f / 255f, 14f / 255f, 14f / 255f, 255f / 255f);
            String text = "" + (i + 1);
            visualCardSequencing[i] = new GlyphLayout(font, text);

        }
    }

    public void initializeTextures() {
        createVisualCardSequencing();
        cardBackground = new Texture(Gdx.files.internal("cards/Card_background1.png"));
        tileTexture = new Texture(Gdx.files.internal("tiles/empty_tile.png"));
        boardBackground = new Texture(Gdx.files.internal("boards/board_background_round.png"));

    }

    public void makeDeck() {
        deck = new ProgramDeck();
        this.drawForRound();
    }

    //Draw as in draw deck
    public void drawForRound() {
        availableRoundCard = new IProgramCard[9];
        for (int i = 0; i < 9; i++) {
            availableRoundCard[i] = deck.draw();
        }
    }

    @Override
    protected void handleInput() {
        handleNextStage();
    }

    // Handles criteria necessary for proceeding to the next stage
    public void handleNextStage() {
        if (chosenCards.size() == 5 && confirmed) {
            for (int i = 0; i < 5; i++)
                System.out.println(chosenCards.pop().priority());
            try {
                gsm.set(new PhaseState(gsm));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    ///Draws numbers 1-5 to the position of chosen card
    //TODO adjusting for duplicates
    //visualCardSequencing goes from 0 to 4 (included), rather than 1 to 5, hence the chosenCards.size() -1
    public void handleVisualSelection() {
        if (chosenCards.size() > 0 && chosenCards.size() < 6)
            for (int i = 0; i <chosenCards.size() ; i++) {
                drawSelectedNumber(chosenCards.size() - 1);
            }
    }

    //Draw as in paint
    //Draws the currently selected card's number on board
    private void drawSelectedNumber(int selectedNum) {
        BitmapFontCache bc = new BitmapFontCache(font);
        float yPos = (CARD_HEIGHT - 65 + visualCardSequencing[selectedNum].height);
        for (int i = 0; i <selectedCardPosX.size() ; i++) {
            float xPos = ((CARD_WIDTH * selectedCardPosX.get(i) + CARD_WIDTH / 4) + visualCardSequencing[i].width / 2 + CARD_WIDTH /2);
            bc.addText(visualCardSequencing[i], xPos, yPos);
        }
        bc.draw(stage.getBatch());
    }

    public void disposeFonts() {
        font.dispose();
        selectedCardPosX.clear();
        createVisualCardSequencing();
    }

    public void makeConfirmationButtons() {
        confirm = new CustomImageButton("buttons/Confirm.png", "buttons/Confirm.png", RobotDemo.WIDTH - 250, CARD_WIDTH / 2 + 50, 100, 50);
        reset = new CustomImageButton("buttons/Reset.png", "buttons/Reset.png", RobotDemo.WIDTH - 250, 30, 100, 50);
        confirm.getButton().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (chosenCards.size() == 5)
                    confirmed = true;
                return true;
            }
        });

        reset.getButton().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                chosenCards.clear();
                disposeFonts();
                confirmed = false;
                createVisualCardSequencing();
                return true;
            }
        });

        stage.addActor(confirm.getButton());
        stage.addActor(reset.getButton());
    }


    //Each card button has a listener for user input
    public void makeCardButtons() {
        CustomImageButton[] cards = new CustomImageButton[9];

        for (int i = 0; i < 9; i++) {
            int x = CARD_WIDTH * i + CARD_WIDTH / 4;
            int y = 18;
            int width = CARD_WIDTH - 30;
            int height = CARD_HEIGHT - 60;
            cards[i] = new CustomImageButton("cards/card.png", "cards/card.png", x, y, width, height);

            final int finalI = i;
            cards[i].getButton().addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (!chosenCards.contains(availableRoundCard[finalI]) && chosenCards.size() < 5) {
                        chosenCards.add(availableRoundCard[finalI]);
                        selectedCardPosX.add(finalI);
                    }

                    return true;
                }
            });
            stage.addActor(cards[i].getButton());
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        stage.act();
        stage.getBatch().begin();
        this.renderBoard((SpriteBatch) stage.getBatch());
        handleVisualSelection();
        stage.getBatch().end();
        stage.draw();
    }

    public void renderBoard(SpriteBatch sb) {
        int height = (RobotDemo.HEIGHT - cardBackground.getHeight()) / 10;
        stage.getBatch().draw(boardBackground, 0, 0);
        int temp = visualBoardLoader.getTileWidthHeight() * 10 / 2;
        visualBoardLoader.renderBoardCustomSize(sb, RobotDemo.WIDTH / 2 - temp, cardBackground.getHeight(), height, height);
        stage.getBatch().draw(cardBackground, 0, 0);

    }

    @Override
    public void dispose() {
        confirm.getTexture().dispose();
        reset.getTexture().dispose();
        cardBackground.dispose();
        tileTexture.dispose();

    }
}