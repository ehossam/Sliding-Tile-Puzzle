package team6.slidingtiles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cheesea on 2/10/18.
 */

public class MathModeMultiSimple extends GameMode implements RoomFinder.RoomFinderListener {
    ArrayList<String> boardLayout;
    private static final String ARGS_GAMEBOARD      = "gameBoard";
    private static final String ARGS_BOARDLAYOUT    = "boardLayout";
    private static final String ARGS_BLANKTILE      = "blankTile";
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    RoomFinder roomFinder;

    AlertDialog matchingDialog;
    int myScore;
    int theirScore;
    TextView theirScoreView;
    TextView myScoreView;
    public HashSet<String> ss=new HashSet<>();
    private Room room;
    int playerNum;
    String roomKey;
    HashSet<String> usedEquations;
    public int no_rounds;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Intent mintent = getIntent();
        no_rounds = mintent.getIntExtra("rounds", 1); //no of rounds passed from previous activity
        gameBoard = null;
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null) {
            finish();
            Intent intent = new Intent(MathModeMultiSimple.this, SigninPage.class);
            startActivity(intent);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        myScoreView = getWindow().getDecorView().findViewById(R.id.my_score);
        theirScoreView = getWindow().getDecorView().findViewById(R.id.their_score);

        ImageButton equationIcon = findViewById(R.id.equation_button);
        equationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                savedequtions().show();
            }
        });

          }

    private void saveScore(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail();
        String[] userName = email.split("@");
        String name = userName[0];

        UserScore userScore = new UserScore(name, myScore);

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
        if(myScore > 0)
          newGameDialog().show();
        else
            createGame();
    }

    void createGame(){
        matchingDialog();

    }

    @Override
    public boolean handleSWipe(int start, int end) {
        int startX = start % 5;
        int startY = start / 5;

        int endX = end % 5;
        int endY = end / 5;


        Toast.makeText(this, "startX: "+ startX+" startY: "+startY + " endX: " + endX + " endY: " + endY , Toast.LENGTH_LONG).show();
        if ((startX == 0 || endX == 0) && startY == endY) {
            myScore += ((MathBoard) gameBoard).getScore(startX, startY, false);
        } else if ((startY == 0 || endY == 0) && startX == endX) {
            myScore += ((MathBoard) gameBoard).getScore(startX, startY, true);
        } else
            return false;
        changeScore();
        usedEquations = ((MathBoard) gameBoard).foundEquations();
        return true;
    }

    public void changeScore(){
        String playerString;
        if(playerNum == 1) {
            room.setP1Score(myScore);
            playerString = "p1Score";
        } else {
            room.setP2Score(myScore);
            playerString = "p2Score";
        }
        databaseReference.child("rooms").child(room.getKey()).child(playerString).setValue(myScore);
    }

    public void matchingDialog(){
        Log.d(" matchingdialog", ": ");
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setMessage("Finding match");

        adBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
//                finish();
            }
        });
        adBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        adBuilder.setView(findViewById(R.id.matching_dialog_view));
        matchingDialog = adBuilder.create();
        matchingDialog.show();
        roomFinder = new RoomFinder(this);
        roomFinder.getOpenRoom();
    }

    @Override
    public void roomFound() {

        room = roomFinder.getRoom();
        int total_rounds = room.getNoRounds();
        playerNum = roomFinder.getPlayerNum();
        for(int i=0;i<total_rounds;i++)
            {
             Log.d("round in roomFound ",String.valueOf(i) ) ;
             Log.d("player in roomFound ",String.valueOf(playerNum) ) ;

                if(playerNum == 1 && i>0){
                 //let 1 player update the mathboard
                 MathBoard mathBoard = new MathBoard(false);
                 List<String> mathBoardList = new ArrayList<>();
                 for (int j = 0; j < mathBoard.getBoard().length; j++){
                      mathBoardList.addAll(Arrays.asList(mathBoard.getBoard()[j]));
                     }
                 room.setInitBoardState(mathBoardList);
             }
              this.gameBoard = new MathBoard(room.getInitBoardState());
              SetBoard(this.gameBoard);
              updateScores();
              matchingDialog.cancel();
              super.createGame();
            }
    }

    public void updateScores(){
        myScoreView.setText(Integer.toString(myScore));
        theirScoreView.setText(Integer.toString(theirScore));
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            matchingDialog.dismiss();
            String key = dataSnapshot.getKey();
            Log.d("onChildChanged", "onChildChanged: " + key);
            if(key.equals("p1Score")){
                if(playerNum == 1){
                    myScore = dataSnapshot.getValue(Integer.class);
                } else {
                    theirScore = dataSnapshot.getValue(Integer.class);
                }
                updateScores();
            }
            if(key.equals("p2Score")) {
                if(playerNum == 1){
                    theirScore = dataSnapshot.getValue(Integer.class);
                } else {
                    myScore = dataSnapshot.getValue(Integer.class);
                }
                updateScores();
            }

            if(key.equals("isOpen") )
                roomFound();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}


