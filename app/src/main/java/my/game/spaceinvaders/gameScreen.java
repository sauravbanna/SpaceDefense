package my.game.spaceinvaders;


import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import my.game.spaceinvaders.R;
import my.game.spaceinvaders.databinding.ActivityGameScreenBinding;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */


public class gameScreen extends AppCompatActivity {

    // gameObject is new gameObject(x, y)
    // interp: x is x coordinate of gameObject
    //         y is y coordinate of gameObject
    class gameObject {

        int POS_X;
        int POS_Y;

        // Constructor for gameObject with coordinates
        public gameObject(int x, int y) {
            this.POS_X = x;
            this.POS_Y = y;
        }

        // Function -> getX
        // Void -> int
        // returns the current x coordinate of gameObject
        int getX() {
            return POS_X;
        }

        // Function -> getY
        // Void -> int
        // returns the current y coordinate of gameObject
        int getY() {
            return POS_Y;
        }

        // Function -> setX
        // int -> Void
        // sets the gameObject's x coordinate to given int
        void setX(int x) {
            POS_X = x;
        }

        // Function -> setY
        // int -> Void
        // sets the gameObject's y coordinate to given int
        void setY(int y) {
            POS_Y = y;
        }

        // Function -> setXY
        // int int -> Void
        // sets the gameObject's x and y coordinate to given int values
        void setXY(int x, int y) {
            POS_X = x;
            POS_Y = y;
        }
    }

    // Spaceship is new Spaceship(x, y);
    // interp. x is the x-coordinate of Spaceship
    //         y is the y-coordinate of Spaceship
    class Spaceship extends gameObject {

        // Constructor for Spaceship with coordinates
        public Spaceship(int x, int y) {
            super(x, y);
        }

    }

    // Alien is new Alien(x, y, alive);
    // interp. x is the x-coordinate of Alien
    //         y is the y-coordinate of Alien
    //         alive is true if alien is alive, false if not
    class Alien extends gameObject {

        int health = 100;

        // Constructor for Alien with coordinates and health
        public Alien(int x, int y, int health) {
            super(x, y);
            this.health = health;
        }

        // Constructor for Alien with only coordinate parameters
        public Alien(int x, int y) {
            super(x, y);
        }

        // Function -> getHealth
        // Void -> int
        // returns the current health of Alien
        int getHealth() {
            return this.health;
        }

        // Function -> setHealth
        // int -> Void
        // sets the Alien's health to given int value
        void setHealth(int health) {
            this.health = health;
        }

    }

    // Explosion is new Explosion(x, y, state)
    // interp. x is x coordinate of explosion on screen
    //         y is y coordinate of explosion on screen
    //         state is current state of explosion process
    // CONSTRAINT: state has to be in [1, 8], 1 is start of explosion, 8 is when explosion is done
    class Explosion extends gameObject {

        int State = 1;

        // Constructor for Explosion with coordinates and state
        public Explosion(int x, int y, int state) {
            super(x, y);
            this.State = state;
        }

        // Constructor for Explosion with coordinates
        public Explosion(int x, int y) {
            super(x, y);
        }

        // Function -> getState
        // Void -> int
        // returns the current state of Explosion
        int getState() {
            return this.State;
        }

        // Function -> setState
        // int -> Void
        // sets the Explosion's state to given int
        void setState(int state) {
            this.State = state;
        }
    }

    // Bullet is new Bullet(x, y)
    // interp. x is x coordinate of bullet
    //         y is y coordinate of bullet
    class Bullet extends gameObject {

        // Constructor for Bullet with coordinates
        public Bullet(int x, int y) {
            super(x, y);
        }
    }

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    // Spaceship and list of game objects
    public static Spaceship spaceship;
    public static List<Alien> aliens = new ArrayList<Alien>();
    public static List<Bullet> bullets = new ArrayList<Bullet>();
    public static List<Explosion> explosions = new ArrayList<Explosion>();

    // images used in game
    BitmapDrawable spaceship_img;
    BitmapDrawable alien_img;
    BitmapDrawable bullet_img;
    BitmapDrawable explosion1_img;
    BitmapDrawable explosion2_img;
    BitmapDrawable explosion3_img;
    BitmapDrawable explosion4_img;
    BitmapDrawable explosion5_img;
    BitmapDrawable explosion6_img;
    BitmapDrawable explosion7_img;
    BitmapDrawable explosion8_img;
    BitmapDrawable pauseButton_img;
    BitmapDrawable pauseScreen_img;
    BitmapDrawable line_img;
    BitmapDrawable countdown1_img;
    BitmapDrawable countdown2_img;
    BitmapDrawable countdown3_img;
    BitmapDrawable background;
    BitmapDrawable play_img;

