package team6.slidingtiles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScore extends AppCompatActivity  {

    ListView listViewUsers;
    List<UserScore> userScoreList;

    private DatabaseReference databaseReference;


    private void getHighScore(){
        Query highScoreQuery = databaseReference.orderByChild("score").limitToLast(5);

        highScoreQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userScoreList.clear();

                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    UserScore userScore = userSnapshot.getValue(UserScore.class);
                    userScoreList.add(userScore);
                }
                Collections.reverse(userScoreList);
                UserList adapter = new UserList(HighScore.this,userScoreList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_high_score);

        listViewUsers = findViewById(R.id.listViewUsers);

        userScoreList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        getHighScore();

    }
}
