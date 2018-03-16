package team6.slidingtiles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by cheesea on 2/10/18.
 */

public class MathMode extends GameMode  {
    ArrayList<String> boardLayout;
    private static final String ARGS_GAMEBOARD      = "gameBoard";
    private static final String ARGS_BOARDLAYOUT    = "boardLayout";
    private static final String ARGS_BLANKTILE      = "blankTile";
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;



    int score;
    TextView scoreView;
    public HashSet<String> ss=new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        gameBoard = null;

        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null) {
            //closing this activity
            finish();
            //start new activity
            Intent intent = new Intent(MathMode.this, SigninPage.class);
            startActivity(intent);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        score = 0;
        scoreView = getWindow().getDecorView().findViewById(R.id.my_score);
        scoreView.setText(Integer.toString(score));
    }

    private void saveScore(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail();
        String[] userName = email.split("@");
        String name = userName[0];

        UserScore userScore = new UserScore(name, score);

        databaseReference.child(databaseReference.push().getKey()).setValue(userScore);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(ARGS_GAMEBOARD, gameBoard);
        savedInstanceState.putStringArrayList(ARGS_BOARDLAYOUT, boardLayout);
        savedInstanceState.putInt(ARGS_BLANKTILE, blankTile);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameBoard = savedInstanceState.getParcelable(ARGS_GAMEBOARD);
        boardLayout = savedInstanceState.getStringArrayList(ARGS_BOARDLAYOUT);
        blankTile = savedInstanceState.getInt(ARGS_BLANKTILE);
    }

    AlertDialog.Builder newGameDialog() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setMessage("Submit Score?");
        adBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveScore();
                createGame();
            }
        });
        adBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createGame();
            }
        });
        return adBuilder;
    }



    AlertDialog.Builder savedequtions() {
       final AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);

        ss=(HashSet)(((MathBoard) gameBoard).foundEquations()).clone();
        Iterator iterator = ss.iterator();
        String msg="";
        while (iterator.hasNext()) {
            msg+=(String)iterator.next() + "\n";
            adBuilder.setMessage(msg);
        }

        adBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            }
        });

        return adBuilder;
    }

    void newGame(){
        super.newGame();
        if(score > 0)
          newGameDialog().show();
        else
            createGame();
    }

    void createGame(){
        score = 0;
        scoreView.setText(Integer.toString(score));
        gameBoard = new MathBoard(false);
        CountDownTimer createTimer = new CountDownTimer(30, 1) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                gameBoard = new MathBoard(false);
                MathMode.super.createGame();

            }
        };
        createTimer.start();
    }

    @Override
    public boolean handleSWipe(int start, int end) {
        int startX = start % 5;
        int startY = start / 5;

        int endX = end % 5;
        int endY = end / 5;


        Toast.makeText(this, "startX: "+ startX+" startY: "+startY + " endX: " + endX + " endY: " + endY , Toast.LENGTH_LONG).show();
        if ((startX == 0 || endX == 0) && startY == endY) {
            score += ((MathBoard) gameBoard).getScore(startX, startY, false);
        } else if ((startY == 0 || endY == 0) && startX == endX) {
            score += ((MathBoard) gameBoard).getScore(startX, startY, true);
        } else return false;
        scoreView.setText("My score: " + Integer.toString(score));
        return true;
    }
}