    // maps to access images and sounds with indexes
    Map<Integer, BitmapDrawable> explosion_map = new HashMap<>();
    Map<Integer, BitmapDrawable> countdownImages_map = new HashMap<>();
    Map<Integer, Integer> explosionSounds_map = new HashMap<>();

    // ImageViews and other Views used in game
    ImageView spaceShip_ImageView;
    ImageView Alien_ImageView;
    ImageView Bullet_ImageView;
    ImageView Explosion_ImageView;
    ImageView pauseScreen_ImageView;
    ImageView pauseButton_ImageView;
    ImageView line_ImageView;
    ImageView cornerPause_ImageView;
    ImageView exit_ImageView;
    View Background;
    View gameOver;
    TextView yourScore_Text;
    TextView highScore_Text;
    TextView score_Text;

    // File IDs of sounds used in game
    int BULLET_SOUND = R.raw.shoot;
    int EXPLOSION_1 = R.raw.boom1;
    int EXPLOSION_2 = R.raw.boom2;
    int EXPLOSION_3 = R.raw.boom3;

    // initial x and y positions of game Objects
    int SPACESHIP_INIT_X;
    int SPACESHIP_INIT_Y;
    int MIN_SPACESHIP;
    int MAX_SPACESHIP;
    int BULLET_INIT_Y;

    // Ratios for scaling image sizes and positions
    double MAIN_SCALE = 0.06;
    double SPACESHIP_SCALE = 0.06;
    double BULLET_SCALE_H = 0.01;
    double BULLET_SCALE_W = 0.005;
    double DEFAULT_SCALE = 1.0;
    double RIGHT_LEFT_BARS = 0.025;
    double LINE_SCALE = 0.005;
    double LINE_SCALE_H = 0.1;
    double PAUSE_SCALE = 0.2;
    double CORNER_PAUSE_SCALE = 0.08;

    // Timers for different game activities
    Timer timer = new Timer();
    Timer paintTimer = new Timer();
    Timer explosionTimer = new Timer();
    Timer countdownTimer = new Timer();
    Timer alienTimer = new Timer();

    // Time (in milliseconds) after which game activities should be repeated
    int ALIEN_SPAWN_RATE = 3000;
    int EXPLOSION_FRAME_RATE = 100;
    int PAINT_RATE = 50;
    double ALIEN_SPAWN_MULTIPLIER = 1.2;

    // Velocities and scales for velocities for aliens and bullets
    int ALIEN_V;
    int BULLET_V;
    double ALIEN_V_SCALE = 0.01;
    double BULLET_V_SCALE = 0.04;

    // Maximum height a bullet can reach
    int BULLET_HEIGHT_LIMIT;

    // Final state (frame) of an explosion
    int EXPLOSION_FINAL_STATE = 8;

    // Information to keep track of game's current score and highscore
    int score = 0;
    int default_highscore = 0;
    int highscore;
    String score_key = "hscore";

    int TUTORIAL_DURATION = 3500;

