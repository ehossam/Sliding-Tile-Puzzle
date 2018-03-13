package team6.slidingtiles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by hsamanoudy on 3/11/2018.
 */

public class AiMode extends GameMode {
    AiPlayer Aiplayer;
    //= new ((NumberMode) gameBoard, );
    private static final String ARGS_GAMEBOARD      = "gameBoard";
    private static final String ARGS_BOARDLAYOUT    = "boardLayout";
    private static final String ARGS_BLANKTILE      = "blankTile";
    ArrayList<String> boardLayout;
    int difficulty;


    /**
     * onCreate for numbermode, is called when the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gameBoard = null;
        difficulty = 0;
        super.onCreate(savedInstanceState);
    }

    /**
     * onSaveInstanceState
     * saves the instance state of the board related fields of this activity
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(ARGS_GAMEBOARD, gameBoard);
        savedInstanceState.putStringArrayList(ARGS_BOARDLAYOUT, boardLayout);
        savedInstanceState.putInt(ARGS_BLANKTILE, blankTile);
    }

    /**
     * onSaveInstanceState
     * restores the saved instance states gameBoard
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameBoard   = savedInstanceState.getParcelable(ARGS_GAMEBOARD);
        boardLayout = savedInstanceState.getStringArrayList(ARGS_BOARDLAYOUT);
        blankTile   = savedInstanceState.getInt(ARGS_BLANKTILE);
    }

    void newGame() {
        super.newGame();
        newGameDialog().show();
    }

    /**
     * createGame is called when a new game needs to be created,
     * and creates a board of the desired type.
     */
    void createGame(){
        gameBoard = new NumberBoard(true, difficulty);
        SetBoard(gameBoard);
        Aiplayer= new AiPlayer((NumberBoard) gameBoard,difficulty);
        //AiPlayer.setBoard():
        super.createGame();


    }

    /**
     * creates a newGameDialog to be displayed, lets user select difficulty
     * @return the Builder which the actionDialog is contained in
     */
    AlertDialog.Builder newGameDialog() {
        CharSequence options[]      = new CharSequence[]{"Easy", "Normal", "Hard"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Difficulty");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        difficulty = 8;
                        break;
                    case 1:
                        difficulty = 16;
                        break;
                    case 2:
                        difficulty = 32;
                        break;
                }
                createGame();
            }
        });
        builder.setCancelable(false);
        return builder;
    }

    /**
     * calls the super class' method and checks if the board is complete.
     * if the board is complete then complete is called which displays a menu to the user
     * @param pos is the position of the tile being swapped with the blank tile
     */
    boolean moveTile(int pos) {
        //boolean success = super.moveTile(pos);
        Aiplayer.makeMove();
        if(((NumberBoard)gameBoard).isComplete())
            complete();
       return true;
       // return success;
    }


    /**
     * checks if the board is complete, which means that the user has won
     * prompts the user to create a new game or exit
     */
    void complete(){
        onPause();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AI Wins!");
        CharSequence options[] = new CharSequence[]{"New game", "Quit"};

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

        builder.setCancelable(false);
        builder.show();
    }
}