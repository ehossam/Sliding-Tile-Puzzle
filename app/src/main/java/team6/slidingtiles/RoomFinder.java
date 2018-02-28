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
    private RoomFinderListener roomFinderListener;
    Room room;
    int playerNum;

    public RoomFinder(MathModeMultiSimple mathModeMultiSimple){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        roomFinderListener = mathModeMultiSimple;
    }

    public void getOpenRoom(){
        Query findOpenRoom = databaseReference.child("rooms").
                orderByChild("isOpen").equalTo(true).limitToFirst(1);
        findOpenRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    room = dataSnapshot.getValue(Room.class);
                    playerNum = 2;
                    databaseReference.child("rooms").child("isOpen").setValue(false);
                } else {
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
        MathBoard mathBoard = new MathBoard(false);
        List<String> mathBoardList = new ArrayList<>();
        for (int i = 0; i < mathBoard.getBoard().length; i++){
            mathBoardList.addAll(Arrays.asList(mathBoard.getBoard()[i]));
        }
        Room room  = new Room(mathBoardList);
        String key = databaseReference.push().getKey();
        room.setKey(key);
        databaseReference.child("rooms").child(key).setValue(room);
        playerNum = 1;
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
