package team6.slidingtiles;

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
    private MathModeMultiSimple mathModeMultiSimple;
    boolean flag = false;

    Room room;
    int playerNum;
    public String id;
    private int rounds;

    public RoomFinder(MathModeMultiSimple mathModeMultiSimple){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms");
        this.mathModeMultiSimple = mathModeMultiSimple;
        rounds = mathModeMultiSimple.no_rounds;
    }

    public void getOpenRoom(){
        Log.d("In get open room", ": ");
        Query findOpenRoom = databaseReference.
                orderByChild("isOpen").equalTo(true);
        Log.d("query : ", findOpenRoom.toString());
        findOpenRoom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) { //
                    for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                        room = snapshot.getValue(Room.class);
                        Log.d("round result", String.valueOf(room.getNoRounds()));
                        if(room.getNoRounds()== rounds){
                            flag = true;
                            Log.d("flag true","going to find match");
                            break;
                        }

                    }
                    if(flag==false){
                        Log.d("flag false","going to find match");
                        createOpenRoom();
                    }
                    else{
                        Log.d("key value in listener", room.getKey());
                        playerNum = 2;
                        databaseReference.child(room.getKey()).child("isOpen").setValue(false);
                        databaseReference.child(room.getKey()).getRef().
                                addChildEventListener(mathModeMultiSimple.childEventListener);
                        mathModeMultiSimple.roomFound();
                    }

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
        room  = new Room(mathBoardList);
        String key1 = databaseReference.push().getKey();
        room.setKey(key1);
        room.setNoRounds(mathModeMultiSimple.no_rounds);
        databaseReference.child(key1).setValue(room);
        databaseReference.child(room.getKey()).getRef().
                addChildEventListener(mathModeMultiSimple.childEventListener);
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
