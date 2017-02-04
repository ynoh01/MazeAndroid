package edu.wm.cs.cs301.amazebyyunwoonoh.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.wm.cs.cs301.amazebyyunwoonoh.R;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.BasicRobot;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.ManualDriver;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.MazeController;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {
    Button button;
    Button shortCutButton;
    Button noBatteryButton;
    MediaPlayer bgm;
    private CoordinatorLayout coordinatorLayout;
    private ImageButton up;
    private ImageButton down;
    private ImageButton left;
    private ImageButton right;
    private Switch wall;
    private Switch map;
    private Switch solution;
    private ProgressBar energyBar;


    private Bitmap bitmap;
    private Canvas canvas;
    private View mazeView;
    MazeController mazeController = GeneratingActivity.getMazeController();
    private boolean manual = false;
    private BasicRobot robot;
    private ManualDriver driver;
    private int percent;
    private Handler handler = new Handler();
    private Intent intent;



    /**
     * Once this activity begins, it plays backgorund music
     * and display buttons to nagivate around the maze
     * along with two shortcut buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
        setContentView(R.layout.activity_play);

        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(this, R.raw.nyancat);
        bgm.setAudioStreamType(AudioManager.STREAM_MUSIC);

        bgm.start();
        mazeView = findViewById(R.id.maze);
        energyBar = (ProgressBar) findViewById(R.id.energy);

        setAllElements();
        energyBar.setMax(2500);
        energyBar.setProgress(2500);

        intent = getIntent();
        mazeController.setPlayAct(this);

        setView();
        mazeController.beginGraphics();

        driver = (ManualDriver) mazeController.getDriver();



    }

    private void setView(){
        bitmap = Bitmap.createBitmap(400,400,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mazeController.getPanel().setCanvas(canvas);
        mazeController.getPanel().setPlayActivity(this);
        mazeView.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private void setAllElements(){
        button = (Button) findViewById(R.id.manual_back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PlayActivity.this, AMazeActivity.class);
                startActivity(intent);
                //Snackbar.make(coordinatorLayout, "Return to Main Screen", Snackbar.LENGTH_LONG).show();
                Log.v("Navigate", "From PlayActivity to AmazeActivity");
            }
        });


        shortCutButton = (Button) findViewById(R.id.manual_sc);
        shortCutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent2 = new Intent(PlayActivity.this, FinishActivity.class);
                startActivity(intent2);
                //Snackbar.make(coordinatorLayout, "ShortCut to Finish Screen", Snackbar.LENGTH_LONG).show();
                Log.v("Navigate", "From PlayActivity to FinishActivity");
            }
        });


        //////////////////////play arrows //////////////////////////////
        up = (ImageButton)findViewById(R.id.up);
        up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                try{
                    driver.drive2Exit('k');
                }catch(Exception e){}
                Log.v("move", "UP");
            }
        });

        down = (ImageButton)findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                try{
                    driver.drive2Exit('j');
                }catch(Exception e){}
                Log.v("move", "DOWN");
            }
        });

        left = (ImageButton)findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                try{
                    driver.drive2Exit('h');
                }catch(Exception e){}
                Log.v("move", "LEFT");
            }
        });

        right = (ImageButton)findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                try{
                    driver.drive2Exit('l');
                }catch(Exception e){}
                Log.v("move", "RIGHT");
            }
        });

        ////////////////////////////////////////////////////////////

        ////////////////////////////// toggles //////////////////////////////
        wall = (Switch) findViewById(R.id.wall);
        wall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    mazeController.keyDown('m');
                }
                else{
                    mazeController.keyDown('m');
                }
                Log.v("Toggle", "Wall toggle enabled");
            }
        });

        map = (Switch) findViewById(R.id.map);
        map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    mazeController.keyDown('z');
                }
                else{
                    mazeController.keyDown('z');
                }
                Log.v("Toggle", "Map toggle enabled");
            }
        });

        solution = (Switch) findViewById(R.id.solution);
        solution.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    mazeController.keyDown('s');
                }
                else{
                    mazeController.keyDown('s');
                }
                Log.v("Toggle", "Solution toggle enabled");
            }
        });

    }
    ////////////////////////////// //////////////////////////////


    public void updateView(){
        mazeView.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    /**
     * update floor graphics
     * @return
     */
    public Bitmap getFloor(){
        Bitmap floor = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        return floor;
    }

    /**
     * update sky graphics
     * @return
     */
    public Bitmap getSky(){
        Bitmap sky = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        return sky;
    }


    /**
     * update energy level, when the energy becomes 0  navigate to hungryActivity
     * @param energy
     */
    public void updateEnergy(float energy){
        percent = (int) energy;
        if(energy > 0){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    energyBar.setProgress(percent);
                }
            });
            try{
                Thread.sleep(15);
            }catch(Exception e){}
        }
        else{
            energyBar.setProgress(0);
            Intent intent = new Intent(this, HungryActivity.class);
            startActivity(intent);
        }
    }


    /**
     * navigate to finish screen
     */

    public void goToFinishAct(){
        Intent intent = new Intent(this, FinishActivity.class);
        startActivity(intent);
    }


    /**
     * music stops once the user leaves this activity
     */
    @Override
    protected void onStop(){
        super.onStop();
        bgm.stop();
    }


}
