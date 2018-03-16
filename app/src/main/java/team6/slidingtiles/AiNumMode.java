package team6.slidingtiles;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AiNumMode extends AiMode2 {

    private static final String ARGS_GAMEBOARD      = "gameBoard";
    private static final String ARGS_BOARDLAYOUT    = "boardLayout";
    private static final String ARGS_BLANKTILE      = "blankTile";
    ArrayList<String> boardLayout;
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gameBoard1 = null;
        gameBoard2=null;
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
        savedInstanceState.putParcelable(ARGS_GAMEBOARD, gameBoard1);
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
        gameBoard1   = savedInstanceState.getParcelable(ARGS_GAMEBOARD);
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
        gameBoard1 = new NumberBoard(true, difficulty);
        SetBoard(gameBoard1);
        gameBoard2=new NumberBoard(gameBoard1);
        testPlayer.setBoard(gameBoard2);
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
        boolean success = super.moveTile(pos);

        if((gameBoard1).isComplete())
            complete();
        aiTask = new AiTask(this, testPlayer, gameBoard2);
        aiTask.execute();
        return success;
    }

    /**
     * checks if the board is complete, which means that the user has won
     * prompts the user to create a new game or exit
     */
    void complete(){
        aiTask.cancel(true);
        onPause();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You Win!");
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