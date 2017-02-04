package edu.wm.cs.cs301.amazebyyunwoonoh.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;

import edu.wm.cs.cs301.amazebyyunwoonoh.R;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.BasicRobot;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.MazeController;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.RobotDriver;

public class RobotActivity extends AppCompatActivity {
    Button button;
    Button shortCutButton;
    Button noBatteryButton;
    MediaPlayer bgm;
    private CoordinatorLayout coordinatorLayout;

    private Switch wall;
    private Switch map;
    private Switch solution;
    private ProgressBar energyBar;
    private Button start;
    private Button pause;





    private Bitmap bitmap;
    private Canvas canvas;
    private View mazeView;
    MazeController mazeController = GeneratingActivity.getMazeController();
    private boolean manual = false;
    private BasicRobot robot;
    private RobotDriver driver;
    private int percent;
    private Handler handler = new Handler();
    private Intent intent;

    /**
     * When this activity begins, background music plays until the user leaves the activity,
     * Buttons to start and pause the robot display
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        // music plays
        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(this, R.raw.nyancat);
        bgm.setAudioStreamType(AudioManager.STREAM_MUSIC);

        bgm.start();
        mazeView = findViewById(R.id.maze);
        energyBar = (ProgressBar) findViewById(R.id.energyBar);

        setAllElements();

        energyBar.setMax(2500);
        energyBar.setProgress(2500);

        intent = getIntent();
        mazeController.setRobotAct(this);

        setView();
        mazeController.beginGraphics();

        driver = mazeController.getDriver();

    }

    public void setAllElements() {
        button = (Button) findViewById(R.id.robot_back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(RobotActivity.this, AMazeActivity.class);
                startActivity(intent);
                //Snackbar.make(coordinatorLayout, "Return to Main Screen", Snackbar.LENGTH_LONG).show();
                Log.v("Navigate", "From PlayActivity to AmazeActivity");
            }
        });

        // shorcut button to navigate user to finish activity
        shortCutButton = (Button) findViewById(R.id.robot_sc);
        shortCutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent2 = new Intent(RobotActivity.this, FinishActivity.class);
                startActivity(intent2);
                //Snackbar.make(coordinatorLayout, "ShortCut to Finish Screen", Snackbar.LENGTH_LONG).show();
                Log.v("Navigate", "From PlayActivity to FinishActivity");
            }
        });

        ////////////////////////////// toggles //////////////////////////////
        wall = (Switch) findViewById(R.id.wall);
        wall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mazeController.keyDown('m');
                } else {
                    mazeController.keyDown('m');
                }
                Log.v("Toggle", "Wall toggle enabled");
            }
        });

        map = (Switch) findViewById(R.id.map);
        map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mazeController.keyDown('z');
                } else {
                    mazeController.keyDown('z');
                }
                Log.v("Toggle", "Map toggle enabled");
            }
        });

        solution = (Switch) findViewById(R.id.solution);
        solution.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mazeController.keyDown('s');
                } else {
                    mazeController.keyDown('s');
                }
                Log.v("Toggle", "Solution toggle enabled");
            }
        });

        ///////////////////start and pause buttons ////////////////////
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                driver.resume();
                Log.v("Button","Start the robot");
            }

        });


        pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                driver.pause();
                Log.v("Button", "Pause the robot");
            }

        });




    }

    private void setView(){
        bitmap = Bitmap.createBitmap(400,400,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        mazeController.getPanel().setCanvas(canvas);
        mazeController.getPanel().setRobotActivity(this);
        mazeView.setBackground(new BitmapDrawable(getResources(), bitmap));
    }



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
            //energyBar.setProgress(0);
            Intent intent = new Intent(this, HungryActivity.class);
            startActivity(intent);
            Log.v("Navigate", "From PlayActivity to HungryActivity");
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
     * Music stops once the user leaves AmazeActivity
     */
    @Override
    protected void onStop(){
        super.onStop();
        bgm.stop();
        driver.pause();
    }

}
