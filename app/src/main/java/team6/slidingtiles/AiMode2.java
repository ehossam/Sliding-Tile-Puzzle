package team6.slidingtiles;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class AiMode2 extends AppCompatActivity
        implements BoardFragment.SelectionHandler, AiBoardFragment.AiSelectionHandler{
    ArrayList<String> boardLayout;
    ArrayList<String> boardLayout2;
    BoardFragment   boardFragment1;
    AiBoardFragment   boardFragment2;

    Chronometer     timer;
    int     blankTile;
    long    timePaused;
    NumberBoard gameBoard1;
    NumberBoard gameBoard2;
    AiPlayer testPlayer = new AiPlayer();
    AiTask aiTask;


    private static final String ARGS_GAMEBOARD      = "gameBoard";
    private static final String ARGS_BOARDLAYOUT    = "boardLayout";
    private static final String ARGS_BLANKTILE      = "blankTile";
    int difficulty;


    //AiPlayer Aiplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_mode2);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        timer = findViewById(R.id.timer);
        timePaused = 0;

        ImageButton menuIcon = findViewById(R.id.menu_button);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                pauseTimer();
                pause().show();
            }
        });

        boardFragment2 = AiBoardFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentFrame2, boardFragment2).commit();
    }





    /**
     * onResume is called when the activity becomes visible again
     * if a Board exists then it will resume the timer
     */
    @Override
    protected void onResume(){
        super.onResume();
        if(gameBoard1!=null)
            resumeTimer();
    }

    /**
     * onPause is called when the activity loses focus,
     * in which case it pauses the timer.
     */
    @Override
    protected void onPause(){
        super.onPause();
        pauseTimer();
    }

    /**
     * pauses the timer
     */
    void pauseTimer(){
        timePaused = timer.getBase() - SystemClock.elapsedRealtime();
        timer.stop();
    }

    /**
     * resumes the timer
     */
    void resumeTimer(){
        timer.setBase(SystemClock.elapsedRealtime() + timePaused);
        timer.start();
    }


    /**
     * convert from 2d array to ArrayList
     * @param oldArray the 2d array representing the board
     * @return an ArrayList representing the board
     */
    ArrayList<String> convertDimm(String[][] oldArray){
        ArrayList<String> tmp = new ArrayList<>();
        for (String[] array : oldArray) {
            tmp.addAll(Arrays.asList(array));
        }
        return tmp;
    }

    /**
     * displays the new game menu
     */
    void newGame(){
        pauseTimer();
    }

    /**
     * updates the board currently being displayed
     * @param board the new board
     */
    void SetAiBoard(Board board) {
        boardLayout = convertDimm(board.getBoard());
        for(int i = 0; i < boardLayout.size(); i++)
            if (boardLayout.get(i).compareTo(" ")==0)
                blankTile = i;
        boardFragment2.setBoardLayout(boardLayout);
       // boardFragment2.setBoardLayout(boardLayout);
    }

    void SetBoard(Board board) {
        boardLayout = convertDimm(board.getBoard());
        for(int i = 0; i < boardLayout.size(); i++)
            if (boardLayout.get(i).compareTo(" ")==0)
                blankTile = i;
        boardFragment1.setBoardLayout(boardLayout);
        // boardFragment2.setBoardLayout(boardLayout);
    }



    /**
     * starts the timer. this must be called from child classes
     */
    void createGame(){
        timePaused = 0;
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    /**
     * creates and returns an AlterDialog with the pausemenu inside of it
     * @return Builder containing the pause menu
     */
    AlertDialog.Builder pause(){
        CharSequence options[] = new CharSequence[]{"Resume", "New game", "Quit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pause_menu);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        resumeTimer();
                        break;
                    case 1:
                        newGame();
                        break;
                    case 2:
                        finish();
                        break;
                }
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                resumeTimer();
            }
        });
        return builder;
    }

    /**
     * tries to swap the blank tile with the selected tile
     * @param pos the selected tile
     * @return if the operation was successful
     */
    boolean moveTile(int pos){
        int x = pos % 5;
        int y = pos / 5;

        if(gameBoard1.swapTiles(x,y)) {
            boardLayout = convertDimm(gameBoard1.getBoard());
            boardFragment1.setBoardLayout(boardLayout);
            blankTile = pos;
            return true;
        }
        return false;
    }

    /**
     * checks if the board is complete, which means that the user has won
     * prompts the user to create a new game or exit
     */

    /**
     * method for the handler in BoardFragment
     * @param pos the position of the selected tile
     * @return if the tile was successfully swapped
     */
    public boolean handleSelection(int pos){
        return moveTile(pos);
    }

    public boolean handleSWipe(int start, int end){
        return true;
    }
    public void fragmentReady(){
        newGame();
    }

    public void aiFragmentReady(){
        boardFragment1 = BoardFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentFrame1, boardFragment1).commit();
    }

    public void updateAiBoard(State.Location result){
        gameBoard2.swapTiles(result.getX(), result.getY());
        SetAiBoard(gameBoard2);
        if(gameBoard2.isComplete())
            complete_2();
    }

    static class AiTask extends AsyncTask<Void, Void, State.Location> {
        AiPlayer aiPlayer;
        Board board;
        WeakReference<AiMode2> aiNumModeWeakReference;

        AiTask(AiMode2 aiMode2, AiPlayer aiPlayer, Board board){
            aiNumModeWeakReference = new WeakReference<AiMode2>(aiMode2);
            this.aiPlayer = aiPlayer;
            this.board = board;
        }

        @Override
        protected State.Location doInBackground(Void... params) {
            return aiPlayer.getNextMove();
        }

        @Override
        protected void onPostExecute(State.Location location) {
            if (aiNumModeWeakReference.get() != null)
                aiNumModeWeakReference.get().updateAiBoard(location);
        }
    }

    void complete_2(){
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
