package edu.wm.cs.cs301.amazebyyunwoonoh.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Spinner;

import edu.wm.cs.cs301.amazebyyunwoonoh.R;


public class AMazeActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    SeekBar seekbar;
    TextView tv_val;
    Spinner spinner;
    Spinner spinner2;
    Button button;
    int difficulty = 0;
    String algo;
    String driver;
    MediaPlayer bgm;
    CheckBox checkBox;

    /**
     * When the activity is started following should start:
     * background music, seek bar with according number value,
     * display spinner and button. Once button is clicked sent intent
     * with difficulty, algorithm, and driver and navigate to generatingActivity
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
        setContentView(R.layout.activity_amaze);

        bgm = new MediaPlayer();
        bgm = MediaPlayer.create(this, R.raw.opening);
        bgm.setAudioStreamType(AudioManager.STREAM_MUSIC);

        bgm.start();


        seekbar = (SeekBar) findViewById(R.id.seekBar);
        tv_val = (TextView) findViewById(R.id.tv_val);
        // display seekbar and according integer value below
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_val.setText("Difficulty Level : " + progress);
                //Snackbar.make(coordinatorLayout,"Difficulty: "+ progress,Snackbar.LENGTH_LONG).show();
                Log.v("Generating Maze", "Difficulty: "+progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

        spinner = (Spinner) findViewById(R.id.algo);
        spinner2 = (Spinner) findViewById(R.id.driver);

        //spinner.setPrompt("Maze Algorithm");
        //spinner for Maze Algorithm
        algo = spinner.getSelectedItem().toString();
        //Snackbar.make(coordinatorLayout,"Algorithm: "+ algo,Snackbar.LENGTH_LONG).show();
        Log.v("Generating Maze", "Algorithm: "+algo);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}


        });
        //spinner for driver
        driver = spinner2.getSelectedItem().toString();
        //Snackbar.make(coordinatorLayout,"Driver: "+ driver,Snackbar.LENGTH_LONG).show();
        Log.v("Generating Maze", "Driver: "+driver);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        button = (Button) findViewById(R.id.newgame);
        // when game start button is clicked, move to generating activity
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //get user input from amazeactivity
                difficulty = seekbar.getProgress();
                algo = spinner.getSelectedItem().toString();
                driver = spinner2.getSelectedItem().toString();


                //navigate to generating activity
                Intent intent = new Intent(AMazeActivity.this, GeneratingActivity.class);
                //send message
                intent.putExtra("Difficulty", difficulty);
                intent.putExtra("Algorithm", algo);
                intent.putExtra("Driver", driver);

                checkBox = (CheckBox) findViewById(R.id.checkBox);
                if (checkBox.isChecked()) {
                    intent.putExtra("CheckBox","checked");
                }
                else{
                    intent.putExtra("CheckBox", "unchecked");
                }

                startActivity(intent);
                //Snackbar.make(coordinatorLayout,"Input given, start generating the maze" ,Snackbar.LENGTH_LONG).show();
                Log.v("Navigate","From AmazeActivity to GeneratingActivity");

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
