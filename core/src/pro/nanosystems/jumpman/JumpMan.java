package pro.nanosystems.jumpman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;
import java.util.Random;

public class JumpMan extends ApplicationAdapter {
    private Logger logger;
    SpriteBatch batch;

    Texture background;
    Texture[] man;
    int manState = 0;
    int pause = 0;
    float gravity = 0.2f;
    float velocity = 0;
    int manY = 0, manX = 0;
    Texture coin;
    int startPoint;

    //  Texture coin;
    Random random;
    ArrayList<Integer> coinXs = new ArrayList<>();
    ArrayList<Integer> coinYs = new ArrayList<>();
     ArrayList<Rectangle> coinRectangles = new ArrayList<>();
    int coinCount = 0;

     int score = 0;

      Rectangle manRectangle;
     private Music music_level1,  win;

    @Override
    public void create() {
        logger = new Logger("Application Lifecycle", Logger.INFO);
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4];
        man[0] = new Texture("frame-1.png");
        man[1] = new Texture("frame-2.png");
        man[2] = new Texture("frame-3.png");
        man[3] = new Texture("frame-4.png");

        coin = new Texture("coin.png");


        manY = Gdx.graphics.getHeight() / 2 - man[0].getHeight() / 2;
        manX = Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2;

        //   coin = new Texture("coin.png");
        random = new Random();
        startPoint = Gdx.graphics.getWidth();

           music_level1 = Gdx.audio.newMusic(Gdx.files.internal("DustLoop.mp3"));
           music_level1.setLooping(true);

            win = Gdx.audio.newMusic(Gdx.files.internal("eat.mp3"));

        logger.info("Create");
    }

    @Override
    public void render() {
             music_level1.play();


        // coins
       /* if (coinCount < 100) {
            coinCount++;
        } else {
            coinCount = 0;
            makeCoin();
        }*/
        makeCoin();

        if (Gdx.input.justTouched()) { // -- on Touch Screen.
            velocity = -10;
        }
        velocity += gravity;
        manY = manY - (int) velocity;
        //manY -= velocity;

        if (manY <= 0) {
            manY = 0;
        }

        if (pause < 8) {// To delay move.
            pause++;
        } else {
            pause = 0;
            if (manState < 3) {
                manState++;
            } else manState = 0;
        }
        batch.begin();
        // draw scene
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

           coinRectangles.clear();
        for (int i = 0; i < coinXs.size(); i++) {
            batch.draw(coin, coinXs.get(i), coinYs.get(i));
            coinXs.set(i, coinXs.get(i) - 4);
            if (coinXs.get(i) < (coin.getWidth() * -1)){
                coinXs.remove(i);
                coinYs.remove(i);
            }

    coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
        }

        batch.draw(man[manState],
                manX,
                manY);
        //  batch.draw(coin,Gdx.graphics.getWidth() - 300 , 300);


               manRectangle = new Rectangle(manX,
                manY,
                man[manState].getWidth(),
                man[manState].getHeight());


        batch.end();

          scoreCoin();


        logger.info("Render");
    }

    @Override
    public void dispose() {
        batch.dispose();
           coin.dispose();
        background.dispose();
        for (int i = 0; i < 4; i++) {
            man[i].dispose();
        }


         music_level1.stop();
         music_level1.dispose();
         win.dispose();
        logger.info("Dispose");

    }

    public void makeCoin() {
        if (coinCount < 50) {
            coinCount++;
        } else {
            coinCount = 0;
            int height = random.nextInt(Gdx.graphics.getHeight());//* Gdx.graphics.getHeight();
            coinYs.add(height);
            coinXs.add(startPoint);
            logger.info(height + "");
        }

    }


   public void scoreCoin(){
        for (int i = 0; i < coinRectangles.size(); i++) {
            if (Intersector.overlaps(manRectangle, coinRectangles.get(i))) {
                score++;
                Gdx.app.log("Coin!", "Collision! Score:" + score);
                win.play();
                coinXs.remove(i);
                coinYs.remove(i);
                coinRectangles.remove(i);
            }
        }
    }
}
