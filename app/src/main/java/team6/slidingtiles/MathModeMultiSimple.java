package team6.slidingtiles;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import java.util.LinkedHashSet;
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
    TextView usedEquationText;
    int myScore;
    int theirScore;
    TextView theirScoreView;
    TextView myScoreView;
    public LinkedHashSet<String> ss=new LinkedHashSet<>();
    private Room room;
    int playerNum;
    String lastUsed;
    HashSet<String> usedEquations;
    public int no_rounds;
    List<String> newBoard = new ArrayList<>();
    List<String> winners = new ArrayList<>();
    String name; //username of players

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Intent mintent = getIntent();
        no_rounds = mintent.getIntExtra("rounds", 1); //no of rounds passed from previous activity
        gameBoard = null;
        Log.d("round oncreate", "round: " + no_rounds);
        canPause = false;

        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null) {
            finish();
            Intent intent = new Intent(MathModeMultiSimple.this, SigninPage.class);
            startActivity(intent);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = user.getEmail();
        String[] userName = email.split("@");
        name = userName[0];

        myScoreView = getWindow().getDecorView().findViewById(R.id.my_score);
        //myScoreView.setText(Integer.toString(0));
        theirScoreView = getWindow().getDecorView().findViewById(R.id.their_score);
        usedEquationText = getWindow().getDecorView().findViewById(R.id.center_button);
        usedEquationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                savedequtions().show();
            }
        });
    }

    @Override
    protected void onStop() {
        if(room != null) {
            databaseReference.child("rooms").child(room.getKey()).removeEventListener(childEventListener);
            databaseReference.child("rooms").child(room.getKey()).child("isOpen").removeValue();
        }
        super.onStop();
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
        if(myScore < theirScore)
            adBuilder.setTitle("You lose");
        if(myScore > theirScore)
            adBuilder.setTitle("You Win");
        else
            adBuilder.setTitle("You Tied");

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


    AlertDialog.Builder winnerDialog() {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setTitle("Winner ");
        String message = "";
        for(int i=0;i<winners.size();i++){
           message += " Round "+ (i+1) +" : "+ winners.get(i) + "\n";
        }
        adBuilder.setMessage(message);
        adBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MathModeMultiSimple.this,PlayerMode.class);
                startActivity(intent);
            }
        });
        return adBuilder;
    }



    AlertDialog.Builder savedequtions() {
       final AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);

        ss=(LinkedHashSet)(((MathBoard) gameBoard).foundEquations()).clone();
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

    void findWinner(){
        String property = "winners";
              if(myScore>theirScore){
                winners.add(name);
                databaseReference.child("rooms").child(room.getKey()).child(property).setValue(winners);
            }
            else if(myScore==theirScore) {
                winners.add("Tie Occurred");
                databaseReference.child("rooms").child(room.getKey()).child(property).setValue(winners);
            }
        if(no_rounds < 1){
            winnerDialog().show();
            databaseReference.child("rooms").child(room.getKey()).child("initBoardState").setValue(boardLayout);
        }
    }

    void newGame(){
        super.newGame();
        if(myScore > 0)
          newGameDialog().show();
        else
            createGame();
    }

    void createGame(){
        if(roomFinder==null){
            Log.d(" creategame", "will show match dialog ");
            Log.d(" creategame round", String.valueOf(no_rounds));
            Log.d(" creategame room", String.valueOf(roomFinder));
            matchingDialog();

        }
        else if(no_rounds>=1){//player who selects new game - going to

            Log.d(" creategame round", String.valueOf(no_rounds));
            Log.d("round --", "round: " + no_rounds);
            myScore = 0;
            theirScore = 0;
            updateScores();
            String nameBoard = "initBoardState";
            gameBoard = new MathBoard(false);
            List<String> mathBoardList = new ArrayList<>();
            for (int i = 0; i < gameBoard.getBoard().length; i++){
                mathBoardList.addAll(Arrays.asList(gameBoard.getBoard()[i]));
            }

            databaseReference.child("rooms").child(room.getKey()).child(nameBoard).setValue(mathBoardList);
            Log.d(" creategame1 else", String.valueOf(mathBoardList));
            SetBoard(gameBoard);
        }
        lastUsed = " ";
    }

   @Override
    public boolean handleSWipe(int start, int end) {
        int startX = start % 5;
        int startY = start / 5;

        int endX = end % 5;
        int endY = end / 5;


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

    void endGame(){
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        if(myScore < theirScore)
            adBuilder.setTitle("You lose");
        if(myScore > theirScore)
            adBuilder.setTitle("You Win");
        else
            adBuilder.setTitle("You Tied");

        if(myScore > 0) {
        adBuilder.setMessage("Submit Score?");
        adBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveScore();
                MathModeMultiSimple.super.endGame();
            }
        });
        adBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MathModeMultiSimple.super.endGame();
            }
        });
    } else super.endGame();



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
                roomFinder.removeRoom();
                finish();
            }
        });

        adBuilder.setView(findViewById(R.id.matching_dialog_view));
        matchingDialog = adBuilder.create();
        matchingDialog.setCancelable(false);
        matchingDialog.show();

        roomFinder = new RoomFinder(this, "MathModeMultiSimple", no_rounds);
        Log.d(" roomfinder.getopenroom", ": ");
        roomFinder.getOpenRoom();
    }

    @Override
    public void roomFound() {
        room = roomFinder.getRoom();
        myScore = 0;
        theirScore = 0;
        playerNum = roomFinder.getPlayerNum();
        this.gameBoard = new MathBoard(room.getInitBoardState());
        lastUsed = "no equations played";
        updateScores();

    }

    public void updateScores(){
        myScoreView.setText("You: " + Integer.toString(myScore));
        theirScoreView.setText("Them: " + Integer.toString(theirScore));
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Object value = dataSnapshot.getValue();
            Log.d("onChildChanged", "onChildChanged: " + key);
            switch (key) {
                case "p1Score":
                    if (playerNum == 1) {
                        myScore =  dataSnapshot.getValue(Integer.class);
                        lastUsed = (String) usedEquations.toArray()[usedEquations.size() - 1];
                        usedEquationText.setText("You played:\n"+lastUsed);
                        databaseReference.child("rooms").child(room.getKey()).child("lastUsed").
                                setValue(lastUsed);
                    } else {
                        theirScore =  dataSnapshot.getValue(Integer.class);
                    }
                    updateScores();
                    break;

                case "p2Score":
                    if (playerNum == 1) {
                        theirScore = dataSnapshot.getValue(Integer.class);
                    } else {
                        myScore = dataSnapshot.getValue(Integer.class);
                        lastUsed = (String) usedEquations.toArray()[usedEquations.size() - 1];
                        usedEquationText.setText("You played:\n"+lastUsed);
                        databaseReference.child("rooms").child(room.getKey()).child("lastUsed").
                                setValue(lastUsed);
                    }
                    updateScores();
                    break;

                case "isOpen":
                    if (!dataSnapshot.getValue(Boolean.class)) {
                        roomFound();
                        databaseReference.child("rooms").child(room.getKey()).
                                child("roundStarted").setValue(true);
                    }
                    break;
                case "roundStarted":
                    MathModeMultiSimple.super.createGame();
                    matchingDialog.dismiss();
                    break;
                case "lastUsed":
                    if (!lastUsed.equals(dataSnapshot.getValue(String.class))){
                        lastUsed = dataSnapshot.getValue(String.class);
                        usedEquationText.setText("they played:\n"+lastUsed);
                    }
                    break;
                case "initBoardState":
                   Log.d("debugchange", "round: " + no_rounds);

                   newBoard = (List<String>)dataSnapshot.getValue();
                   gameBoard = new MathBoard(newBoard);

//                 Log.d("onChildChanged", "snapshot: " + newBoard);
                   Log.d("onChildChanged", "snap value: " + dataSnapshot.getValue());
                   MathModeMultiSimple.super.createGame();
                   no_rounds--;
                   findWinner();
                   break;
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getKey().equals("isOpen")) {
                AlertDialog playerLeftAlert = new AlertDialog.Builder(
                        MathModeMultiSimple.this).create();
                playerLeftAlert.setMessage("The other player left the room.");
                playerLeftAlert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child("rooms").child(room.getKey()).removeValue();
                                endGame();
                            }
                        });
                playerLeftAlert.setCancelable(false);
                playerLeftAlert.show();
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}


