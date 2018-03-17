package team6.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SaveScore extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSave;
    private EditText editTextScore;
    private Button buttonHighScore;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null) {
            //closing this activity
            finish();
            //start new activity
            Intent intent = new Intent(SaveScore.this,SigninPage.class);
            startActivity(intent);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonSave = findViewById(R.id.buttonSave);
        editTextScore = findViewById(R.id.editTextScore);
        buttonHighScore = findViewById(R.id.buttonHighScore);

        buttonSave.setOnClickListener(this);
        buttonHighScore.setOnClickListener(this);
    }

    private void saveScore(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        int score = Integer.parseInt(editTextScore.getText().toString().trim());
        String email = user.getEmail();
        String[] userName = email.split("@");
        String name = userName[0];

        UserScore userScore = new UserScore(name, score);

        databaseReference.child(databaseReference.push().getKey()).setValue(userScore);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSave){
            saveScore();
        }
        if(view == buttonHighScore){
            Intent intent = new Intent(SaveScore.this,HighScore.class);
            startActivity(intent);
        }
    }
}
