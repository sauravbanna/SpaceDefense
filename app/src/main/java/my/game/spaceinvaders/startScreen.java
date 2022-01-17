package my.game.spaceinvaders;

import static android.view.View.GONE;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import my.game.spaceinvaders.R;
import my.game.spaceinvaders.databinding.ActivityStartScreenBinding;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class startScreen extends AppCompatActivity {
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
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(
                        WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            } else {
                // Note that some of these constants are new as of API 16 (Jelly Bean)
                // and API 19 (KitKat). It is safe to use them, as they are inlined
                // at compile-time and do nothing on earlier devices.
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
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

    // dimensions of screen
    public static int HEIGHT;
    public static int WIDTH;
    public static int NAV_BAR_WIDTH;

    // images used in the start screen
    BitmapDrawable logo_img;
    BitmapDrawable startButton_img;
    BitmapDrawable settings_img;
    Drawable leverOn_img;
    Drawable leverOff_img;
    public static BitmapDrawable empty;

    // ImageViews used in the start screen
    ImageView logo_ImageView;
    ImageView startButton_ImageView;
    ImageView settings_ImageView;

    // Ratios for scaling image sizes and positions
    public static double ASPECT_RATIO;
    public static double LOGO_SCALE_H = (double) 1 / 3;
    public static double LOGO_SCALE_W = 1;

    // constants for the logo slide up animation
    int LOGO_FINAL_Y;
    int LOGO_SLIDE_FRAMES = 8;

    // frame rate of the logo slide up animation
    int FRAME_RATE = 50;

    // timers for different activities in the start screen
    Timer logoTimer = new Timer();
    Timer startTimer = new Timer();

    // DisplayMetrics to get the screen size information
    final DisplayMetrics SIZE = new DisplayMetrics();

    // SharedPrefenrence keys and default values for settings booleans
    public static String tutorial_key = "tutorial";
    public static Boolean default_tutorial = true;
    public static String sound_key = "sound";
    public static Boolean default_sound = true;
    public static String settingsPrefKey = "settings";

    // Instance of start screen
    private static startScreen sInstance = null;

    // Boolean flags
    Boolean settingsFlag = false;

    // User setting Booleans
    public static Boolean tutorial;
    public static Boolean sound;

    // Function -> init
    // Void -> Void
    // initliases the start screen
    private void init() {
        initDimensions();
        initImages();
        initImageViews();
    }

    // Function -> initDimensions
    // Void -> Void
    // initialises the size of the current screen
    private void initDimensions() {

        // gets the screen height and width
        getWindowManager().getDefaultDisplay().getMetrics(SIZE);
        HEIGHT = SIZE.heightPixels;
        WIDTH = SIZE.widthPixels;

        // gets the screen's navigation bar width
        int nav = this.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (nav > 0) {
            NAV_BAR_WIDTH = this.getResources().getDimensionPixelSize(nav);
        }

        ASPECT_RATIO = (double) WIDTH / HEIGHT;
    }


    // Function -> initImages
    // Void -> Void
    // initialises the images used in the start screen
    private void initImages() {
        logo_img = (BitmapDrawable) this.getResources().getDrawable(R.drawable.logo);
        startButton_img = (BitmapDrawable) this.getResources().getDrawable(R.drawable.startflash);
        settings_img = (BitmapDrawable) this.getResources().getDrawable(R.drawable.settings);
        empty = (BitmapDrawable) getResources().getDrawable(R.drawable.empty);
        leverOff_img = getResources().getDrawable(R.drawable.leveroff);
        leverOn_img = getResources().getDrawable(R.drawable.leveron);
    }

    // Function -> initImageViews
    // Void -> Void
    // initialises the ImageViews used to display images in the start screen
    private void initImageViews() {
        logo_ImageView = resizeImageView(findViewById(R.id.logo_Image), logo_img
                , LOGO_SCALE_W, LOGO_SCALE_H);

        LOGO_FINAL_Y = (int)(HEIGHT * LOGO_SCALE_H);
        logo_ImageView.setY(HEIGHT);

        startButton_ImageView = resizeImageView(findViewById(R.id.startButton_Image), empty
                , LOGO_SCALE_W / 4, LOGO_SCALE_H / 2);

        settings_ImageView = resizeImageView(findViewById(R.id.settings_Image), settings_img
                , LOGO_SCALE_W / 4, LOGO_SCALE_H / 2);
        // adds OnTouchListener to settings button
        settings_ImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    settings();
                }
                return true;
            }
        });

        // get the stored setting booleans
        SharedPreferences settings = this.getSharedPreferences(settingsPrefKey, Context.MODE_PRIVATE);
        tutorial = settings.getBoolean(tutorial_key, default_tutorial);
        sound = settings.getBoolean(sound_key, default_sound);

        // hides the settings menu
        findViewById(R.id.settings).setY(HEIGHT);
    }

    // Function -> settings
    // Void -> Void
    // displays the game settings menu
    private void settings() {
        settingsFlag = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //displays the settings menu
                logoTimer.cancel();
                logoTimer.purge();
                startButton_ImageView.setOnTouchListener(null);


                findViewById(R.id.settingsBack).setBackground(getDrawable(R.drawable.settingsback));

                findViewById(R.id.settingsOk_Image).setBackground(getDrawable(R.drawable.okbutton));
                // adds OnTouchListener to return back to start screen
                findViewById(R.id.settingsOk_Image).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            settingsFlag = false;
                            Intent intent = new Intent(getApplicationContext(), startScreen.class);
                            startActivity(intent);
                            finish();

                        }
                        return true;
                    }
                });

                // adds image and OnTouchListener to lever that controls Tutorial On/Off
                leverImage(tutorial,
                        findViewById(R.id.tutorial_Lever),
                        leverOn_img,
                        leverOff_img);
                findViewById(R.id.tutorial_Lever).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            // changes state of setting and displays different image based on on/off state
                            leverImage(changeSetting(tutorial_key, default_tutorial),
                                    findViewById(R.id.tutorial_Lever),
                                    leverOn_img,
                                    leverOff_img);
                        }
                        return true;
                    }
                });

                // adds image and OnTouchListener to lever that controls Sound On/Off
                leverImage(sound,
                        findViewById(R.id.sound_Lever),
                        leverOn_img,
                        leverOff_img);
                findViewById(R.id.sound_Lever).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            // changes state of setting and displays different image based on on/off state
                            leverImage(changeSetting(sound_key, default_sound),
                                    findViewById(R.id.sound_Lever),
                                    leverOn_img,
                                    leverOff_img);
                        }
                        return true;
                    }
                });
            }
        });

        // slides the settings menu up
        moveImage(HEIGHT, 0, LOGO_SLIDE_FRAMES, findViewById(R.id.settings), null, sInstance);
        findViewById(R.id.title).setVisibility(View.GONE);
    }

    private void leverImage(Boolean currentState, ImageView imgview, Drawable leveron, Drawable leveroff) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentState) {
                    imgview.setBackground(leveron);
                } else {
                    imgview.setBackground(leveroff);
                }
            }
        });
    }

    // Function -> changeSetting
    // String Boolean -> Boolean
    // changes value and returns current value of the setting with the given key to its opposite value
    private Boolean changeSetting(String key, Boolean def) {
        SharedPreferences spref = this.getSharedPreferences(settingsPrefKey, Context.MODE_PRIVATE);
        Boolean current = spref.getBoolean(key, def);
        Boolean updated = !current;
        SharedPreferences.Editor editor = spref.edit();
        editor.putBoolean(key, updated);
        editor.apply();
        return updated;
    }

    // Function -> resize
    // Drawable double double Context -> BitmapDrawable
    // returns a BitmapDrawable of the given Drawable resized to the given scales relative to the screen height
    public static BitmapDrawable resize(Drawable b, double w_scale, double h_scale, Context context) {
        BitmapDrawable bitdrw = (BitmapDrawable) b;
        Bitmap bitmap = bitdrw.getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (HEIGHT * w_scale), (int) (HEIGHT * h_scale), true);
        return new BitmapDrawable(context.getResources(), resized);
    }

    // Function -> resizeImageView
    // ImageView BitmapDrawable double double -> ImageView
    // returns an ImageView scaled with given doubles relative to screen height with the given image as background
    public static ImageView resizeImageView(ImageView imgview, BitmapDrawable img, double w_scale, double h_scale) {
        ViewGroup.LayoutParams params = imgview.getLayoutParams();
        params.width = (int)(HEIGHT*w_scale);
        params.height = (int)(HEIGHT*h_scale);
        imgview.setLayoutParams(params);
        imgview.setBackground(img);
        return imgview;
    }

    private ActivityStartScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sInstance = this;

        binding = ActivityStartScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mVisible = true;
        mControlsView = binding.fullscreenContentControls;
        mContentView = binding.fullscreenContent;

        init();
        hide();
        Method m = null;
        try {
            m = Class.forName(startScreen.class.getName()).getDeclaredMethod("startButton");
        } catch(Exception e) {
            e.getStackTrace();
        }
        moveImage(HEIGHT, LOGO_FINAL_Y, LOGO_SLIDE_FRAMES, logo_ImageView, m, sInstance);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!settingsFlag) {
            startTimer.cancel();
            startTimer.purge();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!settingsFlag) {
            startButtonFlash();
        }
    }

    // Function -> moveImage
    // int int int View Method AppCompatActivity Boolean -> Void
    // slides the given View up from max height to target, and then invokes given method upon completion
    private void moveImage(int max, int target, int frames, View view, Method m, AppCompatActivity instance) {
        double slideDistance = max - target; // distance to move
        logoTimer = new Timer();
        logoTimer.schedule(new TimerTask() {
            int count = 1;
            @Override
            public void run() {
                if (count <= frames) {   // proceeds with animation until completion
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setY((int) (target + ((slideDistance) / (Math.pow(1.5, count)))));
                        }
                    });
                    count++; // indicates that the animation has progressed
                } else {   // animation is complete
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setY(target); // sets view's final height to target
                        }
                    });
                    // invokes given method
                    try {
                        m.setAccessible(true);
                        m.invoke(instance);
                    } catch(Exception e) {
                        e.getStackTrace();
                    }
                    // cancels timer and ends animation
                    logoTimer.cancel();
                    logoTimer.purge();
                }
            }
        }, 500, FRAME_RATE); // progresses animation every FRAME_RATE milliseconds
    }

    // Function -> startButton
    // Void -> Void
    // displays the start button and adds an OnTouchListener to start the game upon user touch
    private void startButton() {
        startButton_ImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // moves the logo off screen upwards
                    Method m = null;
                    try {
                        m = Class.forName(startScreen.class.getName()).getDeclaredMethod("startGame");
                    } catch(Exception e) {
                        e.getStackTrace();
                    }
                    moveImage(LOGO_FINAL_Y, -((int)(HEIGHT * LOGO_SCALE_H)), LOGO_SLIDE_FRAMES
                            , logo_ImageView, m, sInstance);
                }
                return true;
            }
        });

        startButtonFlash();
    }

    // Function -> startButtonFlash
    // Void -> Void
    // makes the start button flash every 1 second
    private void startButtonFlash() {
        startTimer = new Timer();
        startTimer.schedule(new TimerTask() {
            // makes the start button flash every 1 second
            int count = 0;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count % 2 == 0) {
                            startButton_ImageView.setBackground(startButton_img);
                        } else {
                            startButton_ImageView.setBackground(empty);
                        }
                        count++;
                    }
                });
            }
        }, 0, 1000);
    }

    // Function -> startGame
    // Void -> Void
    // starts the game
    private void startGame() {
        startTimer.cancel();
        startTimer.purge();
        Intent intent = new Intent(this, gameScreen.class);
        startActivity(intent);
        finish();
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
        mControlsView.setVisibility(GONE);
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