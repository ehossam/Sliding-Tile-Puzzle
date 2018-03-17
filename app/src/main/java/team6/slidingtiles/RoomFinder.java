package team6.slidingtiles;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cheesea on 2/27/18.
 */

public class RoomFinder {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private GameMode gameMode;
    boolean flag = false;
    private String mode;

    Room room;
    int playerNum;
    public String id;
    private int rounds;

    public RoomFinder(GameMode gameMode, String mode, int rounds){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms");
        this.rounds = rounds;
        this.mode = mode;
        this.gameMode = gameMode;

        Log.d("rounds: ", Integer.toString(rounds));
    }

    public void getOpenRoom(){
        Log.d("In get open room", ": ");
        Query findOpenRoom = databaseReference.
                orderByChild("isOpen").equalTo(true);
        Log.d("query : ", findOpenRoom.toString());
        findOpenRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { //
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        room = snapshot.getValue(Room.class);
                        Log.d("round result", String.valueOf(room.getNoRounds()));
                        if (room.getNoRounds() == rounds) {
                            flag = true;
                            Log.d("flag true", "going to find match");
                            break;
                        }
                    }
                }
                if (flag == false) {
                    Log.d("flag false", "going to find match");
                    createOpenRoom();
                } else {
                    Log.d("key value in listener", room.getKey());
                    playerNum = 2;

                    databaseReference.child(room.getKey()).child("isOpen").setValue(false);

                    if (mode.equals("MathModeMultiCut")) {
                        databaseReference.child(room.getKey()).getRef().
                                addChildEventListener(((MathModeMultiCut) gameMode).childEventListener);
                        ((MathModeMultiCut) gameMode).roomFound();
                    }
                    if (mode.equals("MathModeMultiSimple")) {
                        databaseReference.child(room.getKey()).getRef().
                                addChildEventListener(((MathModeMultiSimple) gameMode).childEventListener);
                        ((MathModeMultiSimple) gameMode).roomFound();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("RoomFinder", "getOpenRoom Cancelled!");
            }
        });

    }

    public void createOpenRoom(){
        MathBoard mathBoard = new MathBoard(false);
        List<String> mathBoardList = new ArrayList<>();
        for (int i = 0; i < mathBoard.getBoard().length; i++){
            mathBoardList.addAll(Arrays.asList(mathBoard.getBoard()[i]));
        }
        room  = new Room(mathBoardList);
        String key1 = databaseReference.push().getKey();
        room.setKey(key1);
        Log.d("createOpenRoom", mode);
        room.setNoRounds(rounds);
        databaseReference.child(key1).setValue(room);

        if(mode.equals("MathModeMultiCut")) {
            databaseReference.child(room.getKey()).getRef().
                    addChildEventListener(((MathModeMultiCut) gameMode).childEventListener);
        }else if(mode.equals("MathModeMultiSimple")) {
            databaseReference.child(room.getKey()).getRef().
                    addChildEventListener(((MathModeMultiSimple) gameMode).childEventListener);
        }
        playerNum = 1;
    }

    public void removeRoom(){
        if(room != null) {
            if (mode.equals("MathModeMultiCut"))
                databaseReference.child(room.getKey()).getRef().
                        removeEventListener(((MathModeMultiCut) gameMode).childEventListener);
            if (mode.equals("MathModeMultiSimple"))
                databaseReference.child(room.getKey()).getRef().
                        removeEventListener(((MathModeMultiSimple) gameMode).childEventListener);
            databaseReference.child(room.getKey()).removeValue();
        }
    }

    interface RoomFinderListener{
        public void roomFound();
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public Room getRoom() {
        return room;
    }
}
