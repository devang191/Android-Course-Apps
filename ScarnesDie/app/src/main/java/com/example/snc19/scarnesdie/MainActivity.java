package com.example.snc19.scarnesdie;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by snc19 on 11/9/17.
 */

public class MainActivity extends AppCompatActivity {

    private int[] totalScore;
    private int[] turnScore;
    private int[] totalScoreTextView;
    private int[] turnScoreTextView;
    private short userNo;
    private boolean gameOver;
    private Timer t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalScore = new int[2];
        turnScore = new int[2];
        totalScoreTextView = new int[2];
        turnScoreTextView = new int[2];

        totalScoreTextView[0] = R.id.your_score;
        totalScoreTextView[1] = R.id.comp_score;

        turnScoreTextView[0] = R.id.your_turn_score;
        turnScoreTextView[1] = R.id.computer_turn_score;

        gameOver = false;
        userNo = 0;
    }

    private void updateTextView(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    public void hold() {
        totalScore[userNo] += turnScore[userNo];
        turnScore[userNo] = 0;
        updateTextView(totalScoreTextView[userNo], "Score: " + totalScore[userNo]);
        updateTextView(turnScoreTextView[userNo], "Turn Score: 0");
        userNo = (short) ((userNo + 1) % 2);
        if (userNo == 1){
            t = new Timer();
            TimerTask tt = new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            roll();
                        }
                    });
                }
            };
            t.scheduleAtFixedRate(tt, 1000, 1000 * 2);
        }
        else{
            t.cancel();
        }

}
    public void reset(){
        if(userNo==1){
            t.cancel();
            userNo=0;
        }
        totalScore[0] = 0;
        totalScore[1] = 0;
        turnScore[0] = 0;
        turnScore[1] = 0;

        updateTextView(totalScoreTextView[0],"Score: "+totalScore[0]);
        updateTextView(totalScoreTextView[1],"Score: "+totalScore[1]);

        updateTextView(turnScoreTextView[0],"Turn Score: "+turnScore[0]);
        updateTextView(turnScoreTextView[1],"Turn Score: "+turnScore[1]);
    }

    public void rollClicked(View view){
        if(userNo==0)
            roll();
    }

    public void holdClicked(View view){
        if(userNo==0)
            hold();
    }

    public void resetClicked(View view){
        if(userNo==0)
            reset();
    }

    public void roll(){
        Random random = new Random();
        int rolledValue = random.nextInt(6) + 1 + userNo;
        int id = R.drawable.dice1;
        switch (rolledValue){
            case 2:
                id = R.drawable.dice2;
                break;
            case 3:
                id = R.drawable.dice3;
                break;
            case 4:
                id = R.drawable.dice4;
                break;
            case 5:
                id = R.drawable.dice5;
                break;
            case 6:
                id = R.drawable.dice6;
                break;
            default:
                break;
        }
        if(rolledValue==7){
            hold();
            return;
        }else{
            turnScore[userNo]+=rolledValue;
            updateTextView(turnScoreTextView[userNo],"Turn Score: "+turnScore[userNo]);

            if(rolledValue==1){
                turnScore[userNo]=0;
                hold();
            }else if(totalScore[userNo]+turnScore[userNo]>=100){
                String user = "You Won";
                if(userNo==1)
                    user = "Computer Won";
                Toast.makeText(this,user,Toast.LENGTH_LONG).show();
                reset();
            }
            ((ImageView)findViewById(R.id.diceimage)).setImageResource(id);
        }

    }
}