    // Game State Booleans
    Boolean deathFlag = false;
    Boolean gameOverFlag = false;
    Boolean pauseFlag = false;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(WindowInsets.Type.systemBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private ActivityGameScreenBinding binding;

    // Function -> init
    // Void -> Void
    // initialises the game
    private void init() {
        initImages();
        initImageViews();
        initObjects();
        startTimer();
        paint();
    }

    // Function -> initImages
    // Void -> Void
    // initialises the images used in game
    private void initImages() {
        // game objects
        spaceship_img = (BitmapDrawable) this.getResources().getDrawable(R.drawable.spaceship);
        alien_img = startScreen.resize((BitmapDrawable) this.getResources().getDrawable(R.drawable.alien), MAIN_SCALE, MAIN_SCALE, this);
        bullet_img = startScreen.resize((BitmapDrawable) this.getResources().getDrawable(R.drawable.bullet), BULLET_SCALE_W, BULLET_SCALE_H, this);
        explosion1_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion1)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion2_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion2)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion3_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion3)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion4_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion4)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion5_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion5)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion6_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion6)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion7_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion7)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion8_img = startScreen.resize(this.getResources().getDrawable(R.drawable.explosion8)
                , MAIN_SCALE, MAIN_SCALE, this);
        explosion_map.put(1, explosion1_img);
        explosion_map.put(2, explosion2_img);
        explosion_map.put(3, explosion3_img);
        explosion_map.put(4, explosion4_img);
        explosion_map.put(5, explosion5_img);
        explosion_map.put(6, explosion6_img);
        explosion_map.put(7, explosion7_img);
        explosion_map.put(8, explosion8_img);

        // UI elements
        line_img = (BitmapDrawable) this.getResources().getDrawable(R.drawable.line);

        pauseButton_img = (BitmapDrawable) getResources().getDrawable(R.drawable.pause_button);
        pauseScreen_img = (BitmapDrawable) getResources().getDrawable(R.drawable.pause_screen);

        countdown1_img = (BitmapDrawable) getResources().getDrawable(R.drawable.countdown1);
        countdown2_img = (BitmapDrawable) getResources().getDrawable(R.drawable.countdown2);
        countdown3_img = (BitmapDrawable) getResources().getDrawable(R.drawable.countdown3);

        // adding countdown images to map
        countdownImages_map.put(1, countdown1_img);
        countdownImages_map.put(2, countdown2_img);
        countdownImages_map.put(3, countdown3_img);

        play_img = (BitmapDrawable) this.getResources().getDrawable(R.drawable.play);

        gameOver = findViewById(R.id.gameOver);
        gameOver.setVisibility(View.GONE);

        // adding explosion sounds to map
        explosionSounds_map.put(1, EXPLOSION_1);
        explosionSounds_map.put(2, EXPLOSION_2);
        explosionSounds_map.put(3, EXPLOSION_3);

        background = (BitmapDrawable) this.getResources().getDrawable(R.drawable.background);
    }

    // Function -> initImageViews
    // Void -> Void
    // initialises the Views used to display images or text in game, and accesses SharedPreference Values
    private void initImageViews() {

        // ImageViews to display game objects
        spaceShip_ImageView = startScreen.resizeImageView((ImageView) findViewById(R.id.spaceShip_Image), spaceship_img
                , SPACESHIP_SCALE, SPACESHIP_SCALE);
        Alien_ImageView = startScreen.resizeImageView((ImageView) findViewById(R.id.Aliens_Image), startScreen.empty
                , startScreen.ASPECT_RATIO, DEFAULT_SCALE);
        Bullet_ImageView = startScreen.resizeImageView((ImageView) findViewById(R.id.Bullets_Image), startScreen.empty
                , startScreen.ASPECT_RATIO, DEFAULT_SCALE);
        Explosion_ImageView = startScreen.resizeImageView((ImageView) findViewById(R.id.Explosions_Image), startScreen.empty
                , startScreen.ASPECT_RATIO, DEFAULT_SCALE);

        // ImageViews for UI elements
        line_ImageView = startScreen.resizeImageView(findViewById(R.id.line_Image), line_img, startScreen.ASPECT_RATIO, LINE_SCALE);
        line_ImageView.setX(0);
        line_ImageView.setY((float)(startScreen.HEIGHT*(1-LINE_SCALE_H)));

        pauseButton_ImageView = startScreen.resizeImageView(findViewById(R.id.pauseButton_Image), startScreen.empty
                , PAUSE_SCALE, PAUSE_SCALE);
        pauseScreen_ImageView = findViewById(R.id.pauseScreen_Image);

        score_Text = findViewById(R.id.score);
        score_Text.setText(String.valueOf(score));

        // get the stored highscore value
        SharedPreferences scores = this.getPreferences(Context.MODE_PRIVATE);
        highscore = scores.getInt(score_key, default_highscore);

        highScore_Text = findViewById(R.id.highscore);
        highScore_Text.setText("High: " + highscore);

        yourScore_Text = findViewById(R.id.yourScore);

        cornerPause_ImageView = startScreen.resizeImageView(findViewById(R.id.cornerPause_Image), pauseButton_img
                , CORNER_PAUSE_SCALE, CORNER_PAUSE_SCALE);
        cornerPauseInit();

        findViewById(R.id.tutorial).setVisibility(View.GONE);

        Background = findViewById(R.id.FrameLayout);
        Background.setBackground(background);

    }

    // Function -> cornerPauseInit
    // Void -> Void
    // adds an OnTouchListener to the pause button in the top-right corner
    private void cornerPauseInit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cornerPause_ImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        // pause upon user touch
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            pause();
                        }
                        return true;
                    }
                });
            }
        });
    }

    // Function -> initObjects
    // Void -> Void
    // initialises default position, velocity, and other miscellaneous values used in game
    private void initObjects() {
        // spaceship default values
        SPACESHIP_INIT_X = (int)(startScreen.WIDTH / 2);
        SPACESHIP_INIT_Y = (int) (startScreen.HEIGHT - (LINE_SCALE_H * startScreen.HEIGHT) - (startScreen.HEIGHT*SPACESHIP_SCALE));
        spaceship = new Spaceship(SPACESHIP_INIT_X, SPACESHIP_INIT_Y);

        // bullet starting height
        BULLET_INIT_Y = SPACESHIP_INIT_Y - bullet_img.getMinimumHeight();

        // bullet max height
        BULLET_HEIGHT_LIMIT = (int)((startScreen.HEIGHT* RIGHT_LEFT_BARS) - bullet_img.getMinimumHeight());

        // max and min spaceship x-coordinates
        MIN_SPACESHIP = (int)(startScreen.WIDTH * RIGHT_LEFT_BARS);
        MAX_SPACESHIP = (int)(startScreen.WIDTH - (startScreen.WIDTH * RIGHT_LEFT_BARS));

        // velocities of bullets and aliens
        ALIEN_V = (int)(startScreen.HEIGHT * ALIEN_V_SCALE);
        BULLET_V = (int)(startScreen.HEIGHT * BULLET_V_SCALE);

        // clears lists for aliens, bullets and explosions
        aliens.clear();
        bullets.clear();
        explosions.clear();
    }

    // Function -> startTimer
    // Void -> Void
    // schedules tasks to be executed repeatedly to progress the game
    private void startTimer() {
        timer = new Timer(); // timer for most game activities
        paintTimer = new Timer();  // timer to schedule displaying next frame of game
        explosionTimer = new Timer(); // timer to progress explosions
        explosionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextExplosions();
            }
        }, 0, EXPLOSION_FRAME_RATE);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextAliens();
                nextBullets();
                removeInvalidBullets();
                removeFinishedExplosions();
                alienLineCross();
                spaceshipCollision();
                alienDamage();
            }
        }, 0, PAINT_RATE);
        paintTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                paint();
            }
        }, 0, PAINT_RATE);
    }

    // Function -> startAlienTimer
    // Void -> Void
    // schedules a TimerTask to spawn in aliens at regular intervals
    private void startAlienTimer() {
        alienTimer = new Timer(); // timer to spawn aliens
        timer.schedule(new TimerTask() { // adds task to gradually increase frequency of alien spawning
            @Override
            public void run() {
                for (int i = 1; i <= ALIEN_SPAWN_MULTIPLIER; i++) { // adds a spawn task ALIEN_SPAWN_MULTIPLIER times
                    timer.schedule(new TimerTask() { // adds task to spawn an alien every ALIEN_SPAWN_RATE milliseconds
                        @Override
                        public void run() {
                            addAlien();
                        }
                    }, 0, ALIEN_SPAWN_RATE);
                }
                ALIEN_SPAWN_MULTIPLIER = ALIEN_SPAWN_MULTIPLIER * 1.2; // increases ALIEN_SPAWN_MULTIPLIER
            }
        }, 0, 10000);
    }

    // Function -> stopTimers
    // Void -> Void
    // stops all scheduled TimerTasks and cancels all Timers
    private void stopTimers() {
        timer.cancel();
        timer.purge();
        paintTimer.cancel();
        paintTimer.purge();
        explosionTimer.cancel();
        explosionTimer.purge();
        alienTimer.cancel();
        alienTimer.purge();
    }

    // Function -> nextAliens
    // Void -> Void
    // changes each Alien's y-coordinate in the list by ALIEN_V
    private void nextAliens() {
        for (Alien a : aliens) {
            a.setY(a.getY() + ALIEN_V);
        }
    }

    // Function -> nextBullets
    // Void -> Void
    // changes each Bullet's y-coordinate in the list by BULLET_V
    private void nextBullets() {
        CopyOnWriteArrayList<Bullet> temp = new CopyOnWriteArrayList<Bullet>(bullets);
        for (Bullet b : temp) {
            b.setY(b.getY() - BULLET_V);
        }
        bullets = temp;
    }

    // Function -> removeInvalidBullets
    // Void -> Void
    // remove all Bullets in the list whose y-coordinate is <= BULLET_HEIGHT_LIMIT
    private void removeInvalidBullets() {
        List<Bullet> toRemove = new ArrayList<Bullet>();
        CopyOnWriteArrayList<Bullet> temp = new CopyOnWriteArrayList<Bullet>(bullets);
            for (Bullet b : temp) {
                if (b.getY() <= BULLET_HEIGHT_LIMIT) {
                    toRemove.add(b);
                }
            }
            bullets.removeAll(toRemove);
    }

    // Function -> alienDamage
    // Void -> Void
    // Detects collision and removes corresponding Bullet and Alien from their lists
    private void alienDamage() {

        List<Bullet> toRemoveBullet = new ArrayList<Bullet>();
        List<Alien> toRemoveAlien = new ArrayList<Alien>();
        CopyOnWriteArrayList<Bullet> temp1 = new CopyOnWriteArrayList<Bullet>(bullets);
        CopyOnWriteArrayList<Alien> temp2 = new CopyOnWriteArrayList<Alien>(aliens);
            for (Alien a : temp2) {
                for (Bullet b : temp1) {
                    if (overlapping(b, a)) { // detect overlapping objects
                        toRemoveBullet.add(b); // removes bullet
                        toRemoveAlien.add(a); // removes dead alien
                        explosions.add(new Explosion(a.getX(), a.getY())); // adds explosion where alien was
                        playSound("Explosion"); // plays explosion sound
                        score++; // adds to score
                        break;
                    }
                }
            }
            bullets.removeAll(toRemoveBullet);
            aliens.removeAll(toRemoveAlien);
    }

    // Function -> overlapping
    // gameObject gameObject -> Boolean
    // returns true if 2 gameObjects are overlapping
    private <T extends gameObject> Boolean overlapping(T second, T first) {
        BitmapDrawable firstImage = getImage(first);
        BitmapDrawable secondImage = getImage(second);

        // checks if second images is within bounds of first image
        return ((second.getX() > (first.getX() - secondImage.getMinimumWidth()))
                && (second.getX() < (first.getX() + firstImage.getMinimumWidth() + secondImage.getMinimumWidth()))
                && (second.getY() < (first.getY() + firstImage.getMinimumHeight()))
                && (second.getY() > first.getY()));
    }

    // Function -> spaceshipCollision
    // Void -> Void
    // detects when an Alien in the list and the Spaceship have collided
    private void spaceshipCollision() {
        List<Alien> lst = aliens;
        for (Alien a : lst) {
            if (overlapping(spaceship, a)) {
                death(a); // initiates death process
                break;
            }
        }
    }

    // Function -> alienLineCross
    // Void -> Void
    // detects when an Alien in the list has crossed the bottom line
    private void alienLineCross() {
        List<Alien> lst = aliens;
        for (Alien a : lst) {
            if (a.getY() >= ((startScreen.HEIGHT * (1 - LINE_SCALE_H)) - (startScreen.HEIGHT * MAIN_SCALE))) {
                death(a); // initiates death process
                break;
            }
        }
    }

    // Function -> newHighScore
    // Void -> Void
    // Updates the stored highscore if current score is higher
    private void newHighScore() {
        SharedPreferences scores = this.getPreferences(Context.MODE_PRIVATE);
        if (score > highscore) { // if current score is higher than stored highscore
            SharedPreferences.Editor editor = scores.edit();
            editor.putInt(score_key, score); // replaces stored highscore with current score
            editor.apply();
        }
    }

    // Function -> death
    // Alien -> Void
    // initiates the death process for the spaceship and signals that the game has ended
    // takes in Alien that caused death as parameter
    private void death(Alien a) {

        newHighScore(); // checks for new high score

        deathFlag = true; // indicates spaceship has died

        timer.cancel();
        timer.purge();
        alienTimer.cancel();
        alienTimer.purge();

        // sets background to black
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Background.setBackgroundColor(Color.BLACK);
            }
        });

        // removes all game objects
        aliens.clear();
        explosions.clear();
        bullets.clear();

        // adds Alien that caused death to list
        aliens.add(a);

        // pauses Thread
        try {
            Thread.sleep(500);
        } catch(Exception e) {
            e.getStackTrace();
        }

        // removes UI elements and alien
        aliens.clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                line_ImageView.setBackground(startScreen.empty);
                pauseButton_ImageView.setBackground(startScreen.empty);
            }
        });

        // adds explosion where death causing alien was
        explosions.add(new Explosion(a.getX(), a.getY()));
        playSound("Explosion");

        // pauses Thread
        try {
            Thread.sleep(1000);
        } catch(Exception e) {
            e.getStackTrace();
        }

        // cancels all Timers
        explosionTimer.cancel();
        explosionTimer.purge();
        paintTimer.cancel();
        paintTimer.purge();

        // clears explosions
        explosions.clear();

        // pauses Thread
        try {
            Thread.sleep(1000);
        } catch(Exception e) {
            e.getStackTrace();
        }

        // start game over process
        gameOver();
    }

    // Function -> gameOver
    // Void -> Void
    // initialises and shows the Game Over screen
    private void gameOver() {

        // signals game is over
        gameOverFlag = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // displays game over screen
                gameOver.setVisibility(View.VISIBLE);
                yourScore_Text.setText("Your Score: " + score);
                score_Text.setText("");
                highScore_Text.setText("");
                cornerPause_ImageView.setVisibility(View.GONE);
                spaceShip_ImageView.setVisibility(View.GONE);
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // sets OnTouchListeners for game over screen buttons
                findViewById(R.id.play_Image).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return startOver(motionEvent);
                    }
                });
                findViewById(R.id.exit_Image).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return exit(motionEvent);
                    }
                });
            }
        });

    }

    // Function -> startOver
    // MotionEvent -> Boolean
    // starts the game again upon clicking a button
    private Boolean startOver(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            // starts new game
            wrapUpGame();
            Intent intent = new Intent(this, gameScreen.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    // Function -> exit
    // MotionEvent -> Boolean
    // returns back to the Title screen upon clicking a button
    private Boolean exit(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            // goes back to start screen
            wrapUpGame();
            Intent intent = new Intent(this, startScreen.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    // Function -> wrapUpGame
    // Void -> Void
    // stops all timers and sets all game values to default values
    private void wrapUpGame() {
        stopTimers();
        aliens.clear();
        explosions.clear();
        bullets.clear();
        score = 0;
    }

    // Function -> removeFinishedExplosions
    // Void -> Void
    // removes explosions that have reached their final state
    private void removeFinishedExplosions() {
        if (Build.VERSION.SDK_INT >= 24) {
            explosions.removeIf(e -> (e.getState() >= EXPLOSION_FINAL_STATE));
        } else {
            List<Explosion> toRemove = new ArrayList<Explosion>();
            CopyOnWriteArrayList<Explosion> temp = new CopyOnWriteArrayList<Explosion>(explosions);
            for (Explosion e : temp) {
                if (e.getState() >= EXPLOSION_FINAL_STATE) {
                    toRemove.add(e);
                }
            }
            explosions.removeAll(toRemove);
        }
    }

    // Function -> addAlien
    // Void -> Void
    // adds an alien at a random x-coordinate in the range if game is still running
    private void addAlien() {
        if (!deathFlag) { // if game is still running
            // generates random x-coordinate within screen bounds
            int random_X = (int) ((startScreen.WIDTH - (startScreen.HEIGHT * MAIN_SCALE) - startScreen.NAV_BAR_WIDTH) * (Math.random()));
            Boolean inlist = false;
            for (Alien a : aliens) {
                inlist = (inlist && (random_X == a.getX())); // checks if there is already Alien with random_X x-coordinate
            }
            if (inlist) { // Alien already in list with random_X x-coordinate
                addAlien(); // re-generates random x-coordinate
            } else {
                aliens.add(new Alien(random_X, -(int) (startScreen.HEIGHT * LINE_SCALE_H))); // adds alien to list off-screen
            }
        }
    }

    // Function -> nextExplosions
    // Void -> Void
    // increases the state of each explosion in the list by 1
    private void nextExplosions() {
        for (Explosion e : explosions) {
            if (e.getState() < 8) {
                e.setState(e.getState() + 1);
            }
        }
    }

    // Function -> paint
    // Void -> Void
    // sets the current images to be displayed onto their corresponding ImageViews
    private void paint() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // displays all aliens, bullets, and explosions at respective coordinates
                Alien_ImageView.setBackground(allImage(aliens));

                Bullet_ImageView.setBackground(allImage(bullets));

                Explosion_ImageView.setBackground(allImage(explosions));

                // displays spaceship
                spaceShip_ImageView.setBackground(spaceship_img);
                spaceShip_ImageView.setX(spaceship.getX());
                spaceShip_ImageView.setY(spaceship.getY());

                // displays current score
                score_Text.setText(String.valueOf(score));
            }
        });

    }

    // Function -> allImage
    // (listof gameObject) -> BitmapDrawable
    // compiles the corresponding images for a list of gameObjects into a single image
    private <T extends gameObject> BitmapDrawable allImage(List<T> objList) {
        List<T> lst = objList;
        if (lst.isEmpty()) { // empty list returns default empty image
            return new BitmapDrawable(getResources(), Bitmap.createBitmap(startScreen.WIDTH, startScreen.HEIGHT, Bitmap.Config.ARGB_8888));
        }
        else {
            T first = lst.get(0);
            List<T> rest = lst.subList(1, lst.size());
            Bitmap result = (allImage(rest)).getBitmap(); // gets result for rest of list by natural recursion
            Canvas canvas = new Canvas(result);
            BitmapDrawable img = null;
            try {
                img = getImage(first); // gets image for first gameObject in list
            } catch (NullPointerException e) {
                System.out.println("error" + lst.getClass());
            }
            canvas.drawBitmap(img.getBitmap(), first.getX(), first.getY(), null); // adds image onto result
            return new BitmapDrawable(getResources(), result); // returns updated result

        }
    }

    // Function -> getImage
    // gameObject -> BitmapDrawable
    // returns the corresponding image for the given gameObject
    private <T extends gameObject> BitmapDrawable getImage(T obj) {
        switch(obj.getClass().getSimpleName()) {
            case "Spaceship" :
                return spaceship_img;
            case "Alien" :
                return alien_img;
            case "Bullet" :
                return bullet_img;
            case "Explosion" :
                return explosion_map.get(((Explosion)(obj)).getState()); // returns image that matches explosion state
            default :
                return null;
        }
    }

    /*private BitmapDrawable alienWithHealth(Alien a) {
        BitmapDrawable img = alienImage(a);

        Bitmap result = Bitmap.createBitmap((int)(img.getMinimumWidth() * (1 + 2*HEALTH_BAR_PADDING)),
                (int)(img.getMinimumHeight() * (1 + 2*HEALTH_BAR_PADDING)), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(img.getBitmap(), (int)(img.getMinimumWidth()*HEALTH_BAR_PADDING)
                , (int)(img.getMinimumHeight()*2*HEALTH_BAR_PADDING), null);

        healthBar_img = Bitmap.createScaledBitmap(healthBar_img, (int)(img.getMinimumWidth() * (1 + 2*HEALTH_BAR_PADDING))
                , (int)(img.getIntrinsicHeight()*HEALTH_BAR_PADDING), true);
        healthMeter_img = Bitmap.createScaledBitmap(healthMeter_img,
            (int)((img.getMinimumWidth() * (1 + 2*(HEALTH_BAR_PADDING - HEALTH_METER_PADDING))) *
                    (a.getHealth() / ALIEN_DEFAULT_HEALTH))
                , (int)(img.getMinimumHeight()*(HEALTH_BAR_PADDING - 2*HEALTH_METER_PADDING)), true);

        canvas.drawBitmap(healthBar_img, 0, 0, null);
        canvas.drawBitmap(healthMeter_img, (int)(img.getMinimumWidth()*HEALTH_METER_PADDING)
                , (int)(img.getMinimumHeight()*HEALTH_METER_PADDING), null);

        return new BitmapDrawable(getResources(), result);
    }
*/
    /*private BitmapDrawable alienImage(Alien a) {
        return alien_img;
    }*/

    private GestureDetector gestureDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER);
        binding = ActivityGameScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;
        gestureDetect = new GestureDetector(this, new gestures());

        // initialises game
        init();
        // hides System bars
        hide();
        // displays tutorial if turned on in settings
        if (startScreen.tutorial) {
            tutorial();
        } else {
            startAlienTimer(); // skips tutorial
        }

        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                addBullet(motionEvent);
                return gestureDetect.onTouchEvent(motionEvent);

            }
        });
        
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    // Function -> tutorial
    // Void -> Void
    // displays the tutorial on screen for TUTORIAL_DURATION milliseconds
    private void tutorial() {

        // displays the tutorial
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.tutorial).setVisibility(View.VISIBLE);
                findViewById(R.id.tutorialLeft_Image).setBackground(getDrawable(R.drawable.tutorialleft));
                findViewById(R.id.tutorialRight_Image).setBackground(getDrawable(R.drawable.tutorialright));
                findViewById(R.id.tutorialTap_Image).setBackground(getDrawable(R.drawable.tutorialtap));
            }
        });

        // hides the tutorial after TUTORIAL_DURATION milliseconds
        Timer tutorialTimer = new Timer();
        tutorialTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // hides tutorial
                        findViewById(R.id.tutorial).setVisibility(View.GONE);
                        findViewById(R.id.tutorialLeft_Image).setBackground(null);
                        findViewById(R.id.tutorialRight_Image).setBackground(null);
                        findViewById(R.id.tutorialTap_Image).setBackground(null);
                    }
                });

                // stops schedules task and starts spawning aliens
                startAlienTimer();
                tutorialTimer.cancel();
                tutorialTimer.purge();
            }
        }, TUTORIAL_DURATION, 1000); // action delayed by TUTORIAL_DURATION
    }

    @Override
    protected void onStop() {
        super.onStop();
        pause();
    }

    // Function -> pause
    // Void -> Void
    // pauses the game
    private void pause() {
        pauseFlag = true;
        if (!deathFlag) { // if game is still running

            cornerPause_ImageView.setBackground(null);
            cornerPause_ImageView.setOnTouchListener(null);

            // stops currently running tasks
            stopTimers();

            pauseScreen_ImageView.setBackground(pauseScreen_img);
            pauseButton_ImageView.setImageDrawable(pauseButton_img);
            // sets OnTouchListener for middle pause button
            pauseButton_ImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        // resumes game upon user touch
                        resume();
                    }
                    return false;
                }
            });
        } else if (deathFlag && !gameOverFlag) {  // if death process is running
            stopTimers();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // Function -> resume
    // Void -> Void
    // resumes the game
    private void resume() {
        pauseFlag = false;
        if (!deathFlag) { // if game is still running
            pauseButton_ImageView.setOnTouchListener(null);

            countdownTimer = new Timer(); // timer for resume countdown
            countdownTimer.schedule(new TimerTask() {
                int count = 3;

                @Override
                public void run() {
                    if (count > 0) { // if countdown is not over
                        // sets middle image to image corresponding to current count
                        pauseButton_ImageView.setImageDrawable(countdownImages_map.get(count));
                        count--; // reduces count
                    } else { // countdown is over
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cornerPause_ImageView.setBackground(pauseButton_img);
                                cornerPauseInit();
                                // restarts game activities
                                startTimer();
                                startAlienTimer();
                                pauseButton_ImageView.setImageDrawable(startScreen.empty);
                                pauseScreen_ImageView.setBackground(startScreen.empty);
                            }
                        });
                        // stops countdown timer
                        countdownTimer.cancel();
                        countdownTimer.purge();
                    }
                }
            }, 0, 1000); // progresses countdown every 1 second
        } else if (deathFlag && !gameOverFlag) {
            gameOver(); // displays game over screen
        }
    }

    // Function -> addBullet
    // MotionEvent -> Void
    // adds a bullet to the list and plays the sound effect when the user touches the screen
    private void addBullet(MotionEvent me) {
        // if game is still running
        if ((me.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN
        || me.getActionMasked() == MotionEvent.ACTION_UP) && !deathFlag && !pauseFlag) {
            bullets.add(new Bullet((int)(spaceShip_ImageView.getX() + ((startScreen.HEIGHT * SPACESHIP_SCALE) / 2) - ((startScreen.HEIGHT * BULLET_SCALE_W) / 2))
                    , BULLET_INIT_Y)); // add bullet at current spaceship center
            playSound("Bullet"); // play bullet sound
        }
    }

    // Function -> playSound
    // String -> Void
    // plays the sound effect corresponding to the given string if sound is turned on
    private void playSound(String obj) {
        if (startScreen.sound && !pauseFlag) { // if sound is enabled
            switch (obj) {
                case "Bullet":
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), BULLET_SOUND);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mp.release(); // release sound after completion
                        }
                    });
                    break;
                case "Explosion":
                    int sound_choice = ((int) (2 * Math.random())) + 1; // random explosion sound out of 3
                    MediaPlayer mp2 = MediaPlayer.create(getApplicationContext(), explosionSounds_map.get(sound_choice));
                    mp2.start();
                    mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mp2.release(); // release sound after completion
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    // class to detect when user swipes the screen
    class gestures extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        /*@Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float v1, float v2) {
            fling = true;
            Timer flingTimer = new Timer();
            SPACESHIP_V = (int)v1;
            flingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    flingSpaceship((int)SPACESHIP_V);
                }
            }, 0, PAINT_RATE);
            return true;
        }*/

        // Function -> onScroll
        // MotionEvent MotionEvent float float -> Boolean
        // detects when the user scrolls their finger across the screen
        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float dx, float dy) {
            moveSpaceship((int)dx);
            return true;
        }

    }

    /*private void flingSpaceship(int x) {
        if (((spaceship.getX() - x) >= 0) && ((spaceship.getX() - x) <= (WIDTH - HEIGHT * SPACESHIP_SCALE))) {
            spaceship.setX(spaceship.getX() - x);
        }
        SPACESHIP_V = SPACESHIP_V - FRICTION;
    }*/

    // Function -> moveSpaceship
    // int -> Void
    // changes the spaceship's x-coordinate by the given value
    private void moveSpaceship(int dx) {
        // if new spaceship position is within bounds and game is still running
        if (((spaceship.getX() - dx) >= 0) && ((spaceship.getX() - dx) <= (startScreen.WIDTH - startScreen.HEIGHT * SPACESHIP_SCALE))
                && !deathFlag && !pauseFlag) {
            spaceship.setX(spaceship.getX() - dx); // change spaceship x-coordinate
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().hide(WindowInsets.Type.systemBars());
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void show() {
        // Show the system bar

            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().show(WindowInsets.Type.systemBars());
            } else {
                int flags = mContentView.getSystemUiVisibility();
                flags &= ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                flags &= ~View.SYSTEM_UI_FLAG_FULLSCREEN;
                flags &= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
                flags &= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                flags &= ~View.SYSTEM_UI_FLAG_IMMERSIVE;
                flags &= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                mContentView.setSystemUiVisibility(flags);
            }
            mVisible = true;

            // Schedule a runnable to display UI elements after a delay
            mHideHandler.removeCallbacks(mHidePart2Runnable);
            mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);

    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}