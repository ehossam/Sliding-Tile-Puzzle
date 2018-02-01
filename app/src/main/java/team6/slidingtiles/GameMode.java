package team6.slidingtiles;

import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Arrays;


public abstract class GameMode extends AppCompatActivity implements fragment01.SelectionHandler {
    public final String ARG_BOARD = "boardArray";
    int         difficulty;
    Toolbar     toolbar;
    ImageView menuIcon;
    LinearLayout toolbarLayout;
    fragment01 boardFragment;
    ArrayList<String> boardLayout;
    int blankTile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        boardFragment = fragment01.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentFrame, boardFragment).commit();


        menuIcon = new ImageView(this);
        menuIcon.setImageDrawable(getDrawable(R.drawable.pause_menu));
        menuIcon.setPadding(10,10,10,10);
        menuIcon.setId(View.generateViewId());
        menuIcon.setScaleType(ImageView.ScaleType.FIT_END);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                pause().show();
            }
        });

        if (toolbarLayout == null)
            toolbarLayout = new LinearLayout(this);

        toolbarLayout.addView(menuIcon);
        toolbar.addView(toolbarLayout);

        difficulty = 0;
        newGame();


    }

    AlertDialog.Builder newGameDialog() {
        CharSequence options[] = new CharSequence[]{"easy", "normal", "hard"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Difficulty");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        difficulty = 1;
                        break;
                    case 1:
                        difficulty = 2;
                        break;
                    case 2:
                        difficulty = 3;
                        break;
                }
                createGame();
            }
        });
        builder.setCancelable(false);

        return builder;
    }

    ArrayList<String> convertDimm(String[][] oldArray){
        ArrayList<String> tmp = new ArrayList<>();
        for (String[] array : oldArray) {
            tmp.addAll(Arrays.asList(array));
        }
        return tmp;
    }

    void newGame(){
        AlertDialog.Builder builder = newGameDialog();
        builder.show();
    }

    void SetBoard(Board board) {

        boardLayout = convertDimm(board.getBoard());
        for(int i = 0; i < boardLayout.size(); i++)
            if (boardLayout.get(i).compareTo(" ")==0)
                blankTile = i;
        boardFragment.setBoardLayout(boardLayout);

    }

    abstract void createGame();

    //displays the pause menu and pauses the timer
    AlertDialog.Builder pause(){
        CharSequence options[] = new CharSequence[]{"Resume", "New game", "quit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pause_menu);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        onResume();
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
                onResume();
            }
        });
        return builder;
    }

    abstract boolean moveTile(int pos);

    public boolean handleSelection(int pos){
        return moveTile(pos);
    }

}

