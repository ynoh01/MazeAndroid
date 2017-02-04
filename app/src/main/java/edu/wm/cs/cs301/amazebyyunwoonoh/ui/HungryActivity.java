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

import edu.wm.cs.cs301.amazebyyunwoonoh.R;

public class HungryActivity extends AppCompatActivity {
    MediaPlayer bgm;

    Button button;
    private CoordinatorLayout coordinatorLayout;


    /**
     * Once this activity begins, indicates the user that the game is over
     * due to lack of battery and display back button for user to navigate
     * back to main title screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
        setContentView(R.layout.activity_hungry);

        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(this, R.raw.hungry);
        bgm.setAudioStreamType(AudioManager.STREAM_MUSIC);

        bgm.start();

    button = (Button) findViewById(R.id.button12);
    // when game start button is clicked, move to generating activity
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view){

            //navigate to AMaze activity
            Intent intent = new Intent(HungryActivity.this, AMazeActivity.class);
            //send message
            startActivity(intent);
            //Snackbar.make(coordinatorLayout, "Return to Main Screen", Snackbar.LENGTH_LONG).show();
            Log.v("Navigate", "From HungryActivity to AmazeActivity");

        }
    });
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
