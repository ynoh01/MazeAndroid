package edu.wm.cs.cs301.amazebyyunwoonoh.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.wm.cs.cs301.amazebyyunwoonoh.R;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.MazeController;

public class FinishActivity extends AppCompatActivity {
    Button button;
    MediaPlayer bgm;
    private CoordinatorLayout coordinatorLayout;
    MazeController mazeController = GeneratingActivity.getMazeController();
    private TextView energy;
    private TextView path;


    /**
     * Indicate the user that the game is over and the user found the exit.
     * when back button is clicked, navigate to AmazeActivity by sending intent
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(this, R.raw.finish);
        bgm.setAudioStreamType(AudioManager.STREAM_MUSIC);

        bgm.start();

        button = (Button) findViewById(R.id.finish_back);
        // when game start button is clicked, move to generating activity
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){

                //navigate to AMaze activity
                Intent intent = new Intent(FinishActivity.this, AMazeActivity.class);
                //send message
                startActivity(intent);
                //Snackbar.make(coordinatorLayout, "Return to Main Screen", Snackbar.LENGTH_LONG).show();
                Log.v("Navigate", "From FinishActivity to AMazeActivity");

            }
        });

        path = (TextView) findViewById(R.id.path);
        path.setText("Path Length: "+ mazeController.getPathLength());

        energy = (TextView) findViewById(R.id.energy);
        energy.setText("Energy Consumption: "+(2500-mazeController.getBattery()));

    }
    /**
     * Music stops once the user leaves AmazeActivity
     */
    @Override
    protected void onStop(){
        super.onStop();
        bgm.stop();
    }
    }

