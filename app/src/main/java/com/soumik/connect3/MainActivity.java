package com.soumik.connect3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import androidx.gridlayout.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //0=yellow,1=red,2=empty

    int activePlayer=0;
    int[] gameState={2,2,2,2,2,2,2,2,2};
    int[][] winningPositions={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{2,4,6},{0,4,8}};
    boolean gameActive=true;

    private TextView gameResultText;
    private Button playAgainButton;
    /** Animation, that's runs after someone has won */
    private @Nullable AnimatorSet currentWinnerAnim;

    public void dropIn(View view){

        ImageView counter=(ImageView)view;

        int tappedCounter=Integer.parseInt(counter.getTag().toString());

        if(gameState[tappedCounter]==2 && gameActive){

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

            counter.animate().translationYBy(1500).rotation(1800).setDuration(600);

            for(int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                    //Someone has won!!!!
                    gameActive=false;
                    String winner;
                    if (activePlayer == 1) {
                        winner = "Yellow";
                    } else {
                        winner = "Red";
                    }

                    gameResultText.setText(winner + " has won!!");
                    gameResultText.setVisibility(View.VISIBLE);
                    playAgainButton.setVisibility(View.VISIBLE);
                    startHighlightingWinner(winningPosition);
                }
            }

            if(! Arrays.toString(gameState).contains("2")) {
                //Game is a draw!!!!
                gameActive = false;

                gameResultText.setText("It's a draw!!");
                gameResultText.setVisibility(View.VISIBLE);
                playAgainButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startHighlightingWinner(int[] winPos) {
        ViewGroup gridLayout = findViewById(R.id.gridLayout);

        List<Animator> anims = new ArrayList<>(winPos.length);
        for(int pos : winPos) {
            ImageView coin = findCoinByPos(gridLayout, pos);
            Animator coinAnim = createWinAnim(coin);
            anims.add(coinAnim);
        }

        AnimatorSet coinsAnim = new AnimatorSet();
        coinsAnim.playTogether(anims);
        coinsAnim.start();
        currentWinnerAnim = coinsAnim;
    }

    private ImageView findCoinByPos(ViewGroup groupToSearch, int counterPos) {
        String tag = String.valueOf(counterPos);
        return groupToSearch.findViewWithTag(tag);
    }

    private Animator createWinAnim(View view) {
        long durationMs = 800;
        int repeatCount = Animation.INFINITE;
        float minAlpha = 0.2f;
        float maxAlpha = 1f;

        ObjectAnimator fadeOutAndIn = ObjectAnimator.ofFloat(view, View.ALPHA, maxAlpha, minAlpha,
                maxAlpha);
        fadeOutAndIn.setDuration(durationMs);
        fadeOutAndIn.setRepeatCount(repeatCount);
        return fadeOutAndIn;
    }

    public void playAgain(View view){
        stopHighlightingWinner();
        gameResultText.setVisibility(View.INVISIBLE);
        playAgainButton.setVisibility(View.INVISIBLE);

        GridLayout gridLayout=findViewById(R.id.gridLayout);
        for(int i=0;i<gridLayout.getChildCount();i++){
            ImageView counter=(ImageView)gridLayout.getChildAt(i);
            counter.setImageDrawable(null);
        }
        Arrays.fill(gameState, 2);
        gameActive=true;
        activePlayer=0;
    }

    private void stopHighlightingWinner() {
        if(currentWinnerAnim == null)
            return;
        currentWinnerAnim.cancel();
        currentWinnerAnim = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameResultText=findViewById(R.id.gameResultTextView);
        playAgainButton=findViewById(R.id.playAgainButton);
    }
}