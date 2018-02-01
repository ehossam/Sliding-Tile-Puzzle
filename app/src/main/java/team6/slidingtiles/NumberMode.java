package team6.slidingtiles;

import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.MeasureFormat;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;


public class NumberMode extends GameMode implements fragment01.SelectionHandler {
    private static final String ARGS_GAMEBOARD = "gameBoard";
    private static final String ARGS_BOARDLAYOUT = "boardLayout";
    private static final String ARGS_BLANKTILE = "blankTile";
    Chronometer timer;
    long        timePaused;
    NumberBoard gameBoard;
    ArrayList<String> boardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gameBoard = null;
        timePaused = 0;
        toolbarLayout = new LinearLayout(this);
        timer = new Chronometer(this);
        timer.setGravity(Gravity.CENTER);
        timer.setTextSize(40);
        timer.setTextColor(Color.parseColor("#ffffff"));

        toolbarLayout.addView(timer);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putParcelable(ARGS_GAMEBOARD, gameBoard);
        savedInstanceState.putStringArrayList(ARGS_BOARDLAYOUT, boardLayout);
        savedInstanceState.putInt(ARGS_BLANKTILE, blankTile);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        gameBoard = savedInstanceState.getParcelable(ARGS_GAMEBOARD);
        boardLayout = savedInstanceState.getStringArrayList(ARGS_BOARDLAYOUT);
        blankTile = savedInstanceState.getInt(ARGS_BLANKTILE);
    }

    //Once activity is visible, start the timer
    @Override
    protected void onResume(){
        super.onResume();
        if(gameBoard!=null)
            resumeTimer();
    }

    //timer needs to be stopped when activity is not in the foreground
    @Override
    protected void onPause(){
        super.onPause();
        pauseTimer();
    }

    //onClick for finish and pause.
//    @Override
/*    public void onClick(View view){
        if (view == pauseBttn)
            pauseTimer();
        super.onClick(view);
    }
*/
    void pauseTimer(){
        timePaused = timer.getBase() - SystemClock.elapsedRealtime();
        timer.stop();
    }

    void resumeTimer(){
        timer.setBase(SystemClock.elapsedRealtime() + timePaused);
        timer.start();
    }

    void newGame() {
        pauseTimer();
        super.newGame();
    }

    void createGame(){
        gameBoard = new NumberBoard(true, difficulty);
        timer.setBase(SystemClock.elapsedRealtime());
        timePaused = 0;
        timer.start();
        SetBoard(gameBoard);
    }

    boolean moveTile(int pos) {
        int x = pos % 5;
        int y = pos / 5;
        if(gameBoard.swapTiles(x,y)) {
            boardLayout = convertDimm(gameBoard.getBoard());
            boardFragment.setBoardLayout(boardLayout);
            blankTile = pos;
            if(gameBoard.isComplete())
                complete();
            return true;
        }
        return false;
    }

    void complete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You Win!");
        CharSequence options[] = new CharSequence[]{"New game", "quit"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        newGame();
                        break;
                    case 1:
                        finish();
                        break;
                }
            }
        });

        pauseTimer();
        builder.setCancelable(false);
        builder.show();

    }

    //displays the pause menu and pauses the timer
    AlertDialog.Builder pause(){
        pauseTimer();
        return super.pause();
    }



}
