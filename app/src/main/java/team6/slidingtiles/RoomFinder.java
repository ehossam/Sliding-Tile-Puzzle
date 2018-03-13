package team6.slidingtiles;

import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

    public RoomFinder(GameMode gameMode, String mode){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms");
        this.mode = mode;
        this.gameMode = gameMode;

        Log.d("In Room Finder main", ": ");
    }

    public void getOpenRoom(){
        Log.d("In get open room", ": ");
        Query findOpenRoom = databaseReference.
                orderByChild("isOpen").equalTo(true).limitToFirst(1);
        Log.d("query : ", findOpenRoom.toString());
        findOpenRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Inside listener", ": going");

                if(dataSnapshot.exists()) {
                    flag = true;
                    Log.d("room result", String.valueOf(dataSnapshot.getValue()));
                    if(dataSnapshot.getChildren().iterator().hasNext())
                        room = dataSnapshot.getChildren().iterator().next().getValue(Room.class);
                    databaseReference.child(room.getKey()).child("isOpen").setValue(false);

                    Log.d("room result", String.valueOf(room));

                    Log.d("key value in listener", room.getKey());
                    playerNum = 2;
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
                } else {
                    Log.d("creating open room", ":in else part");
                    createOpenRoom();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("RoomFinder", "getOpenRoom Cancelled!");
            }
        });

    }

    public void createOpenRoom(){
        Log.d("Room finder yayy", "creating open room");
        MathBoard mathBoard = new MathBoard(false);
        List<String> mathBoardList = new ArrayList<>();
        for (int i = 0; i < mathBoard.getBoard().length; i++){
            mathBoardList.addAll(Arrays.asList(mathBoard.getBoard()[i]));
        }
        room  = new Room(mathBoardList);
        String key1 = databaseReference.push().getKey();
        room.setKey(key1);
        Log.d("key print in create", room.getKey());
        databaseReference.child(key1).setValue(room);
        if(mode.equals("MathModeMultiCut"))
            databaseReference.child(room.getKey()).getRef().
                    addChildEventListener(((MathModeMultiCut)gameMode).childEventListener);
        if(mode.equals("MathModeMultiSimple"))
            databaseReference.child(room.getKey()).getRef().
                    addChildEventListener(((MathModeMultiSimple)gameMode).childEventListener);
        playerNum = 1;
    }

    public void removeRoom(){
        if(mode.equals("MathModeMultiCut"))
            databaseReference.child(room.getKey()).getRef().
                    removeEventListener(((MathModeMultiCut)gameMode).childEventListener);
        if(mode.equals("MathModeMultiSimple"))
            databaseReference.child(room.getKey()).getRef().
                    removeEventListener(((MathModeMultiSimple)gameMode).childEventListener);
        databaseReference.child(room.getKey()).removeValue();
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
