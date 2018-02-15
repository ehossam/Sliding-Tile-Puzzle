package team6.slidingtiles;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

/**
 * Created by cheesea on 2/10/18.
 */

public class MathMode extends GameMode  {
    ArrayList<String> boardLayout;
    private static final String ARGS_GAMEBOARD      = "gameBoard";
    private static final String ARGS_BOARDLAYOUT    = "boardLayout";
    private static final String ARGS_BLANKTILE      = "blankTile";

    int score;
    TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        gameBoard = null;
        super.onCreate(savedInstanceState);
        score = 0;
        scoreView = getWindow().getDecorView().findViewById(R.id.my_score);
        scoreView.setText(Integer.toString(score));
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

    void newGame(){
        super.newGame();
        createGame();
    }

    void createGame(){
        gameBoard = new MathBoard(true);
        SetBoard(gameBoard);
        score = 0;
        super.createGame();
    }

    @Override
    public boolean handleSWipe(int start, int end) {
        int x = start % 5;
        int y = start / 5;

        Toast.makeText(this, "blah" + start, Toast.LENGTH_SHORT).show();
        if (x == 0 && y != end / 5)
            score += ((MathBoard) gameBoard).getScore(x, y, false);
        else if (y == 0 && x != end % 5)
            score += ((MathBoard) gameBoard).getScore(x, y, true);
        else return false;
        scoreView.setText(Integer.toString(score));
        return true;
    }
}

