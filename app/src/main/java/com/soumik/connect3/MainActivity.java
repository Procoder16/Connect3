package com.soumik.connect3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    //0=yellow,1=red,2=empty

    int activePlayer=0;
    int[] gameState={2,2,2,2,2,2,2,2,2};

    public void dropIn(View view){

        ImageView counter=(ImageView)view;
        int tappedCounter=Integer.parseInt(counter.getTag().toString());
        gameState[tappedCounter]=activePlayer;
        counter.setTranslationY(-1500);
        if(activePlayer==0){
        counter.setImageResource(R.drawable.yellow);
        activePlayer=1;
        }
        else{
            counter.setImageResource(R.drawable.red);
            activePlayer=0;
        }
        counter.animate().translationYBy(1500).setDuration(300);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}