package edu.wm.cs.cs301.amazebyyunwoonoh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.wm.cs.cs301.amazebyyunwoonoh.R;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.MazeController;
import edu.wm.cs.cs301.amazebyyunwoonoh.falstad.Robot;

public class GeneratingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;
    private int difficulty;
    private String algo;
    private String driver;
    private int percent;
    private Handler handler = new Handler();
    private boolean save = false;
    private CoordinatorLayout coordinatorLayout;
    Button backButton;
    private boolean back;
    private String check;


    private static MazeController mazeController;


    /**
     * Once this activity begins, it displays a progress bar which indicate the progress of
     * building the maze with user input.
     * Also it displays a backbutton for user to return back to main title screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);


        //back button to return to the main title screen
        backButton = (Button) findViewById(R.id.gen_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(GeneratingActivity.this, AMazeActivity.class);
                startActivity(intent);
                Snackbar.make(coordinatorLayout, "Return to Main Screen", Snackbar.LENGTH_LONG).show();
                Log.v("Navigate", "From GeneratingActivity to AmazeActivity");
            }
        });

        //display loading progress bar with percentage
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        textView = (TextView) findViewById(R.id.textView2);


        Intent intent = getIntent();
        difficulty = intent.getIntExtra("Difficulty",0);
        algo = intent.getStringExtra("Algorithm").toString();
        driver = intent.getStringExtra("Driver").toString();
        check = intent.getStringExtra("CheckBox").toString();

        if(check.equals("checked")){
            if(difficulty < 4 && save){
                //reload from existing mazes
                mazeController = new MazeController("Maze"+String.valueOf(difficulty)+".xml");
                mazeController.setUserInputs(difficulty, algo, driver);
                mazeController.setGeneratingAct(this);

                mazeController.init();
            }
            else{
                //difficulty level is to high to generate new maze
                mazeController = new MazeController();
                mazeController.setUserInputs(difficulty, algo, driver);
                mazeController.setGeneratingAct(this);

                mazeController.init();
            }

        }
        else{
            //generate new maze
            mazeController = new MazeController();
            mazeController.setUserInputs(difficulty, algo, driver);
            mazeController.setGeneratingAct(this);

            mazeController.init();
        }




        //display information given from AMazeActivity in snack bar and log
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);

    }


    public void playMode(){
        if(!back && driver.equals("Manual")) {
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        }
        else if(!back && !driver.equals("Manual")){
            Intent intent = new Intent(this, RobotActivity.class);
            startActivity(intent);
        }
    }

    public void progressPercent(int i){
        percent = i;
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(percent);
                textView.setText("Loading" + percent + "%...");
            }
        });
        try{
            Thread.sleep(10);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(GeneratingActivity.this, AMazeActivity.class);
                startActivity(intent);
                back = true;
            }
        });

    }

    public static void setMazeController(MazeController mazeController){
        GeneratingActivity.mazeController = mazeController;
    }

    public static MazeController getMazeController(){
        return mazeController;
    }






}
